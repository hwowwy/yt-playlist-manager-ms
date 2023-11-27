package it.lunacia.yt.service;

import it.lunacia.yt.entity.Playlist;
import it.lunacia.yt.playlists.dto.ImportedPlaylistDTO;
import it.lunacia.yt.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playListRepository;

    @Autowired
    private PlaylistItemService playlistItemService;

    @Autowired
    private YoutubeService youtubeService;

    public Flux<String>  importPlaylistByYtPlaylistId(final String ytPlaylistId){
        Mono<String> savePlaylist = youtubeService.retrievePlaylist(ytPlaylistId).flatMap(res -> {
            if(null != res && !CollectionUtils.isEmpty(res.items()) &&  res.items().size() == 1) {
                return playListRepository.save(new Playlist(res.items().get(0))).then(Mono.just("OK"));
            }
            return Mono.error(new RuntimeException("Something went wrong"));
        }).onErrorReturn("KO");
        Mono<String> savePlaylistItems = youtubeService.retrievePlaylistElements(ytPlaylistId).collectList().flatMap(items ->{
                items.forEach(item -> playlistItemService.savePlaylistItems(ytPlaylistId,item).subscribe());
                return Mono.just("OK");}
        ).onErrorReturn("KO");
        return Flux.zip(savePlaylist,savePlaylistItems).map(tuple -> {
            return tuple.getT1().equalsIgnoreCase("OK") && tuple.getT2().equalsIgnoreCase("OK") ? "OK" : "KO";
        });
    }

    public Flux<ImportedPlaylistDTO> importedPlaylists(){
        return playListRepository.findAll().map(ImportedPlaylistDTO::new);
    }

    public Mono<Object> createPlaylistFromImportedPlaylist(String srcYtPlaylistId, String destYtPlaylistId){
        return playListRepository.findPlaylistByYtPlaylistId(srcYtPlaylistId)
                .flatMap(importedPlaylist ->youtubeService.retrievePlaylist(destYtPlaylistId))
                .flatMap(importedPlaylist ->
                        playlistItemService.getPlaylistDetail(srcYtPlaylistId).collectList().flatMap(items -> {
                            items.forEach(item -> youtubeService.insertPlaylistItem(destYtPlaylistId,item.id()).subscribe());
                            return Mono.just("OK");
           }));
    }
}
