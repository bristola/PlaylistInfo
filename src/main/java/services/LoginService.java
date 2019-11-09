package services;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This code is all the spotify functions that are needed for the website
 * which do not require an authentication code. This means that it does not need
 * to connect to a spotify user's account to access information.
 */
@Component("LoginService")
public class LoginService {

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.clientSecret}")
    private String clientSecret;

    @Value("${spotify.redirectURI}")
    private String uri;

    String scopes = "user-read-email,"
                  + "streaming,"
                  + "user-read-birthdate,"
                  + "playlist-read-private,"
                  + "user-read-private,"
                  + "playlist-modify-public,"
                  + "playlist-modify-private,"
                  + "playlist-read-collaborative";

    /*
        Generates spotify login url
    */
    public String getURI() {

        try {
            URI redirectUri = SpotifyHttpManager.makeUri(this.uri);
            SpotifyApi api = new SpotifyApi.Builder()
                .setClientSecret(clientSecret)
                .setClientId(clientId)
                .setRedirectUri(redirectUri)
                .build();
            AuthorizationCodeUriRequest authorizationCodeUriRequest = api.authorizationCodeUri()
                .state("x4xkmn9pu3j6ukrs8n")
                .scope(this.scopes)
                .show_dialog(true)
                .build();
            URI uri = authorizationCodeUriRequest.execute();

            return uri.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}
