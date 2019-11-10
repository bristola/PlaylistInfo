package controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import services.SpotifyService;
import constants.Constants;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyService _spotifyService;

    @GetMapping(value = "/signinuri")
    public String getSigninUri() {
        return _spotifyService.getSigninUri();
    }

    @GetMapping(value = "/playlists")
    public PlaylistSimplified[] getPlaylists(@RequestHeader(Constants.CODE_HEADER) String code) throws IOException, SpotifyWebApiException{
        return _spotifyService.getUserPlaylists(code);
    }

}
