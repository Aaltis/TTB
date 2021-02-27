package fi.breakwaterworks.config.security.acl;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
@EnableAutoConfiguration
@EnableCaching
public class ACLContext {
	
	
	@Autowired 
	CacheManager cacheManager;
	
    @Autowired
    DataSource dataSource;

    
    // ABOUT CHANGED CACHING:
    //https://stackoverflow.com/questions/56157479/spring-security-acl-with-ehcache-3
    @Bean
    public AclCache aclCache() {
        return new SpringCacheBasedAclCache(
                cacheManager.getCache("aclCache"), 
                permissionGrantingStrategy(), 
                aclAuthorizationStrategy());        
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
    
    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService()));
        return expressionHandler;
    }

    @Bean
    public LookupStrategy lookupStrategy() {
    	MyLookupStrategy lookupStrategy = new MyLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
        return lookupStrategy;
    }
    
  
    @Bean
    public JdbcMutableAclService aclService() {
    	JdbcMutableAclService jdbcMutableAclService= new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
        return jdbcMutableAclService;
    }
    
    //#4 https://github.com/spring-projects/spring-security/issues/7978

}