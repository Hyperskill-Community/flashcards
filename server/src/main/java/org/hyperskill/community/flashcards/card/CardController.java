package org.hyperskill.community.flashcards.card;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hyperskill.community.flashcards.card.mapper.CardMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final CardMapper mapper;

    @GetMapping
    public CardPageResponse getCards(
            @RequestParam(name = "categoryId") String categoryId,
            @Valid @Min(0) @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @AuthenticationPrincipal Jwt jwt) {
        var username = jwt.getSubject();
        var cardsPage = cardService.getCardsByCategory(username, categoryId, page);
        return new CardPageResponse(
                cardsPage.isFirst(),
                cardsPage.isLast(),
                cardsPage.getNumber(),
                cardsPage.getTotalPages(),
                cardsPage.getContent().stream().map(mapper::map).toList()
        );
    }
}
