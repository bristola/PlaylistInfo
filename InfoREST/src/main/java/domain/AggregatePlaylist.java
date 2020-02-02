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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToMany(cascade=CascadeType.ALL)
    private List<Song> songs;

    private String name;
    private String description;
    private String spotifyId;
    private String createdBy;
    private String spotifyUrl;
    private String imageUrl;
    private Integer followers;
    private Boolean isCollaborative;
    private Boolean isPublic;
}
