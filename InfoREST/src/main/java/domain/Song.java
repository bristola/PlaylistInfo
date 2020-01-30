package domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter @Setter @NoArgsConstructor
public class Song {
    private String name;
    private String album;
    private String albumUrl;
    private String albumCoverUrl;
    private String releaseDate;
    private Boolean explicit;
    private Integer popularity;
    private Integer duration;
    private List<String> artists;
    private List<String> genres;
}
