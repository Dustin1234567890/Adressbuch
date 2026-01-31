package de.adressbuch.repository;

import de.adressbuch.models.User;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;

import de.adressbuch.repository.interfaces.UserRepo;

public class SQLiteUserRepo implements UserRepo {
    private final String dbUrl;
    private static final String TABLE_NAME = "users";

    public SQLiteUserRepo(String dbUrl) {
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
                .column(field("username"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .column(field("displayedName"), org.jooq.impl.SQLDataType.VARCHAR)
                .constraint(primaryKey(field("id")))
                .execute();
        } catch (SQLException e) {
            throw new RuntimeException("error db init", e);
        }
    }

    @Override
    public User save(User user) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            Long id = create.insertInto(table(TABLE_NAME))
                .columns(field("username"), field("displayedName"))
                .values(user.getUserName(), user.getDisplayedName().orElse(null))
                .returning(field("id"))
                .fetchOne()
                .get(field("id"), Long.class);

            return user.withId(id);
        } catch (SQLException e) {
            throw new RuntimeException("error saving user", e);
        }
    }

    @Override
    public User update(User user) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.update(table(TABLE_NAME))
                .set(field("username"), user.getUserName())
                .set(field("displayedName"), user.getDisplayedName().orElse(null))
                .where(field("id").eq(user.getId().orElse(null)))
                .execute();

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("error updating user", e);
        }
    }

    @Override
    public Optional<User> deleteById(Long id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            User user = User.of(
                ret.get("id", Long.class),
                ret.get("username", String.class),
                ret.get("displayedName", String.class)
            );

            create.deleteFrom(table(TABLE_NAME))
                .where(field("id").eq(id))
                .execute();

            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException("error deleting user by id", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            User user = User.of(
                ret.get("id", Long.class),
                ret.get("username", String.class),
                ret.get("displayedName", String.class)
            );

            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException("error finding user by id", e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("username").eq(username))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            User user = User.of(
                ret.get("id", Long.class),
                ret.get("username", String.class),
                ret.get("displayedName", String.class)
            );

            return Optional.of(user);
        } catch (SQLException e) {
            throw new RuntimeException("error finding user by username", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            return create.select()
                .from(TABLE_NAME)
                .fetch(record -> User.of(
                    record.get("id", Long.class),
                    record.get("username", String.class),
                    record.get("displayedName", String.class)
                ));
        } catch (SQLException e) {
            throw new RuntimeException("error finding list of all users", e);
        }
    }
}


