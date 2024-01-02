package org.hyperskill.community.flashcards;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class FlashcardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashcardsApplication.class, args);
    }

}
