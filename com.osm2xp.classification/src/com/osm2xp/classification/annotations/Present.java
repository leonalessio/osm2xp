package com.osm2xp.classification.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Indicates we should have additional column indicating whether the value for give field is present
 * 
 * @author 32kda
 *
 */
public @interface Present {

}
