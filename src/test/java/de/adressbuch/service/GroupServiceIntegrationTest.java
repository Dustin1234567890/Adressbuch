package de.adressbuch.service;

import de.adressbuch.models.Group;
import de.adressbuch.repository.SQLiteGroupRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class GroupServiceIntegrationTest {

    @TempDir
    Path tempDir;
    
    private GroupService groupService;
    private SQLiteGroupRepo groupRepository;
    private String dbUrl;

    @BeforeEach
    public void setup() throws Exception {
        dbUrl = "jdbc:sqlite:" + tempDir.resolve("test.db").toString();
        groupRepository = new SQLiteGroupRepo(dbUrl);
        groupService = new GroupService(groupRepository);
        
        groupRepository.initializeDatabase();
    }

    @Nested
    @DisplayName("crud")
    class CrudTests {
        
        @Test
        @DisplayName("full cycle")
        public void shouldPerformFullCrudCycle() throws Exception {

            assertEquals(0, groupService.findAllGroups().size());
            

            groupService.addGroup("sample friends", "enge freunde");
            

            assertEquals(1, groupService.findAllGroups().size());
            String groupId = groupService.findAllGroups().get(0).id();
            

            assertTrue(groupService.findGroupById(groupId).isPresent());
            assertEquals("sample friends", groupService.findGroupById(groupId).get().name());
            assertEquals("enge freunde", groupService.findGroupById(groupId).get().description().orElse(null));
            

            groupService.updateGroup(groupId, "Gute Freunde", "Sehr gute Freunde");
            

            assertTrue(groupService.findGroupById(groupId).isPresent());
            assertEquals("Gute Freunde", groupService.findGroupById(groupId).get().name());
            assertEquals("Sehr gute Freunde", groupService.findGroupById(groupId).get().description().orElse(null));
            

            groupService.deleteGroup(groupId);
            

            List<Group> remainingGroups = groupService.findAllGroups();
            assertTrue(remainingGroups.isEmpty());
        }

        @Test
        @DisplayName("multiple")
        public void shouldHandleMultipleGroups() throws Exception {

            groupService.addGroup("sample friends", "close friends");
            groupService.addGroup("sample family", "family members");
            groupService.addGroup("sample work", "work colleagues");
            

            assertEquals(3, groupService.findAllGroups().size());
            
            List<Group> allGroups = groupService.findAllGroups();
            String arbeitsGruppeId = allGroups.stream()
                .filter(g -> g.name().equals("sample work"))
                .map(Group::id)
                .findFirst()
                .orElseThrow();
            groupService.deleteGroup(arbeitsGruppeId);
            

            assertEquals(2, groupService.findAllGroups().size());
        }
    }

    @Nested
    @DisplayName("search")
    class SearchAndFilterTests {
        
        @Test
        @DisplayName("byName")
        public void shouldSearchByName() throws Exception {

            groupService.addGroup("sample uds", "friends an UdS");
            groupService.addGroup("sample htw", "friends an htw");
            groupService.addGroup("sample family", "family members");
            

            Optional<List<Group>> results = groupService.findGroupByName("sample");
            

            assertTrue(results.isPresent());
            assertEquals(3, results.get().size());
            assertTrue(results.get().stream().anyMatch(g -> g.name().contains("uds")));
            assertTrue(results.get().stream().anyMatch(g -> g.name().contains("htw")));
        }

    }

    @Nested
    @DisplayName("errors")
    class ErrorHandlingTests {
        
        @Test
        @DisplayName("delete notFound")
        public void shouldThrowExceptionWhenDeletingNonExistent() throws Exception {

            assertThrows(IllegalArgumentException.class, () -> 
                groupService.deleteGroup("non-existent-id"));
        }

        @Test
        @DisplayName("validation empty")
        public void shouldThrowExceptionOnEmptyName() throws Exception {

            assertThrows(IllegalArgumentException.class, () -> 
                groupService.addGroup("", null));
        }

        }
}
