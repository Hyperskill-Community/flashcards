package org.hyperskill.community.flashcards.registration;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class RegisterServiceTest {

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    RegisterService service;

    @Test
    void whenUserExist_RegisterUserThrows() {
        when(mongoTemplate.findById("test", User.class)).thenReturn(new User());
        var newUser = new User().setUsername("test");
        assertThrows(UserAlreadyExistsException.class, () -> service.registerUser(newUser));
        verify(mongoTemplate, never()).save(any(User.class));
    }

    @Test
    void whenRegisterUser_UserSaved() {
        when(mongoTemplate.findById("test", User.class)).thenReturn(null);
        var newUser = new User().setUsername("test");
        service.registerUser(newUser);
        verify(mongoTemplate).save(newUser);
    }

}
