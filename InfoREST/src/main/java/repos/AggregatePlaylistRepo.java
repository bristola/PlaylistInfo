package repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import domain.AggregatePlaylist;

@Repository
public interface AggregatePlaylistRepo extends CrudRepository<AggregatePlaylist, Long> {
    AggregatePlaylist findBySpotifyId(String spotifyId);
}
