package it.lunacia.yt.playlists.dto;

import it.lunacia.yt.dto.ThumbnailDTO;

import java.util.Map;

public record PlaylistSnippetDTO(String channelId, String title,String description, Map<String, ThumbnailDTO> thumbnails) {
}
