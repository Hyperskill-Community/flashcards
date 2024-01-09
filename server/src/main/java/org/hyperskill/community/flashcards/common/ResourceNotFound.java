package org.hyperskill.community.flashcards.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound() {
        super("Requested resource not found");
    }
}
