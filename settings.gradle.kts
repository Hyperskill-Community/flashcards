rootProject.name = "flashcards"

include("server")
project(":server").name = "flashcards-server"

include("frontend")
project(":frontend").name = "flashcards-client"

include("auth-server")
