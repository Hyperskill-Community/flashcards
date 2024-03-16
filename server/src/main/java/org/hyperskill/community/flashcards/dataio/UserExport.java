package org.hyperskill.community.flashcards.dataio;

import java.util.List;

public record UserExport(String username, List<CategoryExport> categories) {
}
