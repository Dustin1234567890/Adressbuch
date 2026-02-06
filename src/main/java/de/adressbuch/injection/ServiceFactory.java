package de.adressbuch.injection;

import de.adressbuch.repository.SQLiteContactGroupRepo;
import de.adressbuch.repository.SQLiteContactRepo;
import de.adressbuch.repository.SQLiteGroupRepo;
import de.adressbuch.repository.interfaces.ContactGroupRepo;
import de.adressbuch.repository.interfaces.ContactRepo;
import de.adressbuch.repository.interfaces.GroupRepo;
import de.adressbuch.service.ContactGroupService;
import de.adressbuch.service.ContactService;
import de.adressbuch.service.GroupService;

public class ServiceFactory {
    private static ServiceFactory instance;
    
    private final ContactRepo contactRepository;
    private final GroupRepo groupRepository;
    private final ContactGroupRepo contactGroupRepository;
    
    private final ContactService contactService;
    private final GroupService groupService;
    private final ContactGroupService contactGroupService;

    private ServiceFactory() {
        this.contactRepository = new SQLiteContactRepo(DatabaseConfig.DB_URL);
        this.groupRepository = new SQLiteGroupRepo(DatabaseConfig.DB_URL);
        this.contactGroupRepository = new SQLiteContactGroupRepo(DatabaseConfig.DB_URL);
        
        this.contactService = new ContactService(contactRepository);
        this.groupService = new GroupService(groupRepository);
        this.contactGroupService = new ContactGroupService(contactGroupRepository, groupService, contactService);
    }

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public ContactService getContactService() {
        return contactService;
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public ContactGroupService getContactGroupService() {
        return contactGroupService;
    }

    public ContactRepo getContactRepository() {
        return contactRepository;
    }

    public GroupRepo getGroupRepository() {
        return groupRepository;
    }

    public ContactGroupRepo getContactGroupRepository() {
        return contactGroupRepository;
    }
}
