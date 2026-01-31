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
                .column(field("id"), org.jooq.impl.SQLDataType.BIGINT.identity(true))
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

            Long id = create.insertInto(table(TABLE_NAME))
                .columns(field("name"), field("description"))
                .values(group.getName(), group.getDescription().orElse(null))
                .returning(field("id"))
                .fetchOne()
                .get(field("id"), Long.class);

            return group.withId(id);
        } catch (SQLException e) {
            throw new RuntimeException("error saving group", e);
        }
    }

    @Override
    public Group update(Group group) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.update(table(TABLE_NAME))
                .set(field("name"), group.getName())
                .set(field("description"), group.getDescription().orElse(null))
                .where(field("id").eq(group.getId().orElse(null)))
                .execute();

            return group;
        } catch (SQLException e) {
            throw new RuntimeException("error updating group", e);
        }
    }

    @Override
    public Optional<Group> deleteById(Long id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Group group = Group.of(
                ret.get("id", Long.class),
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
    public Optional<Group> findById(Long id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Group group = Group.of(
                ret.get("id", Long.class),
                ret.get("name", String.class),
                Utils.convertToOptionalNonBlank(ret.get("description", String.class))
            );

            return Optional.of(group);
        } catch (SQLException e) {
            throw new RuntimeException("error finding group by id", e);
        }
    }

    @Override
    public Optional<Group> findByName(String name) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("name").eq(name))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Group group = Group.of(
                ret.get("id", Long.class),
                ret.get("name", String.class),
                Utils.convertToOptionalNonBlank(ret.get("description", String.class))
            );

            return Optional.of(group);
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
                .fetch(record -> Group.of(
                    record.get("id", Long.class),
                    record.get("name", String.class),
                    Utils.convertToOptionalNonBlank(record.get("description", String.class))
                ));
        } catch (SQLException e) {
            throw new RuntimeException("error finding list of all groups", e);
        }
    }
}


