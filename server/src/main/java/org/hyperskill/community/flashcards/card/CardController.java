package org.hyperskill.community.flashcards.card;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hyperskill.community.flashcards.card.mapper.CardMapper;
import org.hyperskill.community.flashcards.card.request.CardCreateRequest;
import org.hyperskill.community.flashcards.card.response.CardResponse;
import org.hyperskill.community.flashcards.common.AuthenticationResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(path = "/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final AuthenticationResolver authenticationResolver;
    private final CardMapper mapper;

    @GetMapping
    public CardPageResponse getCards(
            @RequestParam(name = "categoryId") String categoryId,
            @Valid @Min(0) @RequestParam(name = "page", required = false, defaultValue = "0") int page) {

        var username = authenticationResolver.resolveUsername();
        var cardsPage = cardService.getCardsByCategory(username, categoryId, page);
        return new CardPageResponse(
                cardsPage.isFirst(),
                cardsPage.isLast(),
                cardsPage.getNumber(),
                cardsPage.getTotalPages(),
                cardsPage.getContent().stream().map(mapper::map).toList()
        );
    }

    @GetMapping("/count")
    public long getCardsCount(@RequestParam(name = "categoryId") String categoryId) {
        var username = authenticationResolver.resolveUsername();
        return cardService.getCardCountOfCategory(username, categoryId);
    }

    @GetMapping("/{cardId}")
    public CardResponse getCardById(@PathVariable String cardId, @RequestParam String categoryId) {
        var username = authenticationResolver.resolveUsername();
        var card = cardService.getCardById(username, cardId, categoryId);
        return mapper.map(card);
    }

    @DeleteMapping("/{cardId}")
    public void deleteCardById(@PathVariable String cardId, @RequestParam String categoryId) {
        var username = authenticationResolver.resolveUsername();
        cardService.deleteCardById(username, cardId, categoryId);
    }

    @PostMapping
    public ResponseEntity<Void> createCard(
            @RequestParam String categoryId,
            @Valid @RequestBody CardCreateRequest request
    ) {
        var username = authenticationResolver.resolveUsername();
        var id = cardService.createCard(username, request, categoryId);
        var uri = URI.create("/api/cards/" + id);
        return ResponseEntity.created(uri).build();
    }
}
