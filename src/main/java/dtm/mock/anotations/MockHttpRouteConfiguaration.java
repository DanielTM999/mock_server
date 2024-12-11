package dtm.mock.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dtm.mock.core.enums.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MockHttpRouteConfiguaration {
    String route() default "/";
    HttpMethod httpMethod() default HttpMethod.GET;
}
