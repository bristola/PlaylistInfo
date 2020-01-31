package domain;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;

import domain.Song;
import constants.Constants;

@Component
public class SongMapper {

    public static Song map(PlaylistTrack playlistTrack, List<Artist> artists) {
        Song song = new Song();

        Track track = playlistTrack.getTrack();
        song.setName(track.getName());
        song.setExplicit(track.getIsExplicit());
        song.setPopularity(track.getPopularity());
        song.setDuration(track.getDurationMs());

        AlbumSimplified album = track.getAlbum();
        song.setAlbum(album.getName());
        song.setAlbumUrl(album.getExternalUrls()
                              .getExternalUrls()
                              .get(Constants.ALBUM_KEY));
        song.setAlbumCoverUrl(album.getImages()[0]
                                   .getUrl());
        song.setReleaseDate(album.getReleaseDate());

        List<ArtistSimplified> artistSimplifieds = Arrays.asList(track.getArtists());
        List<String> artistStrs = artistSimplifieds.stream()
                                                .map(a -> a.getName())
                                                .collect(Collectors.toList());
        song.setArtists(artistStrs);

        List<String> artistIds = artistSimplifieds.stream()
                                                  .map(a -> a.getId())
                                                  .collect(Collectors.toList());
        List<String> genres = artists.stream()
                                     .filter(a -> artistIds.contains(a.getId()))
                                     .flatMap(a -> Arrays.stream(a.getGenres()))
                                     .collect(Collectors.toList());
        song.setGenres(genres);

        return song;
    }

}
