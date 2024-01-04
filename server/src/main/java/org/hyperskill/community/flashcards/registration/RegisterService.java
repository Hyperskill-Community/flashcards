package org.hyperskill.community.flashcards.registration;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final MongoTemplate mongoTemplate;

    /**
     * method receives and saves the User entity with data mapped from the UserDto (name and encrypted password),
     * 
     * @param user the prepared User entity to save to the database.
     * @throws UserAlreadyExistsException if user already exists.
     */
    public void registerUser(User user) throws UserAlreadyExistsException {
        if (Objects.nonNull(mongoTemplate.findById(user.getUsername(), User.class))) {
            throw new UserAlreadyExistsException();
        }
        mongoTemplate.save(user);
    }
}
