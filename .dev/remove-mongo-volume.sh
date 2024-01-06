echo "Really delete all flashcards data - including user data ? [y/n]" && read choice
[[ $choice = "y" ]] || exit;

echo "Deleting container and volume ..."
docker ps -a --format '{{.Names}}' | grep '^flashcards' | xargs docker rm -f
docker volume ls -q --filter name=flashcards | xargs docker volume rm
