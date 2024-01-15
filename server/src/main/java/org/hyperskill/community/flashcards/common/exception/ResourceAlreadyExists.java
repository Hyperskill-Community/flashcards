package org.hyperskill.community.flashcards.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExists extends RuntimeException {

    public ResourceAlreadyExists() {
        super("Such resource already exists");
    }
}
