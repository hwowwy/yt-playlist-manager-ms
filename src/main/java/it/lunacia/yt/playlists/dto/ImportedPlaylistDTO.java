package it.lunacia.yt.playlists.dto;

import it.lunacia.yt.entity.Playlist;

public record ImportedPlaylistDTO(String title, String description, String ytPlaylistId, String thumbnail) {
    public ImportedPlaylistDTO(Playlist pl){
        this(pl.getTitle(),pl.getDescription(),pl.getYtPlaylistId(),pl.getThumbnail());
    }

    public ImportedPlaylistDTO(PlaylistDTO pl){
        this(pl.snippet().title(),pl.snippet().description(),pl.id(),pl.snippet().thumbnails().get("default").url());
    }
}
