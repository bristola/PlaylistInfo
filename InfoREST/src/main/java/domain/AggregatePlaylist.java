package domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import domain.Song;

@Getter @Setter @NoArgsConstructor
public class AggregatePlaylist {
    private String name;
    private String createdBy;
    private String description;
    private String spotifyUrl;
    private String imageUrl;
    private Integer followers;
    private Boolean isCollaborative;
    private Boolean isPublic;
    private List<Song> songs;
}
