package web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import services.SigninService;

@RestController
public class SpotifyController {

    @Autowired
    private SigninService _loginService;

    @GetMapping(value = "/signinuri")
    public String getSigninUri() {
        return _loginService.getSigninUri();
    }

}
