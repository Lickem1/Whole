package tk.lickem.whole.manager.dynamic.annotations;

import org.atteo.classindex.IndexAnnotated;
import tk.lickem.whole.data.packet.ClassType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@IndexAnnotated
public @interface Init {

    ClassType[] classType();
}
