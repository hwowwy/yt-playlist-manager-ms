package it.lunacia.yt.entity;

import it.lunacia.yt.playlist.items.dto.PlaylistItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="playlist_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistItem {

    @Id
    private String id;

    private String itemId;

    private String title;

    private String description;

    private String thumbnail;

    private String ytPlaylistId;

    private String kind;

    public PlaylistItem(PlaylistItemDTO item, String ytPlaylistId){
        this.itemId = item.snippet().resourceId().videoId();
        this.title = item.snippet().title();
        this.thumbnail = item.snippet().thumbnails().get("default").url();
        this.ytPlaylistId = ytPlaylistId;
        this.description = item.snippet().description();
        this.kind = item.snippet().resourceId().kind();
    }
}
