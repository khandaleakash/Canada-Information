package com.canadainformation.data.scopes;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created By Akash
 * on 17,Dec,2019 : 3:46 PM
 */
/**
 * This scope has been created for Dagger to differentiate between types of Data Sources
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {

}
