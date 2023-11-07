package it.lunacia.yt.view.controller;

import it.lunacia.yt.service.PlaylistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

@Controller
public class PlaylistItemsController extends BaseController{
    @Autowired
    private PlaylistItemService playlistItemService;

    @GetMapping("/playlist/detail/{id}")
    public Mono<Rendering> playlistDetail(@PathVariable("id") final String ytPlaylistId, Model model, WebSession session){
        return setRedirectAttributes(model,session).thenReturn(Rendering.view("playlistitems").modelAttribute("playlistItems",playlistItemService.getPlaylistDetail(ytPlaylistId)).build());
    }

}
