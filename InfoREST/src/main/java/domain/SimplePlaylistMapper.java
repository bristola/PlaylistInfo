package domain;

import org.springframework.stereotype.Component;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import domain.SimplePlaylist;
import constants.Constants;

@Component
public class SimplePlaylistMapper {

    public static SimplePlaylist map(PlaylistSimplified playlist) {
        SimplePlaylist simplePlaylist = new SimplePlaylist();

        simplePlaylist.setId(playlist.getId());
        simplePlaylist.setName(playlist.getName());
        simplePlaylist.setCreatedBy(playlist.getOwner()
                                            .getDisplayName());
        simplePlaylist.setSpotifyUrl(playlist.getExternalUrls()
                                             .getExternalUrls()
                                             .get(Constants.IMAGE_KEY));
        simplePlaylist.setImageUrl(playlist.getImages()[0]
                                           .getUrl());
        simplePlaylist.setIsCollaborative(playlist.getIsCollaborative());
        simplePlaylist.setIsPublic(playlist.getIsPublicAccess());

        return simplePlaylist;
    }

}
