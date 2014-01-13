/**
 * 
 */
package org.fao.aoscs.domain;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author rajbhandari
 *
 */

@Documented
@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface VBConfigInfo {
    String key();
	String description();
	String defaultValue();
	String separator() default ";";
    boolean mandatory() default false;
}
