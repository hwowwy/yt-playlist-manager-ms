package it.lunacia.yt.controller;

import it.lunacia.yt.playlist.items.dto.PlaylistItemDTO;
import it.lunacia.yt.playlists.dto.CreatePlaylistDTO;
import it.lunacia.yt.playlists.dto.ImportedPlaylistDTO;
import it.lunacia.yt.playlists.dto.PlaylistDTO;
import it.lunacia.yt.service.PlaylistService;
import it.lunacia.yt.service.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
public class PlaylistController {
    @Autowired
    private YoutubeService ytService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @GetMapping("/playlists")
    public Flux<PlaylistDTO> playlists(){
        return ytService.retrieveMyPlaylists();
    }

    @GetMapping("/playlist/{id}")
    public Flux<PlaylistItemDTO> playlists(@PathVariable("id") final String playListId){
        return ytService.retrievePlaylistElements(playListId);
    }

    @PostMapping("/playlist")
    public Mono<Object> createPlaylist(@RequestBody CreatePlaylistDTO playlist){
        return ytService.createPlaylist(playlist.title(), playlist.description(), playlist.privacy());
    }

    @GetMapping("/import/playlist/{id}")
    public Mono<String> importPlaylist(@PathVariable("id") final String playlistId, ServerWebExchange exchange){
        return playlistService.importPlaylistByYtPlaylistId(playlistId).collectList().flatMap(res -> Mono.just("OK"));
    }

    @GetMapping("/imported/playlists")
    public Flux<ImportedPlaylistDTO> importedPlaylists(){
        return playlistService.importedPlaylists();
    }
}
