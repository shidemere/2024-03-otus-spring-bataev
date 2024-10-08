package ru.otus.hw.config;

import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Permission;

/**
 * Реализация собественного разрешения для Spring ACL.
 * Чтобы не пришлось для одной сущности выдавать права на чтение + изменения + создание + удаление с помощью 4-х записей в БД
 * Теперь это будет делаться одной записью.
 * Для корректной работы необходимо зарегистрировать в AclService
 */
public class CumulativePermissionGrantingStrategy extends DefaultPermissionGrantingStrategy {

    public CumulativePermissionGrantingStrategy(AuditLogger auditLogger) {
        super(auditLogger);
    }

    @Override
    protected boolean isGranted(AccessControlEntry ace, Permission p) {
        /* Если в выражении слева не совпадают значения друг с другом - будет возвращено какое то левое значение и тогда всё выражение
        *   бдует считатся ложным
        */
        return (ace.getPermission().getMask() & p.getMask()) == p.getMask();
    }
}
