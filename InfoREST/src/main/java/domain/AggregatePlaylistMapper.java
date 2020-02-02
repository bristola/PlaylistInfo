package domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Artist;

import domain.AggregatePlaylist;
import domain.Song;
import domain.SongMapper;
import constants.Constants;

@Component
public class AggregatePlaylistMapper {

    @Autowired
    private SongMapper songMapper;

    public static AggregatePlaylist map(Playlist playlist, List<PlaylistTrack> tracks, List<Artist> artists) {
        AggregatePlaylist aggregatePlaylist = new AggregatePlaylist();

        aggregatePlaylist.setName(playlist.getName());
        aggregatePlaylist.setSpotifyId(playlist.getId());
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
                                 .map(t -> SongMapper.map(t, artists))
                                 .collect(Collectors.toList());
        aggregatePlaylist.setSongs(songs);


        return aggregatePlaylist;
    }

}
