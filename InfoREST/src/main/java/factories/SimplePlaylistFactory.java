package factories;

import org.springframework.stereotype.Component;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import domain.SimplePlaylist;

@Component
public class SimplePlaylistFactory {

    public static SimplePlaylist create(PlaylistSimplified playlist) {
        SimplePlaylist simplePlaylist = new SimplePlaylist();

        simplePlaylist.setName(playlist.getName());
        simplePlaylist.setCreatedBy(playlist.getOwner()
                                            .getDisplayName());
        simplePlaylist.setSpotifyUrl(playlist.getExternalUrls()
                                             .getExternalUrls()
                                             .get("spotify"));
        simplePlaylist.setImageUrl(playlist.getImages()[0]
                                           .getUrl());
        simplePlaylist.setIsCollaborative(playlist.getIsCollaborative());
        simplePlaylist.setIsPublic(playlist.getIsPublicAccess());

        return simplePlaylist;
    }

}
