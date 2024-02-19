package it.lunacia.yt.playlist.items.dto;

import it.lunacia.yt.entity.PlaylistItem;

public record ImportedPlaylistItemDTO(String title, String videoId, String kind, String thumbnail) {
    public ImportedPlaylistItemDTO(PlaylistItem item){
        this(item.getTitle(),item.getItemId(),item.getKind(),item.getThumbnail());
    }
}
