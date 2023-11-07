package it.lunacia.yt.repository;

import it.lunacia.yt.entity.Playlist;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PlaylistRepository extends ReactiveMongoRepository<Playlist,String> {

    Mono<Playlist> findPlaylistByYtPlaylistId(final String ytPlaylistId);
}
