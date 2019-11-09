package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;

import services.LoginService;

/**
 * Controller which hadles all the requests after the user is already logged in.
 * Holds a session variable of spotifyUser. So that the user only needs to sign
 * in once and this state object is saved until the session is over or the user
 * logs out.
 */
@ComponentScan
@Controller
public class SpotifyController {

    @Autowired
    @Qualifier("LoginService")
    private LoginService _loginService;

    @RequestMapping(value = "/signinuri", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public String getSigninUri() {
        System.out.println("IN SIGNIN");
        return _loginService.getURI();
    }

}
