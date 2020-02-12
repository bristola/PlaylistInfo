package services;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;

import services.SpotifyService;
import constants.Constants;
import domain.AuthorizeResponse;
import domain.SimplePlaylist;
import domain.AggregatePlaylist;
import domain.SimplePlaylistMapper;
import domain.AggregatePlaylistMapper;
import repos.AggregatePlaylistRepo;

@Service
public class PlaylistService {

    @Autowired
    private SpotifyService _spotifyService;

    @Autowired
    private AggregatePlaylistRepo _aggregatePlaylistRepo;

    @Autowired
    private SimplePlaylistMapper simplePlaylistMapper;

    @Autowired
    private AggregatePlaylistMapper aggregatePlaylistMapper;

    public List<SimplePlaylist> getUserPlaylists(String accessToken, String refreshToken, int page) throws IOException, SpotifyWebApiException {
        List<PlaylistSimplified> playlists = _spotifyService.getUserPlaylists(accessToken, refreshToken, page);

        List<SimplePlaylist> simplePlaylists = playlists.stream()
                                                        .map(p -> SimplePlaylistMapper.map(p))
                                                        .collect(Collectors.toList());

        return simplePlaylists;
    }

    public void generatePlaylistInfo(String accessToken, String refreshToken, String playlistId) throws InterruptedException, IOException, SpotifyWebApiException {
        Playlist playlist = _spotifyService.getPlaylist(accessToken, refreshToken, playlistId);

        List<PlaylistTrack> tracks = _spotifyService.getPlaylistTracks(accessToken, refreshToken, playlistId);

        List<String> artistIds = tracks.stream()
                                       .flatMap(t -> Arrays.asList(t.getTrack().getArtists()).stream().map(a -> a.getId()))
                                       .distinct()
                                       .collect(Collectors.toList());

        List<Artist> artists = _spotifyService.getArtists(accessToken, refreshToken, artistIds);

        String currentUser = _spotifyService.getUsername(accessToken, refreshToken);

        AggregatePlaylist aggregatePlaylist = AggregatePlaylistMapper.map(playlist, tracks, artists, currentUser);

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
