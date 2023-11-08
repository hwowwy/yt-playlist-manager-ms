package it.lunacia.yt.service;

import it.lunacia.yt.entity.PlaylistItem;
import it.lunacia.yt.playlist.items.dto.ImportedPlaylistItemDTO;
import it.lunacia.yt.playlist.items.dto.PlaylistItemDTO;
import it.lunacia.yt.repository.PlaylistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaylistItemService {
    @Autowired
    private PlaylistItemRepository playlistItemRepository;

    public Mono<String> savePlaylistItems(String playlistId,Flux<PlaylistItemDTO> elementsToSave){
         return elementsToSave.flatMap(el -> playlistItemRepository.save(new PlaylistItem(el,playlistId))).then(Mono.just("OK"));
    }
    public Mono<String> savePlaylistItems(String playlistId,PlaylistItemDTO elementToSave){
        return playlistItemRepository.save(new PlaylistItem(elementToSave,playlistId)).flatMap(res ->
            Mono.just("OK"));
    }
    public Flux<ImportedPlaylistItemDTO> getPlaylistDetail(final String ytPlaylistId){
        return playlistItemRepository.findPlaylistItemByYtPlaylistId(ytPlaylistId).map(ImportedPlaylistItemDTO::new);
    }
}
