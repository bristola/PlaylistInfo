package controllers;

import java.io.IOException;
import java.util.List;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import constants.Constants;
import domain.AggregatePlaylist;
import domain.SimplePlaylist;
import services.PlaylistService;

@RestController
@RequestMapping("playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService _playlistService;

    @GetMapping(value = "/spotify/{page}")
    public List<SimplePlaylist> getPlaylists(@PathVariable("page") int page,
                                             @RequestHeader(Constants.ACCESS_HEADER) String accessToken) throws IOException, SpotifyWebApiException {
        return _playlistService.getUserPlaylists(accessToken, page);
    }

    @PostMapping(value = "/info/{playlistId}")
    public void generatePlaylistInfo(@PathVariable("playlistId") String playlistId,
                                     @RequestHeader(Constants.ACCESS_HEADER) String accessToken) throws InterruptedException, IOException, SpotifyWebApiException {
        _playlistService.generatePlaylistInfo(accessToken, playlistId);
    }

    @GetMapping(value = "/info/{playlistId}")
    public AggregatePlaylist getPlaylistInfo(@PathVariable("playlistId") String playlistId) {
        return _playlistService.getPlaylistInfo(playlistId);
    }

    @GetMapping(value = "/existing")
    public List<SimplePlaylist> getExistingPlaylists(@RequestHeader(Constants.ACCESS_HEADER) String accessToken) throws InterruptedException, IOException, SpotifyWebApiException {
        return _playlistService.getExistingPlaylists(accessToken);
    }
}
