package de.adressbuch.repository;

import de.adressbuch.models.Group;
import de.adressbuch.util.Utils;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;

import de.adressbuch.repository.interfaces.GroupRepo;

public class SQLiteGroupRepo implements GroupRepo {
    private final String dbUrl;
    private static final String TABLE_NAME = "groups";

    public SQLiteGroupRepo(String dbUrl) {
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
                .column(field("id"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .column(field("name"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .column(field("description"), org.jooq.impl.SQLDataType.VARCHAR)
                .constraint(primaryKey(field("id")))
                .execute();
        } catch (SQLException e) {
            throw new RuntimeException("error db init", e);
        }
    }

    @Override
    public Group save(Group group) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.insertInto(table(TABLE_NAME))
                .columns(field("id"), field("name"), field("description"))
                .values(group.id(), group.name(), group.description().orElse(null))
                .execute();

            return group;
        } catch (SQLException e) {
            throw new RuntimeException("error saving group", e);
        }
    }

    @Override
    public Group update(Group group) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.update(table(TABLE_NAME))
                .set(field("name"), group.name())
                .set(field("description"), group.description().orElse(null))
                .where(field("id").eq(group.id()))
                .execute();

            return group;
        } catch (SQLException e) {
            throw new RuntimeException("error updating group", e);
        }
    }

    @Override
    public Optional<Group> deleteById(String id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Group group = new Group(
                ret.get("id", String.class),
                ret.get("name", String.class),
                Utils.convertToOptionalNonBlank(ret.get("description", String.class))
            );

            create.deleteFrom(table(TABLE_NAME))
                .where(field("id").eq(id))
                .execute();

            return Optional.of(group);
        } catch (SQLException e) {
            throw new RuntimeException("error deleting group by id", e);
        }
    }

    @Override
    public Optional<Group> findById(String id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Group group = new Group(
                ret.get("id", String.class),
                ret.get("name", String.class),
                Utils.convertToOptionalNonBlank(ret.get("description", String.class))
            );

            return Optional.of(group);
        } catch (SQLException e) {
            throw new RuntimeException("error finding group by id", e);
        }
    }

    @Override
    public Optional<List<Group>> findByName(String name) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<Group> groups = create.select()
                .from(table(TABLE_NAME))
                .where(field("name", String.class).eq(name))
                .fetch(record -> new Group(
                    record.get("id", String.class),
                    record.get("name", String.class),
                    Utils.convertToOptionalNonBlank(record.get("description", String.class))
                ));

            if (groups.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(groups);
        } catch (SQLException e) {
            throw new RuntimeException("error finding group by name", e);
        }
    }

    @Override
    public List<Group> findAll() {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            return create.select()
                .from(TABLE_NAME)
                .fetch(record -> new Group(
                    record.get("id", String.class),
                    record.get("name", String.class),
                    Utils.convertToOptionalNonBlank(record.get("description", String.class))
                ));
        } catch (SQLException e) {
            throw new RuntimeException("error finding list of all groups", e);
        }
    }
}


