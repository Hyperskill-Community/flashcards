package org.hyperskill.community.flashcards;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;

public abstract class TestUtils {

    public static final String TEST1 = "test1@test.com";
    public static final String TEST2 = "test2@test.com";

    public static RequestPostProcessor oidc(String sub) {
        return oidcLogin().oidcUser(oidcUser(sub));
    }

    public static OidcUser oidcUser(String sub) {
        return new DefaultOidcUser(
                        List.of(),
                        new OidcIdToken("token",
                                Instant.now(),
                                Instant.now().plusSeconds(60),
                                Map.of("sub", sub)),
                        new OidcUserInfo(Map.of("sub", sub))
                );
    }

    public static Jwt jwtUser(String sub) {
        return new Jwt("token",
                Instant.now(),
                Instant.now().plusSeconds(60),
                Map.of("sub", sub),
                Map.of("sub", sub));
    }

    @NotNull
    public static List<ILoggingEvent> startLogAppender(Class<?> loggerClass) {
        Logger logger = (Logger) LoggerFactory.getLogger(loggerClass);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        return listAppender.list;
    }

}
