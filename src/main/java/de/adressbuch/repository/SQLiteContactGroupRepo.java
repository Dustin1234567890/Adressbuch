package de.adressbuch.repository;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;

import de.adressbuch.repository.interfaces.ContactGroupRepo;

public class SQLiteContactGroupRepo implements ContactGroupRepo {
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
        } catch (SQLException e) {
            throw new RuntimeException("error db init", e);
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
        } catch (SQLException e) {
            throw new RuntimeException("error adding contact to group", e);
        }
    }

    @Override
    public void removeContactFromGroup(String contactId, String groupId) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.deleteFrom(table(TABLE_NAME))
                .where(field("contact_id").eq(contactId).and(field("group_id").eq(groupId)))
                .execute();
        } catch (SQLException e) {
            throw new RuntimeException("error removing contact from group", e);
        }
    }

    @Override
    public List<String> findContactIdsByGroupId(String groupId) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            return create.select(field("contact_id"))
                .from(TABLE_NAME)
                .where(field("group_id").eq(groupId))
                .fetch(r -> r.get("contact_id", String.class));
        } catch (SQLException e) {
            throw new RuntimeException("error finding contactsbygroup", e);
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

            return result != null;
        } catch (SQLException e) {
            throw new RuntimeException("error checking contact in group", e);
        }
    }
}
