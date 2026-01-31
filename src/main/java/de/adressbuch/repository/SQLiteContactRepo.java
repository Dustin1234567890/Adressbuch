package de.adressbuch.repository;

import de.adressbuch.models.Contact;
import java.util.List;
import java.util.Optional;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;

public class SQLiteContactRepo implements de.adressbuch.repository.interfaces.ContactRepo {
    private final String dbUrl;
    private static final String TABLE_NAME = "contacts";

    public SQLiteContactRepo(String dbUrl) {
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
                .column(field("phone"), org.jooq.impl.SQLDataType.VARCHAR)
                .column(field("address"), org.jooq.impl.SQLDataType.VARCHAR)
                .column(field("email"), org.jooq.impl.SQLDataType.VARCHAR)
                .constraint(primaryKey(field("id")))
                .execute();
                
        } catch (SQLException e) {
            throw new RuntimeException("error db init", e);
        }
    }

    @Override
    public Contact save(Contact contact) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            Long id = create.insertInto(table(TABLE_NAME))
                .columns(field("name"), field("phone"), field("address"), field("email"))
                .values(contact.getName(), contact.getPhoneNumber(), contact.getAddress(), contact.getEmail())
                .returning(field("id"))
                .fetchOne()
                .get(field("id"), Long.class);

            return contact.withId(id);
        } catch (SQLException e) {
            throw new RuntimeException("error saving contact", e);
        }
    }

    @Override
    public Contact update(Contact contact) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.update(table(TABLE_NAME))
                .set(field("name"), contact.getName())
                .set(field("phoneNumber"), contact.getPhoneNumber())
                .set(field("address"), contact.getAddress())
                .set(field("email"), contact.getEmail())
                .where(field("id").eq(contact.getId()))
                .execute();

            return contact;
        } catch (SQLException e) {
            throw new RuntimeException("error updating contact", e);
        }
    }

    @Override
    public Optional<Contact> deleteById(Long id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Contact contact = Contact.of(
                ret.get("id", Long.class),
                ret.get("name", String.class),
                Optional.ofNullable(ret.get("phone", String.class)),
                Optional.ofNullable(ret.get("address", String.class)),
                Optional.ofNullable(ret.get("email", String.class))
            );

            create.deleteFrom(table(TABLE_NAME))
                .where(field("id").eq(id))
                .execute();

            return Optional.of(contact);
        } catch (SQLException e) {
            throw new RuntimeException("error deleting contact by id", e);
        }
    }

    @Override
    public Optional<Contact> findById(Long id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Contact contact = Contact.of(
                ret.get("id", Long.class),
                ret.get("name", String.class),
                Optional.ofNullable(ret.get("phone", String.class)),
                Optional.ofNullable(ret.get("address", String.class)),
                Optional.ofNullable(ret.get("email", String.class))
            );

            return Optional.of(contact);
        } catch (SQLException e) {
            throw new RuntimeException("error finding contact byid", e);
        }
    }

    @Override
    public List<Contact> findAll() {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            return create.select()
                .from(table(TABLE_NAME))
                .fetch(record -> Contact.of(
                    record.get("id", Long.class),
                    record.get("name", String.class),
                    Optional.ofNullable(record.get("phone", String.class)),
                    Optional.ofNullable(record.get("address", String.class)),
                    Optional.ofNullable(record.get("email", String.class))
                ));
        } catch (SQLException e) {
            throw new RuntimeException("error finding list of all contacts", e);
        }
    }
}

