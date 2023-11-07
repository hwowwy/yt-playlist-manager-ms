package it.lunacia.yt.playlists.dto;

import it.lunacia.yt.constants.YoutubePlaylistPrivacyEnum;

public record CreatePlaylistDTO(String title, String description, YoutubePlaylistPrivacyEnum privacy) {
}
