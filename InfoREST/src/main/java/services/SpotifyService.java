package services;

import java.net.URI;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import com.wrapper.spotify.requests.data.artists.GetSeveralArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.google.common.collect.Lists;

import constants.Constants;
import domain.AuthorizeResponse;
import domain.SimplePlaylist;
import domain.AggregatePlaylist;
import domain.SimplePlaylistMapper;
import domain.AggregatePlaylistMapper;

@Service
public class SpotifyService {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    @Value("${spotify.redirectURI}")
    private String uri;

    @Autowired
    private SimplePlaylistMapper simplePlaylistMapper;

    @Autowired
    private AggregatePlaylistMapper aggregatePlaylistMapper;

    /**
    * Gets a link to a spotify page for the user to sign into.
    *
    * @return URI to signin page
    */
    public String getSigninUri() throws IOException, SpotifyWebApiException {

        SpotifyApi api = this.getAPI();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
                                                                     .scope(Constants.SCOPES)
                                                                     .show_dialog(false)
                                                                     .build();

        URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();

    }

    /**
    * Authorizes the user to Spotify using returned code from redirect.
    *
    * @param code from Spotify redirect
    * @return Authorize object containing signin information.
    */
    public AuthorizeResponse authorizeUser(String code) throws IOException, SpotifyWebApiException {

        SpotifyApi api = this.getAPI();

        AuthorizationCodeRequest authorizationCodeRequest = api.authorizationCode(code.trim())
            .build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        String accessToken = authorizationCodeCredentials.getAccessToken();
        String refreshToken = authorizationCodeCredentials.getRefreshToken();

        AuthorizeResponse response = new AuthorizeResponse(accessToken, refreshToken);

        return response;
    }
    
    public List<SimplePlaylist> getUserPlaylists(String accessToken, String refreshToken, int page) throws IOException, SpotifyWebApiException {

        SpotifyApi api = this.getAuthorizedAPI(accessToken, refreshToken);

        GetListOfCurrentUsersPlaylistsRequest getListOfUsersPlaylistsRequest = api.getListOfCurrentUsersPlaylists()
                                                                                  .limit(Constants.PLAYLIST_NUM)
                                                                                  .offset(Constants.PLAYLIST_NUM * page)
                                                                                  .build();

        // Execute request
        Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();

        // Turn result into a list
        List<PlaylistSimplified> playlists = Arrays.asList(playlistSimplifiedPaging.getItems());

        // Map to custom objects
        List<SimplePlaylist> simplePlaylists = playlists.stream()
                                                        .map(p -> SimplePlaylistMapper.map(p))
                                                        .collect(Collectors.toList());

        return simplePlaylists;
    }

    public AggregatePlaylist generatePlaylistInfo(String accessToken, String refreshToken, String playlistID) throws InterruptedException, IOException, SpotifyWebApiException {

        SpotifyApi api = this.getAuthorizedAPI(accessToken, refreshToken);

        GetPlaylistRequest getPlaylistRequest = api.getPlaylist(playlistID)
                                                   .build();

        Playlist playlist = getPlaylistRequest.execute();

        List<PlaylistTrack> tracks = this.getPlaylistTracks(api, playlistID);

        List<String> artistIds = tracks.stream()
                                       .flatMap(t -> Arrays.asList(t.getTrack().getArtists()).stream().map(a -> a.getId()))
                                       .distinct()
                                       .collect(Collectors.toList());

        List<Artist> artists = this.getArtists(api, artistIds);

        String currentUser = this.getUsername(api);

        return AggregatePlaylistMapper.map(playlist, tracks, artists, currentUser);
    }

    public String getUsername(String accessToken, String refreshToken) throws IOException, SpotifyWebApiException {
        SpotifyApi api = this.getAuthorizedAPI(accessToken, refreshToken);

        return this.getUsername(api);
    }

    private String getUsername(SpotifyApi api) throws IOException, SpotifyWebApiException {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = api.getCurrentUsersProfile()
                                                                         .build();

        return getCurrentUsersProfileRequest.execute().getDisplayName();
    }

    private List<PlaylistTrack> getPlaylistTracks(SpotifyApi api, String id) throws InterruptedException, IOException, SpotifyWebApiException {
        int page = 0;
        List<PlaylistTrack> allTracks = new ArrayList<PlaylistTrack>();

        do {
            if (page != 0)
                Thread.sleep(1000);

            GetPlaylistsTracksRequest getPlaylistsTracksRequest = api.getPlaylistsTracks(id)
                                                                     .limit(Constants.TRACKS_NUM)
                                                                     .offset(Constants.TRACKS_NUM * page)
                                                                     .build();

            Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
            List<PlaylistTrack> tracks = Arrays.asList(playlistTrackPaging.getItems());

            allTracks.addAll(tracks);

            page++;

        } while (allTracks != null && allTracks.size() % Constants.TRACKS_NUM == 0);

        return allTracks;
    }

    private List<Artist> getArtists(SpotifyApi api, List<String> ids) throws InterruptedException, IOException, SpotifyWebApiException {
        int page = 0;
        List<Artist> allArtists = new ArrayList<Artist>();
        List<List<String>> requestIds = Lists.partition(ids, Constants.ARTIST_NUM);

        for (List<String> request : requestIds) {
            if (page != 0)
                Thread.sleep(1000);

            String[] idsArray = request.toArray(new String[request.size()]);

            GetSeveralArtistsRequest getSeveralArtistsRequest = api.getSeveralArtists(idsArray)
                                                                   .build();

            Artist[] artists = getSeveralArtistsRequest.execute();

            allArtists.addAll(Arrays.asList(artists));

            page++;
        }

        return allArtists;
    }

    private SpotifyApi getAPI() throws IOException, SpotifyWebApiException {
        URI redirectUri = SpotifyHttpManager.makeUri(uri);

        SpotifyApi api = new SpotifyApi.Builder()
                                       .setClientSecret(clientSecret)
                                       .setClientId(clientId)
                                       .setRedirectUri(redirectUri)
                                       .build();

        return api;
    }

    private SpotifyApi getAuthorizedAPI(String accessToken, String refreshToken) throws IOException, SpotifyWebApiException {

        SpotifyApi api = new SpotifyApi.Builder()
                                       .setAccessToken(accessToken)
                                       .setRefreshToken(refreshToken)
                                       .build();

       return api;

   }

}
