package controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import services.SpotifyService;
import constants.Constants;
import domain.AuthorizeResponse;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyService _spotifyService;

    @GetMapping(value = "/signinuri")
    public String getSigninUri() {
        return _spotifyService.getSigninUri();
    }

    @GetMapping(value = "/authorize")
    public AuthorizeResponse authorizeUser(@RequestHeader(Constants.CODE_HEADER) String code) throws IOException, SpotifyWebApiException {
        return _spotifyService.authorizeUser(code);
    }

    @GetMapping(value = "/playlists/{page}")
    public PlaylistSimplified[] getPlaylists(@PathVariable("page") int page,
                                             @RequestHeader(Constants.ACCESS_HEADER) String accessToken,
                                             @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) throws IOException, SpotifyWebApiException {
        return _spotifyService.getUserPlaylists(accessToken, refreshToken, page);
    }

    // @GetMapping(value = "/generateinfo/{playlistID}")
    // public void generateInfo(@PathVariable("playlistID") String playlistID,
    //                          @RequestHeader(Constants.ACCESS_HEADER) String accessToken,
    //                          @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) throws IOException, SpotifyWebApiException {
    //     _spotifyService.generateInfo(accessToken, refreshToken, playlistID);
    // }
}
