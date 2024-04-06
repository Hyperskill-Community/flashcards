package org.hyperskill.community.flashcards.common.response;

public enum ActionType {
    READ,
    WRITE,
    DELETE;

    public static ActionType fromChar(char c) {
        return switch (c) {
            case 'r' -> READ;
            case 'w' -> WRITE;
            case 'd' -> DELETE;
            default -> throw new IllegalArgumentException("Unknown action type: " + c);
        };
    }
}
