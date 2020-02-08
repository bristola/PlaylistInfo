package domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import domain.Song;

@Entity
@Getter @Setter @NoArgsConstructor
public class AggregatePlaylist {

    @Id
    private String spotifyId;

    @ManyToMany(cascade=CascadeType.ALL)
    private List<Song> songs;

    private String currentUsername;
    private String name;
    private String description;
    private String createdBy;
    private String spotifyUrl;
    private String imageUrl;
    private Integer followers;
    private Boolean isCollaborative;
    private Boolean isPublic;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AggregatePlaylist)) {
            return false;
        }
        AggregatePlaylist a = (AggregatePlaylist) o;
        return a.getSpotifyId() == this.getSpotifyId();
    }
}
