package services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import constants.Constants;
import domain.AggregatePlaylist;
import domain.AggregatePlaylistMapper;
import domain.SimplePlaylist;
import domain.SimplePlaylistMapper;
import repos.AggregatePlaylistRepo;
import services.SpotifyService;

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

    public List<SimplePlaylist> getUserPlaylists(String accessToken, int page) throws IOException, SpotifyWebApiException {
        List<PlaylistSimplified> playlists = _spotifyService.getUserPlaylists(accessToken, page);

        List<SimplePlaylist> simplePlaylists = playlists.stream()
                                                        .map(p -> SimplePlaylistMapper.map(p))
                                                        .collect(Collectors.toList());

        return simplePlaylists;
    }

    public void generatePlaylistInfo(String accessToken, String playlistId) throws InterruptedException, IOException, SpotifyWebApiException {
        Playlist playlist = _spotifyService.getPlaylist(accessToken, playlistId);

        List<PlaylistTrack> tracks = _spotifyService.getPlaylistTracks(accessToken, playlistId);

        List<String> artistIds = tracks.stream()
                                       .flatMap(t -> Arrays.asList(t.getTrack().getArtists()).stream().map(a -> a.getId()))
                                       .distinct()
                                       .collect(Collectors.toList());

        List<Artist> artists = _spotifyService.getArtists(accessToken, artistIds);

        String currentUser = _spotifyService.getUsername(accessToken);

        AggregatePlaylist aggregatePlaylist = AggregatePlaylistMapper.map(playlist, tracks, artists, currentUser);

        _aggregatePlaylistRepo.save(aggregatePlaylist);
    }

    public AggregatePlaylist getPlaylistInfo(String playlistId) {
        return _aggregatePlaylistRepo.findBySpotifyId(playlistId);
    }

    public List<SimplePlaylist> getExistingPlaylists(String accessToken) throws InterruptedException, IOException, SpotifyWebApiException {
        String username = _spotifyService.getUsername(accessToken);
        List<AggregatePlaylist> aggregatePlaylists = _aggregatePlaylistRepo.findByCurrentUsername(username);
        List<SimplePlaylist> simplePlaylists = aggregatePlaylists.stream()
                                                                 .map(a -> SimplePlaylistMapper.map(a))
                                                                 .collect(Collectors.toList());
        return simplePlaylists;
    }

}
