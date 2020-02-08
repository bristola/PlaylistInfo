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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import services.SpotifyService;
import constants.Constants;
import domain.AuthorizeResponse;
import domain.SimplePlaylist;
import domain.AggregatePlaylist;
import domain.SimplePlaylistMapper;
import repos.AggregatePlaylistRepo;

@RestController
public class SpotifyController {

    @Autowired
    private SpotifyService _spotifyService;

    @Autowired
    private AggregatePlaylistRepo _aggregatePlaylistRepo;

    @Autowired
    private SimplePlaylistMapper simplePlaylistMapper;

    @GetMapping(value = "/signinuri")
    public String getSigninUri() throws IOException, SpotifyWebApiException {
        return _spotifyService.getSigninUri();
    }

    @GetMapping(value = "/authorize")
    public AuthorizeResponse authorizeUser(@RequestHeader(Constants.CODE_HEADER) String code) throws IOException, SpotifyWebApiException {
        return _spotifyService.authorizeUser(code);
    }

    @GetMapping(value = "/playlists/{page}")
    public List<SimplePlaylist> getPlaylists(@PathVariable("page") int page,
                                             @RequestHeader(Constants.ACCESS_HEADER) String accessToken,
                                             @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) throws IOException, SpotifyWebApiException {
        return _spotifyService.getUserPlaylists(accessToken, refreshToken, page);
    }

    @PostMapping(value = "/playlistinfo/{playlistId}")
    public void generatePlaylistInfo(@PathVariable("playlistId") String playlistId,
                                     @RequestHeader(Constants.ACCESS_HEADER) String accessToken,
                                     @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) throws InterruptedException, IOException, SpotifyWebApiException {
        AggregatePlaylist aggregatePlaylist = _spotifyService.generatePlaylistInfo(accessToken, refreshToken, playlistId);
        _aggregatePlaylistRepo.save(aggregatePlaylist);
    }

    @GetMapping(value = "/playlistinfo/{playlistId}")
    public AggregatePlaylist getPlaylistInfo(@PathVariable("playlistId") String playlistId) {
        return _aggregatePlaylistRepo.findBySpotifyId(playlistId);
    }

    @GetMapping(value = "/existingplaylists")
    public List<SimplePlaylist> getExistingPlaylists(@RequestHeader(Constants.ACCESS_HEADER) String accessToken,
                                                     @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) throws InterruptedException, IOException, SpotifyWebApiException {
        String username = _spotifyService.getUsername(accessToken, refreshToken);
        List<AggregatePlaylist> aggregatePlaylists = _aggregatePlaylistRepo.findByCurrentUsername(username);
        List<SimplePlaylist> simplePlaylists = aggregatePlaylists.stream()
                                                                 .map(a -> SimplePlaylistMapper.map(a))
                                                                 .collect(Collectors.toList());
        return simplePlaylists;
    }
}
