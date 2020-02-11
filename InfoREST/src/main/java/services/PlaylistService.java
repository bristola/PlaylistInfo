package services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;

import services.SpotifyService;
import constants.Constants;
import domain.AuthorizeResponse;
import domain.SimplePlaylist;
import domain.AggregatePlaylist;
import domain.SimplePlaylistMapper;
import repos.AggregatePlaylistRepo;

@Service
public class PlaylistService {

    @Autowired
    private SpotifyService _spotifyService;

    @Autowired
    private AggregatePlaylistRepo _aggregatePlaylistRepo;

    @Autowired
    private SimplePlaylistMapper simplePlaylistMapper;

    public List<SimplePlaylist> getUserPlaylists(String accessToken, String refreshToken, int page) throws IOException, SpotifyWebApiException {
        return _spotifyService.getUserPlaylists(accessToken, refreshToken, page);
    }

    public void generatePlaylistInfo(String accessToken, String refreshToken, String playlistId) throws InterruptedException, IOException, SpotifyWebApiException {
        AggregatePlaylist aggregatePlaylist = _spotifyService.generatePlaylistInfo(accessToken, refreshToken, playlistId);
        _aggregatePlaylistRepo.save(aggregatePlaylist);
    }

    public AggregatePlaylist getPlaylistInfo(String playlistId) {
        return _aggregatePlaylistRepo.findBySpotifyId(playlistId);
    }

    public List<SimplePlaylist> getExistingPlaylists(String accessToken, String refreshToken) throws InterruptedException, IOException, SpotifyWebApiException {
        String username = _spotifyService.getUsername(accessToken, refreshToken);
        List<AggregatePlaylist> aggregatePlaylists = _aggregatePlaylistRepo.findByCurrentUsername(username);
        List<SimplePlaylist> simplePlaylists = aggregatePlaylists.stream()
                                                                 .map(a -> SimplePlaylistMapper.map(a))
                                                                 .collect(Collectors.toList());
        return simplePlaylists;
    }

}
