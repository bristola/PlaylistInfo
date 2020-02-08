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
import javax.persistence.Convert;
import javax.persistence.Table;

import domain.StringListConverter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Song {

    @Id
    private String spotifyId;

    @Convert(converter = StringListConverter.class)
    private List<String> artists;

    @Convert(converter = StringListConverter.class)
    private List<String> genres;

    private String name;
    private String album;
    private String albumUrl;
    private String albumCoverUrl;
    private String releaseDate;
    private Boolean explicit;
    private Integer popularity;
    private Integer duration;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Song)) {
            return false;
        }
        Song a = (Song) o;
        return a.getSpotifyId() == this.getSpotifyId();
    }
}
