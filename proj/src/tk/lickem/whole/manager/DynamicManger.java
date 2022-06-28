package tk.lickem.whole.manager;


import lombok.Getter;
import org.atteo.classindex.ClassIndex;
import tk.lickem.whole.Whole;
import tk.lickem.whole.data.enchantments.IEnchant;
import tk.lickem.whole.manager.dynamic.anno.Init;
import tk.lickem.whole.manager.dynamic.anno.PostInit;
import tk.lickem.whole.manager.dynamic.anno.PreInit;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;

public class DynamicManger {

    @Getter private final LinkedList<IEnchant> enchants = new LinkedList<>();
    @Getter private static final HashMap<Class<?>, Object> reflectionClasses = new HashMap<>();

    public static void init() {
        ClassIndex.getAnnotated(PreInit.class, Whole.class.getClassLoader()).forEach(DynamicManger::newC);
        ClassIndex.getAnnotated(Init.class, Whole.class.getClassLoader()).forEach(DynamicManger::newC);
        ClassIndex.getAnnotated(PostInit.class, Whole.class.getClassLoader()).forEach(DynamicManger::newC);
    }

    private static <T> T newC(Class clazz) {
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
