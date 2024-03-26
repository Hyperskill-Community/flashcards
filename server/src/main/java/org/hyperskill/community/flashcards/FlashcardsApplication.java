package org.hyperskill.community.flashcards;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.ZoneId;

@SpringBootApplication
@Slf4j
public class FlashcardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashcardsApplication.class, args);
    }

   @Bean
   @Profile("!test")
   public CommandLineRunner logBuildInfo(BuildProperties buildProperties, GitProperties gitProperties) {
         return args -> {
             log.info("Build-Info: Group: {} Artifact: {} Version: {}, Buildtime: {}", buildProperties.getGroup(), buildProperties.getArtifact(),
                     buildProperties.getVersion(), buildProperties.getTime().atZone(ZoneId.systemDefault()));
             log.info("Git-Info: Branch: {} Commit: {}, From: {}", gitProperties.getBranch(), gitProperties.getShortCommitId(),
                     gitProperties.getCommitTime().atZone(ZoneId.systemDefault()));
         };
   }

}
