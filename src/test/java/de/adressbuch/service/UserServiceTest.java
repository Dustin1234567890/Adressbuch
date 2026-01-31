package de.adressbuch.service;

import de.adressbuch.models.User;
import de.adressbuch.repository.interfaces.UserRepo;
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
        User user = User.of(1L, "sampleUser", "THESampleUser");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);

        assertEquals("sampleUser", result.get().getUserName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetUserByUsername() {
        User user = User.of(1L, "sampleUser", "THESampleUser");
        when(userRepository.findByUsername("sampleUser")).thenReturn(Optional.of(user));

        User result = userService.findUserByUsername("sampleUser").orElseThrow();

        assertEquals("THESampleUser", result.getDisplayedName().get());
        verify(userRepository, times(1)).findByUsername("sampleUser");
    }

    @Test
    public void testDeleteUser() {
        User user = User.of(1L, "sampleUser", "THESampleUser");
        when(userRepository.deleteById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
