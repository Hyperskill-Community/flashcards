package org.hyperskill.community.flashcards.dataio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperskill.community.flashcards.common.AuthenticationResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService service;
    private final AuthenticationResolver authenticationResolver;


    /**
     * export endpoint, that returns complete flashcards data, that the authenticated user has access to.
     * @return export data record for the authenticated user.
     */
    @GetMapping
    public ResponseEntity<UserExport> export() {
        var username = authenticationResolver.resolveUsername();
        return ok(service.retrieveUserData(username));
    }
}
