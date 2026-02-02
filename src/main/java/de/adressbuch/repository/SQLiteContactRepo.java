package de.adressbuch.repository;

import de.adressbuch.models.Contact;
import de.adressbuch.util.Utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

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
                .column(field("id"), org.jooq.impl.SQLDataType.VARCHAR.identity(true))
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

            create.insertInto(table(TABLE_NAME))
                .columns(field("id"), field("name"), field("phone"), field("address"), field("email"))
                .values(contact.id(), contact.name(), contact.phoneNumber(), contact.address(), contact.email())
                .execute();

            return contact;
        } catch (SQLException e) {
            throw new RuntimeException("error saving contact", e);
        }
    }

    @Override
    public Contact update(Contact contact) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.update(table(TABLE_NAME))
                .set(field("name"), contact.name())
                .set(field("phoneNumber"), contact.phoneNumber())
                .set(field("address"), contact.address())
                .set(field("email"), contact.email())
                .where(field("id").eq(contact.id()))
                .execute();

            return contact;
        } catch (SQLException e) {
            throw new RuntimeException("error updating contact", e);
        }
    }

    @Override
    public Optional<Contact> deleteById(String id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Contact contact = new Contact(
                ret.get("id", String.class),
                ret.get("name", String.class),
                Utils.convertToOptionalNonBlank(ret.get("phone", String.class)),
                Utils.convertToOptionalNonBlank(ret.get("address", String.class)),
                Utils.convertToOptionalNonBlank(ret.get("email", String.class))
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
    public Optional<Contact> findById(String id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var ret = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (ret == null) {
                return Optional.empty();
            }

            Contact contact = new Contact(
                ret.get("id", String.class),
                ret.get("name", String.class),
                Utils.convertToOptionalNonBlank(ret.get("phone", String.class)),
                Utils.convertToOptionalNonBlank(ret.get("address", String.class)),
                Utils.convertToOptionalNonBlank(ret.get("email", String.class))
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
                .fetch(record -> new Contact(
                    record.get("id", String.class),
                    record.get("name", String.class),
                    Utils.convertToOptionalNonBlank(record.get("phone", String.class)),
                    Utils.convertToOptionalNonBlank(record.get("address", String.class)),
                    Utils.convertToOptionalNonBlank(record.get("email", String.class))
                ));
        } catch (SQLException e) {
            throw new RuntimeException("error finding list of all contacts", e);
        }
    }

    @Override
    public List<Contact> searchByName(String searchTerm) {
        String likePattern = "%" + searchTerm.toLowerCase() + "%";

        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            return create.select()
                    .from(table(TABLE_NAME))
                    .where(DSL.lower(field("name", String.class)).like(likePattern))
                    .fetch(record -> new Contact(
                            record.get("id", String.class),
                            record.get("name", String.class),
                            Utils.convertToOptionalNonBlank(record.get("phone", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("address", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("email", String.class))
                    ));
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Suchen von Kontakten mit Begriff: " + searchTerm, e);
        }
    }
}

