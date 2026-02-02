package de.adressbuch.service;

import de.adressbuch.models.User;
import de.adressbuch.repository.interfaces.UserRepo;
import de.adressbuch.util.Utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserRepo userRepository;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository = mock(UserRepo.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void testCreateUser() {
        String username = "sampleUser";
        String displayedName = "THESampleUser";

        userService.addUser(username, displayedName);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetUserById() {
        String id = Utils.generateId();
        User user = new User(id, "sampleUser", Optional.of("THESampleUser"));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(id);

        assertEquals("sampleUser", result.get().username());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User(Utils.generateId(), "sampleUser", Optional.of("THESampleUser"));
        when(userRepository.findByUsername("sampleUser")).thenReturn(Optional.of(user));

        User result = userService.findUserByUsername("sampleUser").orElseThrow();

        assertEquals("THESampleUser", result.displayedName().get());
        verify(userRepository, times(1)).findByUsername("sampleUser");
    }

    @Test
    public void testDeleteUser() {
        String id = Utils.generateId();
        User user = new User(id, "sampleUser", Optional.of("THESampleUser"));
        when(userRepository.deleteById(id)).thenReturn(Optional.of(user));

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}
