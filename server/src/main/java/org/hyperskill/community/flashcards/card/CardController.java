package org.hyperskill.community.flashcards.card;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    public CardPageResponse getCards(
            @RequestParam(name = "categoryId") String categoryId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        var cardsPage = cardService.getCardsByCategory(categoryId, page);
        return new CardPageResponse(
                cardsPage.isFirst(),
                cardsPage.isLast(),
                cardsPage.getNumber(),
                cardsPage.getTotalPages(),
                cardsPage.getContent()
        );
    }
}
