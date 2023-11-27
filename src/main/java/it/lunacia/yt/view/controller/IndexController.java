package it.lunacia.yt.view.controller;

import it.lunacia.yt.playlists.dto.ImportedPlaylistDTO;
import it.lunacia.yt.service.PlaylistService;
import it.lunacia.yt.service.YoutubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class IndexController extends BaseController{

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private YoutubeService ytService;

    @GetMapping("/")
    public Mono<Rendering> index(Model model, WebSession session, @RequestParam(value="nextToken",required = false) final String nextToken){
        return playlistService.importedPlaylists().collectList().flatMap(importedPlaylists -> {
           List<String> importedIds = importedPlaylists.stream().map(ImportedPlaylistDTO::ytPlaylistId).toList();
            return ytService.retrieveMyPlaylists(Optional.ofNullable(nextToken)).flatMap(availablePlaylists -> {
                List<ImportedPlaylistDTO> filteredAvailablePlaylists = availablePlaylists.items().stream().filter(el -> !importedIds.contains(el.id())).map(ImportedPlaylistDTO::new).toList();
                List<ImportedPlaylistDTO> resultingPlaylists = new ArrayList<>(filteredAvailablePlaylists);
                resultingPlaylists.add(new ImportedPlaylistDTO("I miei mi piace","Playlist automatica dei mi piace","LL","#"));
                return setRedirectAttributes(model,session).thenReturn(Rendering.view("index").modelAttribute("nextPageToken",availablePlaylists.nextPageToken()).modelAttribute("importedPlaylists",importedPlaylists).modelAttribute("availablePlaylists",resultingPlaylists).build());
            });
        });
    }
}
