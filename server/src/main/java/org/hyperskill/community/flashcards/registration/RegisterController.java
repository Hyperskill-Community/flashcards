package org.hyperskill.community.flashcards.registration;

import static org.springframework.http.ResponseEntity.ok;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService service;
    private final UserMapper mapper;

    /**
     * register endpoint - unauthenticated (!).
     * 
     * @param userDto dto containing provided user email (=username) and raw password
     * @return empty response 200(OK) on successful register, 400(BadRequest) if dto validation fails or
     *         user exists
     */
    @PostMapping
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDto userDto) {
        var userDocument = mapper.toDocument(userDto);
        service.registerUser(userDocument);
        log.info("User {} successfully registered", userDocument.getUsername());
        return ok().build();
    }
}
