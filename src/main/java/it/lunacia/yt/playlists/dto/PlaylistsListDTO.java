package it.lunacia.yt.playlists.dto;

import it.lunacia.yt.dto.PageInfoDTO;

import java.util.List;

public record PlaylistsListDTO(String nextPageToken, PageInfoDTO pageInfo, List<PlaylistDTO> items) {
}