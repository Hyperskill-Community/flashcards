package org.hyperskill.community.flashcards.common;

import org.hyperskill.community.flashcards.common.response.ActionType;
import org.hyperskill.community.flashcards.common.response.PermittedAction;

import java.util.HashSet;
import java.util.Set;

public class ActionsParser {

    private ActionsParser() {
        // no instantiation
    }

    public static Set<PermittedAction> fromPermissions(String permissions, String uri) {
        Set<PermittedAction> actions = HashSet.newHashSet(3);
        permissions.chars()
                .forEach(c -> actions.add(new PermittedAction(ActionType.fromChar((char) c), uri)));
        return actions;
    }
}
