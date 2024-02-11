package org.hyperskill.community.flashcards.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalModificationException extends RuntimeException {

    public IllegalModificationException() {
        super("The requested modification is illegal.");
    }
}
