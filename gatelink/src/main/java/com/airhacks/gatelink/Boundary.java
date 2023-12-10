
package com.airhacks.gatelink;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Stereotype;

/**
 *
 * @author airhacks.com
 */
@Stereotype
@RequestScoped
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Boundary {

}
