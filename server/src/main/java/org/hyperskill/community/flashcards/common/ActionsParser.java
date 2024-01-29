package org.hyperskill.community.flashcards.common;

import org.hyperskill.community.flashcards.common.response.ActionType;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.util.HashSet;
import java.util.Set;

public class ActionsParser {

    private ActionsParser() {
    }

    public static Set<PermittedAction> fromPermissions(String permissions, String uri) {
        Set<PermittedAction> actions = HashSet.newHashSet(3);
        if (permissions.contains("r")) {
            actions.add(new PermittedAction(ActionType.READ, uri));
        }
        if (permissions.contains("w")) {
            actions.add(new PermittedAction(ActionType.WRITE, uri));
        }
        if (permissions.contains("d")) {
            actions.add(new PermittedAction(ActionType.DELETE, uri));
        }
        return actions;
    }
}
