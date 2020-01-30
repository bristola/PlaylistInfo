package factories;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Artist;

import domain.AggregatePlaylist;
import domain.Song;
import factories.SongFactory;
import constants.Constants;

@Component
public class AggregatePlaylistFactory {

    @Autowired
    private SongFactory songFactory;

    public static AggregatePlaylist create(Playlist playlist, List<PlaylistTrack> tracks, List<Artist> artists) {
        AggregatePlaylist aggregatePlaylist = new AggregatePlaylist();

        aggregatePlaylist.setName(playlist.getName());
        aggregatePlaylist.setCreatedBy(playlist.getOwner()
                                               .getDisplayName());
        aggregatePlaylist.setDescription(playlist.getDescription());
        aggregatePlaylist.setSpotifyUrl(playlist.getExternalUrls()
                                                .getExternalUrls()
                                                .get(Constants.IMAGE_KEY));
        aggregatePlaylist.setImageUrl(playlist.getImages()[0]
                                              .getUrl());
        aggregatePlaylist.setFollowers(playlist.getFollowers()
                                               .getTotal());
        aggregatePlaylist.setIsCollaborative(playlist.getIsCollaborative());
        aggregatePlaylist.setIsPublic(playlist.getIsPublicAccess());

        List<Song> songs = tracks.stream()
                                 .map(t -> SongFactory.create(t, artists))
                                 .collect(Collectors.toList());
        aggregatePlaylist.setSongs(songs);


        return aggregatePlaylist;
    }

}
