package controllers;

import java.io.IOException;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import constants.Constants;
import services.SpotifyService;

@RestController
@RequestMapping("authorization")
public class AuthorizationController {

    @Autowired
    private SpotifyService _spotifyService;

    @GetMapping(value = "/signinuri")
    public String getSigninUri() throws IOException, SpotifyWebApiException {
        return _spotifyService.getSigninUri();
    }

    @GetMapping(value = "/access")
    public String authorizeUser(@RequestHeader(Constants.CODE_HEADER) String code) throws IOException, SpotifyWebApiException {
        return _spotifyService.authorizeUser(code);
    }

}
