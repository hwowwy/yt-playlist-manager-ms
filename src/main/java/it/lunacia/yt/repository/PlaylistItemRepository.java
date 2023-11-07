package it.lunacia.yt.repository;

import it.lunacia.yt.entity.PlaylistItem;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PlaylistItemRepository extends ReactiveMongoRepository<PlaylistItem,String> {

    Flux<PlaylistItem> findPlaylistItemByYtPlaylistId(final String ytPlaylistId);
}
