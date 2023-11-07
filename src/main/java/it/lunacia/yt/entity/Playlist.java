package it.lunacia.yt.entity;

import it.lunacia.yt.playlists.dto.PlaylistDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    private String id;

    private String title;

    private String description;

    private String thumbnail;

    private String ytPlaylistId;

    private String channelId;

    public Playlist(PlaylistDTO item){
        this.title = item.snippet().title();
        this.channelId = item.snippet().channelId();
        this.thumbnail = item.snippet().thumbnails().get("default").url();
        this.ytPlaylistId = item.id();
        this.description = item.snippet().description();
    }
}
