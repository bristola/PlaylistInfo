package services;

import java.net.URI;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
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
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import constants.Constants;
import domain.AuthorizeResponse;

@Service
public class SpotifyService {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    @Value("${spotify.redirectURI}")
    private String uri;

    private SpotifyApi api;

    @PostConstruct
    public void init() {
        URI redirectUri = SpotifyHttpManager.makeUri(uri);

        this.api = new SpotifyApi.Builder()
            .setClientSecret(clientSecret)
            .setClientId(clientId)
            .setRedirectUri(redirectUri)
            .build();
    }

    public String getSigninUri() {

        AuthorizationCodeUriRequest authorizationCodeUriRequest = this.api.authorizationCodeUri()
            .scope(Constants.SCOPES)
            .show_dialog(true)
            .build();

        URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();

    }

    public AuthorizeResponse authorizeUser(String code) throws IOException, SpotifyWebApiException {

        AuthorizationCodeRequest authorizationCodeRequest = this.api.authorizationCode(code.trim())
            .build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        String accessToken = authorizationCodeCredentials.getAccessToken();
        String refreshToken = authorizationCodeCredentials.getRefreshToken();

        AuthorizeResponse response = new AuthorizeResponse(accessToken, refreshToken);

        return response;
    }

    public PlaylistSimplified[] getUserPlaylists(String accessToken, String refreshToken, int page) throws IOException, SpotifyWebApiException {

        this.accessAPI(accessToken, refreshToken);

        GetListOfCurrentUsersPlaylistsRequest getListOfUsersPlaylistsRequest = this.api
            .getListOfCurrentUsersPlaylists()
            .limit(Constants.PLAYLIST_NUM)
            .offset(Constants.PLAYLIST_NUM * page)
            .build();

        // Execute request
        Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
        // Turn result into an array
        PlaylistSimplified[] playlists = playlistSimplifiedPaging.getItems();

        return playlists;

    }

    public void generateInfo(String accessToken, String refreshToken, String playlistID) throws IOException, SpotifyWebApiException {

        this.accessAPI(accessToken, refreshToken);

        GetPlaylistsTracksRequest getPlaylistsTracksRequest = api
            .getPlaylistsTracks(playlistID)
            .build();

        Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
        PlaylistTrack[] tracks = playlistTrackPaging.getItems();

        List<>

        for (PlaylistTrack track : tracks) {
            System.out.println(track);
        }

    }

   private void accessAPI(String accessToken, String refreshToken) throws IOException, SpotifyWebApiException {

       this.api.setAccessToken(accessToken);
       this.api.setRefreshToken(refreshToken);

   }

}
