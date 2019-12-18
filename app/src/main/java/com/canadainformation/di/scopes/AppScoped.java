package com.canadainformation.di.scopes;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;
/**
 * Created By Akash
 * on 17,Dec,2019 : 2:30 PM
 */

/**
 * Replacement scope for @Singleton to improve readability
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScoped {
}