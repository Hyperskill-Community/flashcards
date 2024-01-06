package org.hyperskill.community.flashcards;

import org.springframework.boot.SpringApplication;

public class TestFlashcardsApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(FlashcardsApplication::main)
                .with(TestMongoConfiguration.class)
                .run(args);
    }
}
