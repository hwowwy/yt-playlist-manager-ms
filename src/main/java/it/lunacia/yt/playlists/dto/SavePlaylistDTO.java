package it.lunacia.yt.playlists.dto;

import java.util.Map;

public record SavePlaylistDTO(SavePlaylistSnippetDTO snippet, SavePlaylistStatusDTO status, Map<String,SavePlaylistLocalizationDTO> localizations) {
}
