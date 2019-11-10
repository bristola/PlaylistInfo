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
import com.wrapper.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import constants.Constants;

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

    public PlaylistSimplified[] getUserPlaylists(String code) throws IOException, SpotifyWebApiException {

        String userID = this.authorizeApi(code);

        GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = this.api
            .getListOfUsersPlaylists(userID)
            .offset(0)
            .build();

        // Execute request
        Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
        // Turn result into an array
        PlaylistSimplified[] playlists = playlistSimplifiedPaging.getItems();

        return playlists;

   }

   private String authorizeApi(String code) throws IOException, SpotifyWebApiException {

       AuthorizationCodeRequest authorizationCodeRequest = this.api.authorizationCode(code.trim())
            .build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

        this.api.setAccessToken(authorizationCodeCredentials.getAccessToken());
        this.api.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = this.api.getCurrentUsersProfile()
            .build();

        User user = getCurrentUsersProfileRequest.execute();

        return user.getId();
   }

}
