package it.lunacia.yt.playlists.dto;

import it.lunacia.yt.constants.YoutubePlaylistPrivacyEnum;

public record SavePlaylistStatusDTO(YoutubePlaylistPrivacyEnum privacyStatus) {
}
