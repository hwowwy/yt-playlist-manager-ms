package it.lunacia.yt.playlist.items.dto;

import java.util.List;

public record PlaylistItemsDTO(String nextPageToken, List<PlaylistItemDTO> items) {
}
