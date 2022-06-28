package tk.lickem.whole.manager;


import lombok.Getter;
import org.atteo.classindex.ClassIndex;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.enchantments.AbstractEnchant;
import tk.lickem.whole.manager.dynamic.annotations.Init;
import tk.lickem.whole.manager.dynamic.annotations.PostInit;
import tk.lickem.whole.manager.dynamic.annotations.PreInit;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DynamicManger {

    @Getter private final List<AbstractEnchant> enchants = new LinkedList<>();
    private static final Map<Class<?>, Object> reflectionClasses = new HashMap<>();

    public static void init() {
        ClassIndex.getAnnotated(PreInit.class, Whole.class.getClassLoader()).forEach(DynamicManger::selfConstruct);
        ClassIndex.getAnnotated(Init.class, Whole.class.getClassLoader()).forEach(DynamicManger::selfConstruct);
        ClassIndex.getAnnotated(PostInit.class, Whole.class.getClassLoader()).forEach(DynamicManger::selfConstruct);
    }

    private static <T> T selfConstruct(Class clazz) {
        try {
            Constructor c = clazz.getDeclaredConstructor();
            c.setAccessible(true);

            T test = (T) c.newInstance();
            reflectionClasses.put(clazz, test);

            return test;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T get(Class<T> clazz) {
        return (T) reflectionClasses.get(clazz);
    }
}
