package services;

import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import constants.Constants;

@Service
public class SigninService {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    @Value("${spotify.redirectURI}")
    private String uri;

    public String getSigninUri() {

        URI redirectUri = SpotifyHttpManager.makeUri(this.uri);

        SpotifyApi api = new SpotifyApi.Builder()
            .setClientSecret(clientSecret)
            .setClientId(clientId)
            .setRedirectUri(redirectUri)
            .build();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
            .scope(Constants.SCOPES)
            .show_dialog(true)
            .build();

        URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();

    }

}
