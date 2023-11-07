package it.lunacia.yt.playlist.items.dto;

import it.lunacia.yt.dto.ThumbnailDTO;

import java.util.Map;

public record PlaylistItemSnippetDTO(String channelId, String title, Map<String, ThumbnailDTO> thumbnails) {
}
