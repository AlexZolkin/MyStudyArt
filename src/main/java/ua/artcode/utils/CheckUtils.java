package ua.artcode.utils;

import ua.artcode.model.CheckResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.List;

/**
 * Created by Алексей on 17.04.2017.
 */
public class CheckUtils {
    public static CheckResult runCheckMethod(String className, URLClassLoader classLoader, String methodName)
            throws ClassNotFoundException {
        List<String> results = null;
        try {
            Class<?> cls = Class.forName(className, true, classLoader);
            Method mainMethod = cls.getMethod(methodName);
            results = (List<String>) mainMethod.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return StatsUtils.stats(results);
    }
}
