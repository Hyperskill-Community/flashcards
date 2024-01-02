# Hyperskill Community Project: Flashcards

Project idea came up in the Hyperskill community team and will be realized by this team.

## Technology / External Libraries

- Java 21
- Spring Boot 3.2.1
- Support for Native image on GraalVM
- Mongo DB via docker-compose
- Vue 3 SPA-Frontend using component framework Vuetify 3
- Packaging with Vite
- Lombok
- Testcontainers
- Gradle 8.5

## Program description

The application represents a digital flashcard app, that allows to create, store and retrieve flashcards via REST-endpoints.

Flash cards are stored in collections of a mongo database. A collection is a set of flashcards with a name and a description.

## Project status

Ongoing

## Repository Contents

Sources for backend and frontend gradle modules with tests and configurations (to come up).

## Progress

01.01.24 Project started. Just setup of build and repo with gradle.

02.01.24 Backend now with User management into Collection in cards databas of MongoDB. Change frontend build to download
node 21.5 into local .gradle folder - to avoid relying on node installation on executable location.
