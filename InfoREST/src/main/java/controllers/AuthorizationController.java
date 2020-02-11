package controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import services.SpotifyService;
import constants.Constants;
import domain.AuthorizeResponse;

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
    public AuthorizeResponse authorizeUser(@RequestHeader(Constants.CODE_HEADER) String code) throws IOException, SpotifyWebApiException {
        return _spotifyService.authorizeUser(code);
    }

}
