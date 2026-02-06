package de.adressbuch.repository;

import de.adressbuch.exception.RepositoryException;
import de.adressbuch.models.Contact;
import de.adressbuch.util.Utils;

import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

import static org.jooq.impl.DSL.*;
import org.jooq.SQLDialect;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLiteContactRepo implements de.adressbuch.repository.interfaces.ContactRepo {
    private static final Logger logger = LoggerFactory.getLogger(SQLiteContactRepo.class);
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
                .column(field("id"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .column(field("name"), org.jooq.impl.SQLDataType.VARCHAR.nullable(false))
                .column(field("phone"), org.jooq.impl.SQLDataType.VARCHAR)
                .column(field("address"), org.jooq.impl.SQLDataType.VARCHAR)
                .column(field("email"), org.jooq.impl.SQLDataType.VARCHAR)
                .constraint(primaryKey(field("id")))
                .execute();
            
            logger.debug("Database wurde init");
        } catch (SQLException e) {
            logger.error("Fehler beim init der Database", e);
            throw new RepositoryException("Failed to initialize database", e);
        }
    }

    @Override
    public Contact save(Contact contact) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.insertInto(table(TABLE_NAME))
                .columns(field("id"), field("name"), field("phone"), field("address"), field("email"))
                .values(contact.id(), contact.name(), contact.phoneNumber().orElse(null), contact.address().orElse(null), contact.email().orElse(null))
                .execute();

            logger.debug("Kontakt gesaved: {}", contact.id());
            return contact;
        } catch (SQLException e) {
            logger.error("Fehler beim Saven des Kontakts: {}", contact.id(), e);
            throw new RepositoryException("Failed to save contact", e);
        }
    }

    @Override
    public Contact update(Contact contact) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            create.update(table(TABLE_NAME))
                .set(field("name"), contact.name())
                .set(field("phone"), contact.phoneNumber().orElse(null))
                .set(field("address"), contact.address().orElse(null))
                .set(field("email"), contact.email().orElse(null))
                .where(field("id").eq(contact.id()))
                .execute();

            logger.debug("Kontakt geupdatet: {}", contact.id());
            return contact;
        } catch (SQLException e) {
            logger.error("Fehler beim Updaten des Kontakts: {}", contact.id(), e);
            throw new RepositoryException("Failed to update contact", e);
        }
    }

    @Override
    public Optional<Contact> deleteById(String id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var record = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            if (record == null) {
                logger.debug("Kontakt nicht gefunden zum Deleten: {}", id);
                return Optional.empty();
            }

            Contact contact = Utils.recordToContact(record);

            create.deleteFrom(table(TABLE_NAME))
                .where(field("id").eq(id))
                .execute();

            logger.debug("Kontakt gelöscht: {}", id);
            return Optional.of(contact);
        } catch (SQLException e) {
            logger.error("Fehler beim Deleten des Kontakts: {}", id, e);
            throw new RepositoryException("Failed to delete contact", e);
        }
    }

    @Override
    public Optional<Contact> findById(String id) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            var record = create.select()
                .from(TABLE_NAME)
                .where(field("id").eq(id))
                .fetchOne();

            return record != null
                ? Optional.of(Utils.recordToContact(record))
                : Optional.empty();
        } catch (SQLException e) {
            logger.error("Fehler beim Suchen des Kontakts mit ID: {}", id, e);
            throw new RepositoryException("Failed to find contact by id", e);
        }
    }

    @Override
    public List<Contact> findAll() {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<Contact> foundContacts = create.select()
                .from(table(TABLE_NAME))
                .fetch(Utils::recordToContact);

            logger.debug("{} Kontakte gefunden", foundContacts.size());
            return foundContacts;
        } catch (SQLException e) {
            logger.error("Fehler beim Laden aller Kontakte", e);
            throw new RepositoryException("Failed to find all contacts", e);
        }
    }

    @Override
    public Optional<List<Contact>> findByName(String search) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<Contact> contacts = create.select()
                .from(table(TABLE_NAME))
                .where(DSL.lower(field("name", String.class)).like("%" + search.toLowerCase() + "%"))
                .fetch(record -> new Contact(
                        record.get("id", String.class),
                        record.get("name", String.class),
                        Utils.convertToOptionalNonBlank(record.get("phone", String.class)),
                        Utils.convertToOptionalNonBlank(record.get("address", String.class)),
                        Utils.convertToOptionalNonBlank(record.get("email", String.class))
                ));
            return Optional.of(contacts);
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Suchen von Kontakten mit Begriff: " + search, e);
        }
    }

    @Override
    public Optional<List<Contact>> findByPhone(String search) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<Contact> contacts = create.select()
                    .from(table(TABLE_NAME))
                    .where(DSL.lower(field("phone", String.class)).eq(search.toLowerCase()))
                    .fetch(record -> new Contact(
                            record.get("id", String.class),
                            record.get("name", String.class),
                            Utils.convertToOptionalNonBlank(record.get("phone", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("address", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("email", String.class))
                    ));
            return Optional.of(contacts);
        } catch (SQLException e) {
            throw new RepositoryException("Fehler beim Suchen von Kontakten mit Begriff: " + search, e);
        }
    }

    @Override
    public Optional<List<Contact>> findByEmail(String search) {

        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<Contact> contacts = create.select()
                    .from(table(TABLE_NAME))
                    .where(DSL.lower(field("email", String.class)).eq(search.toLowerCase()))
                    .fetch(record -> new Contact(
                            record.get("id", String.class),
                            record.get("name", String.class),
                            Utils.convertToOptionalNonBlank(record.get("phone", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("address", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("email", String.class))
                    ));
            return Optional.of(contacts);
        } catch (SQLException e) {
            throw new RepositoryException("Fehler beim Suchen von Kontakten mit Begriff: " + search, e);
        }
    }

    @Override
    public Optional<List<Contact>> findByAddresse(String search) {
        try (Connection c = getConnection()) {
            DSLContext create = using(c, SQLDialect.SQLITE);

            List<Contact> contacts = create.select()
                    .from(table(TABLE_NAME))
                    .where(DSL.lower(field("address", String.class)).like("%" + search.toLowerCase() + "%"))
                    .fetch(record -> new Contact(
                            record.get("id", String.class),
                            record.get("name", String.class),
                            Utils.convertToOptionalNonBlank(record.get("phone", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("address", String.class)),
                            Utils.convertToOptionalNonBlank(record.get("email", String.class))
                    ));
            return Optional.of(contacts);
        } catch (SQLException e) {
            throw new RepositoryException("Fehler beim Suchen von Kontakten mit Begriff: " + search, e);
        }
    }
}

