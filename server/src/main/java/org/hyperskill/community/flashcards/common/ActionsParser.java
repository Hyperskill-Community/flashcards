package org.hyperskill.community.flashcards.common;

import org.hyperskill.community.flashcards.common.response.ActionType;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.util.HashSet;
import java.util.Set;

public class ActionsParser {
    private static String currentUri = "";

    private ActionsParser() {
    }

    public static void setUri(String uri) {
        currentUri = uri;
    }

    public static Set<PermittedAction> fromPermissions(String permissions) {
        Set<PermittedAction> actions = new HashSet<>(3);
        if (permissions.contains("r")) {
            actions.add(new PermittedAction(ActionType.READ, currentUri));
        }
        if (permissions.contains("w")) {
            actions.add(new PermittedAction(ActionType.WRITE, currentUri));
        }
        if (permissions.contains("d")) {
            actions.add(new PermittedAction(ActionType.DELETE, currentUri));
        }
        return actions;
    }
}
