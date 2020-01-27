package domain;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter @NoArgsConstructor
public class SimplePlaylist {
    private String name;
    private String createdBy;
    private String spotifyUrl;
    private String imageUrl;
    private Boolean isCollaborative;
    private Boolean isPublic;
}
