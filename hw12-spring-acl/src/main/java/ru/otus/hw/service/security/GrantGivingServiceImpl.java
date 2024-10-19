package ru.otus.hw.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrantGivingServiceImpl implements GrantGivingService {

    private final MutableAclService acl;

    @Override
    public <T> void setReadGrant(T entity) {
        setGrant(entity, BasePermission.READ, "ROLE_USER", "ROLE_ADMIN", "ROLE_EDITOR");
    }

    @Override
    public <T> void setCreateGrant(T entity) {
        setGrant(entity, BasePermission.CREATE, "ROLE_ADMIN", "ROLE_EDITOR");
    }

    @Override
    public <T> void setWriteGrant(T entity) {
        setGrant(entity, BasePermission.WRITE, "ROLE_ADMIN", "ROLE_EDITOR");
    }

    @Override
    public <T> void setDeleteGrant(T entity) {
        setGrant(entity, BasePermission.DELETE, "ROLE_EDITOR");
    }

    @Override
    public <T> void setAdminGrant(T entity) {
        setGrant(entity, BasePermission.ADMINISTRATION, "ROLE_EDITOR");
    }

    private <T> void setGrant(T entity, Permission permission, String... roles) {
        ObjectIdentity identity = new ObjectIdentityImpl(entity);
        MutableAcl mutableAcl;

        try {
            // Проверка, существует ли уже ACL для объекта
            mutableAcl = (MutableAcl) acl.readAclById(identity);
        } catch (NotFoundException e) {
            // Если ACL не существует, создаем новый
            mutableAcl = acl.createAcl(identity);
        }

        /*
         *  Добавление прав доступа для различных ролей.
         *  mutableAcl.getEntries().size() - индекс, куда будет вставлена следующая запись
         *  Когдая я создаю книгу - сначара работает выдача разрешений для USER и на тот момент еще нет никаких разрешений, поэтому size() == 0
         *  Затем уже происходит выдача прав для ROLE_ADMIN. Так как ACL для этого объекта существует - отрабатывает блок try полностью
         *  И в этот ACL уже существующий, в самый конец всех правил я и добавлю новое правило для доступа
         */
        for (String role : roles) {
            mutableAcl.insertAce(mutableAcl.getEntries().size(), permission, new GrantedAuthoritySid(role), true);
        }

        acl.updateAcl(mutableAcl);
    }
}
