package de.adressbuch.repository;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.adressbuch.repository.interfaces.ContactGroupRepo;

public class SQLiteContactGroupRepo implements ContactGroupRepo {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteContactGroupRepo.class);
    private final String dbUrl;
    private static final String TABLE_NAME = "contact_group";

    public SQLiteContactGroupRepo(String dbUrl) {
        this.dbUrl = dbUrl;
        initializeDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }

    @Override
    public void initializeDatabase() {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.createTableIfNotExists(table(TABLE_NAME))
                .column(field("contact_id"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .column(field("group_id"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .constraint(primaryKey(field("contact_id"), field("group_id")))
                .execute();
            logger.debug("Contact-Group Tabelle init");
        } catch (SQLException e) {
            logger.error("Fehler beim Init der Contact-Group Tabelle", e);
            throw new RuntimeException("Fehler beim Init der Contact-Group Tabelle", e);
        }
    }

    @Override
    public void addContactToGroup(String contactId, String groupId) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.insertInto(table(TABLE_NAME))
                .columns(field("contact_id"), field("group_id"))
                .values(contactId, groupId)
                .execute();
            logger.debug("Kontakt {} zu Gruppe {} geaddet", contactId, groupId);
        } catch (SQLException e) {
            logger.error("Fehler beim Adden des Kontakts zu Gruppe: {} / {}", contactId, groupId, e);
            throw new RuntimeException("Fehler beim Adden des Kontakts zu Gruppe", e);
        }
    }

    @Override
    public void removeContactFromGroup(String contactId, String groupId) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.deleteFrom(table(TABLE_NAME))
                .where(field("contact_id").eq(contactId).and(field("group_id").eq(groupId)))
                .execute();
            logger.debug("Kontakt {} von Gruppe {} entfernt", contactId, groupId);
        } catch (SQLException e) {
            logger.error("Fehler beim Entfernen des Kontakts von Gruppe: {} / {}", contactId, groupId, e);
            throw new RuntimeException("Fehler beim Entfernen des Kontakts von Gruppe", e);
        }
    }

    @Override
    public List<String> findContactIdsByGroupId(String groupId) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<String> contactIds = create.select(field("contact_id"))
                .from(TABLE_NAME)
                .where(field("group_id").eq(groupId))
                .fetch(r -> r.get("contact_id", String.class));
            logger.debug("{} Kontakte in Gruppe {} gefunden", contactIds.size(), groupId);
            return contactIds;
        } catch (SQLException e) {
            logger.error("Fehler beim Finden der Kontakte in Gruppe: {}", groupId, e);
            throw new RuntimeException("Fehler beim Finden der Kontakte in Gruppe", e);
        }
    }

    @Override
    public boolean isContactInGroup(String contactId, String groupId) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var result = create.select()
                .from(TABLE_NAME)
                .where(field("contact_id").eq(contactId).and(field("group_id").eq(groupId)))
                .fetchOne();

            boolean foundContactCheck = result != null;
            logger.debug("Kontakt {} in Gruppe {} gesucht: {}", contactId, groupId, foundContactCheck);
            return foundContactCheck;
        } catch (SQLException e) {
            logger.error("Fehler beim Prüfen des Kontakts in Gruppe: {} / {}", contactId, groupId, e);
            throw new RuntimeException("Fehler beim Prüfen des Kontakts in Gruppe", e);
        }
    }
}
