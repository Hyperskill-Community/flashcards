package org.hyperskill.community.flashcards.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Without this, changes in URL will be detected as calls to Spring resources, which do not exist.
 * This controller redirects this call back to Vue.Js Router (SPA at "/").
 */
@Controller
@Slf4j
public class RedirectController {

    @SuppressWarnings("java:S6856")
    @GetMapping(value = {"/{path:[^.]*}", "/*/{path:[^.]*}", "/*/*/{path:[^.]*}"})
    public String forwardCall(HttpServletRequest request) {
        log.trace("Forwarding call of '{}' to Vue.Js", request.getRequestURI());
        return "forward:/";
    }

}
