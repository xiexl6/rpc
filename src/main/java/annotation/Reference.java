package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
* 标记一个需要rpc注入的资源
*/
@Target(ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Reference {

}
