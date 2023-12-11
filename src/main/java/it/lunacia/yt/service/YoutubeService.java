package it.lunacia.yt.service;

import it.lunacia.yt.constants.YoutubePlaylistPrivacyEnum;
import it.lunacia.yt.playlist.items.dto.PlaylistItemDTO;
import it.lunacia.yt.playlist.items.dto.PlaylistItemsDTO;
import it.lunacia.yt.playlist.items.dto.SavePlaylistItemDTO;
import it.lunacia.yt.playlist.items.dto.SavePlaylistItemSnippetDTO;
import it.lunacia.yt.playlists.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class YoutubeService extends BaseService{

    @Value("${base.yt.api.url}")
    private String baseUrl;

    @Value("${google.youtube.apikey}")
    private String apikey;

    @Override
    public String getBaseUrl(){
        return baseUrl;
    }
    public Mono<Object> retrieveMyChannels(){
        HttpHeaders params = new HttpHeaders();
        params.add("part","snippet,contentDetails");
        params.add("mine","true");
        params.add("key",apikey);
        return performCall(HttpMethod.GET,"/channels", Optional.of(params),Object.class);
    }

    public  Mono<PlaylistsListDTO> retrieveMyPlaylists(Optional<String> nextPageToken){
        HttpHeaders params = new HttpHeaders();
        params.add("part","snippet");
        params.add("mine","true");
        params.add("key",apikey);
        nextPageToken.ifPresent(s -> params.add("pageToken", s));
        return performCall(HttpMethod.GET,"/playlists",Optional.of(params), PlaylistsListDTO.class).switchIfEmpty(Mono.empty());
    }
    public  Mono<PlaylistsListDTO> retrievePlaylist(final String ytPlaylistId){
        HttpHeaders params = new HttpHeaders();
        params.add("part","snippet");
        params.add("id",ytPlaylistId);
        params.add("key",apikey);
        return performCall(HttpMethod.GET,"/playlists",Optional.of(params), PlaylistsListDTO.class);
    }
    private Mono<PlaylistItemsDTO> retrievePlaylistElements(final String playlistId, Optional<String> paginationToken){
        HttpHeaders params = new HttpHeaders();
        params.add("part","snippet");
        params.add("playlistId",playlistId);
        params.add("key",apikey);
        return webClient.get().uri(UriComponentsBuilder.fromUriString(getBaseUrl()+"/playlistItems").queryParams(params).queryParamIfPresent("pageToken",paginationToken).toUriString())
                .attributes(
                        ServerOAuth2AuthorizedClientExchangeFilterFunction
                                .clientRegistrationId("google")).retrieve()
                .bodyToMono(PlaylistItemsDTO.class);

    }

    public Flux<PlaylistItemDTO> retrievePlaylistElements(final String playlistId){
        return retrievePlaylistElements(playlistId,Optional.empty()).expand(results -> {
            if(results.nextPageToken() == null){
                return Mono.empty();
            }
            return retrievePlaylistElements(playlistId,Optional.of(results.nextPageToken()));
        }).flatMap(response -> Flux.fromIterable(response.items()));
    }

    public Mono<Object> createPlaylist(final String playlistName, final String description, final YoutubePlaylistPrivacyEnum privacyStatus){
        HttpHeaders params = new HttpHeaders();
        params.add("part","snippet,status,localizations");
        params.add("key",apikey);
        Map<String, SavePlaylistLocalizationDTO> localizations = new HashMap<>();
        localizations.put("it",new SavePlaylistLocalizationDTO(playlistName,description));
        SavePlaylistDTO request = new SavePlaylistDTO(new SavePlaylistSnippetDTO(playlistName,description,"it"),new SavePlaylistStatusDTO(privacyStatus),localizations);
        return performCall(HttpMethod.POST,"/playlists",Optional.of(params),Optional.of(request),Object.class);
    }

    public Mono<Object> insertPlaylistItem(final String ytPlaylistId, final String ytResourceId){
        HttpHeaders params = new HttpHeaders();
        params.add("part","snippet");
        params.add("key",apikey);
        SavePlaylistItemDTO request = new SavePlaylistItemDTO(new SavePlaylistItemSnippetDTO(ytPlaylistId,ytResourceId));
        return performCall(HttpMethod.POST,"/playlistItems",Optional.of(params),Optional.of(request),Object.class);
    }
}
