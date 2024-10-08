package ru.otus.hw.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;

import javax.sql.DataSource;

@Configuration
@EnableCaching
// Включить безопасность методов для поддержки аннотаций @PreAuthorize, @PostAuthorize, @PreFilter, @PostFilter
@EnableMethodSecurity(securedEnabled = true)
public class ACLConfiguration  {

    /* Используется для управления типами разрешений в ACL.  */
    @Bean
    public DefaultPermissionFactory permissionFactory() {
        /* Дефолтные разрешения */
        return new DefaultPermissionFactory();
        // Можно сделать кастомные разрешения.
        //return new DefaultPermissionFactory(FlipTablePermission.class);
    }


    /*
     * Стратегия авторизации в ACL определяет, кто и как может изменять ACL для других объектов и субъектов.
     * Сейчас это ROLE_EDITOR
     */
    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_EDITOR"));
    }

    /*
     * Логгер для аудита операций. Кажется это связано с auditSuccess и auditFailure
     */
    @Bean
    public ConsoleAuditLogger consoleAuditLogger() {
        return new ConsoleAuditLogger();
    }

    /*
     * Стратегия предоставления прав.
     * Решает каким образом будут назначаться права пользователю или группе.
     * Тут у меня кастомная стратегия, которая позволяет одной записью в БД предоставить права на несколько вещей.
     */
    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy(ConsoleAuditLogger consoleAuditLogger) {
        return new CumulativePermissionGrantingStrategy(consoleAuditLogger);
    }

    /*
     * Настройка кэша для ACL.
     * Он для него обязателен, так как ACL создаёт много новых данных.
     * Обычно подключают какой то сторонний кэш, типа caffeine
     * Тут в качестве кэша просто конкурентная хэш мапа
     */
    @Bean
    public SpringCacheBasedAclCache aclCache(PermissionGrantingStrategy permissionGrantingStrategy,
                                             AclAuthorizationStrategy aclAuthorizationStrategy) {
        final ConcurrentMapCache aclCache = new ConcurrentMapCache("acl_cache");
        return new SpringCacheBasedAclCache(aclCache, permissionGrantingStrategy, aclAuthorizationStrategy);
    }


    /*
     *  Стратегия поиска ACL (то бишь разрешений) в БД.
     *  Сейчас это всё выглядит стандартно.
     *
     */
    @Bean
    public LookupStrategy lookupStrategy(DataSource dataSource,
                                         SpringCacheBasedAclCache aclCache,
                                         AclAuthorizationStrategy aclAuthorizationStrategy,
                                         PermissionGrantingStrategy permissionGrantingStrategy) {
        BasicLookupStrategy lookupStrategy = new BasicLookupStrategy(dataSource,
                aclCache, aclAuthorizationStrategy, permissionGrantingStrategy);
        /*
         * Принудительное использование информации о типе идентификаторов сущностей из БД.
         * Можно не писать когда используется Long в качестве ID, но я перестрахуюсь.
         */
        lookupStrategy.setAclClassIdSupported(true);
        return lookupStrategy;
    }

    /*
     * Сервис для упраления ACL записями.
     */
    @Bean
    public MutableAclService aclService(DataSource dataSource,
                                        LookupStrategy lookupStrategy,
                                        SpringCacheBasedAclCache aclCache,
                                        AuditLogger auditLogger) {
        JdbcMutableAclService mutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);

        /*
         * Эти запросы нужны для того, чтобы выставлять разрешения на вставленную запись для субъекта который её создал
         */
        // Запрос на получение идентификатора созданной записи в acl_class
        mutableAclService.setClassIdentityQuery("SELECT MAX(id) FROM acl_class;");
        // Запрос на получение идентификатора созданной записи в acl_sid
        mutableAclService.setSidIdentityQuery("SELECT MAX(id) FROM acl_sid;");
        /*
         * Принудительное использование информации о типе идентификаторов сущностей из БД.
         * Можно не писать когда используется Long в качестве ID, но я перестрахуюсь.
         */
        mutableAclService.setAclClassIdSupported(true);

        return mutableAclService;
    }

    /*
     *  Компонент, который оценивает права доступа в выражениях безопасности (например, в аннотациях @PreAuthorize или @PostFilter).
     *  Он заменяет стандартный PermissionEvaluator и использует ACL для проверки прав доступа к объектам.
     */
    @Bean
    public AclPermissionEvaluator permissionEvaluator(MutableAclService aclService) {
        // Компонент для определения прав в SPEL взамен стандартному DenyAppPermissionEvaluator
//        aclPermissionEvaluator.setPermissionFactory(permissionFactory);
        return new AclPermissionEvaluator(aclService);
    }

    /*
     * Handler для обработки в методах, помеченных аннотациями.
     * Он сам внедряется к контект спринга и нужные компоненты.
     */
    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(
            AclPermissionEvaluator permissionEvaluator,
            ApplicationContext applicationContext) {
        // Компонент для обработки SPEL-выражений для безопасности методов
        // В отличие от DefaultHttpSecurityExpressionHandler он внедряется сам в зависимые объекты
        var defaultHttpSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        defaultHttpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return defaultHttpSecurityExpressionHandler;
    }

    /*
     * Handler работающий в http фильтрах.
     * Я так понимаю будет работать с аннотациями обработки лежащими в методах контроллера?
     */
    @Bean
    public DefaultHttpSecurityExpressionHandler httpSecurityExpressionHandler(
            AclPermissionEvaluator permissionEvaluator,
            ApplicationContext applicationContext) {
        // Компонент для обработки SPEL-выражений для HTTP-безопасности
        var defaultHttpSecurityExpressionHandler = new DefaultHttpSecurityExpressionHandler();
        defaultHttpSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        defaultHttpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return defaultHttpSecurityExpressionHandler;
    }

}
