package ru.otus.hw.service.security;

public interface GrantGivingService {

    <T> void setReadGrant(T entity);

    <T> void setCreateGrant(T entity);

    <T> void setWriteGrant(T entity);

    <T> void setDeleteGrant(T entity);

    <T> void setAdminGrant(T entity);
}
