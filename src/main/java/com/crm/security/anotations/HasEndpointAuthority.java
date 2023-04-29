package com.crm.security.anotations;

import com.crm.security.enums.SecurityAuthority;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface HasEndpointAuthority {

    SecurityAuthority value();

}