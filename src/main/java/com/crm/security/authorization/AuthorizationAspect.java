package com.crm.security.authorization;

import com.crm.exception.UnauthorizedException;
import com.crm.security.anotations.HasAnyPermissions;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
@Aspect
public class AuthorizationAspect {

    private static final String MESSAGE = "You do not have permission to call this operation. Please contact admin to get suitable permission.";

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(permissions)")
    public void hasAuthorities(final JoinPoint joinPoint, final HasAnyPermissions permissions) {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if (Objects.isNull(securityContext)) {
            log.error("The security context is null when checking endpoint access for user request");
            throw new UnauthorizedException(MESSAGE);
        }
        final Authentication authentication = securityContext.getAuthentication();
        if (Objects.isNull(authentication)) {
            log.error("The authentication is null when checking endpoint access for user request");
            throw new UnauthorizedException(MESSAGE);
        }
        final String username = authentication.getName();
        final Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        if (Stream.of(permissions.permissions()).noneMatch(permission -> userAuthorities.stream().anyMatch(userAuthority -> userAuthority.getAuthority().equals(permission.name())))) {
            log.error("User {} does not have the correct authorities required by endpoint", username);
            throw new UnauthorizedException(MESSAGE);
        }
    }

}
