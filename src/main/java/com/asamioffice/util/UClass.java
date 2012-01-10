package com.asamioffice.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.asamioffice.text.UJavaString;

/**
 * UClass
 *
 * @since   Apr. 13, 2004
 * @version Dec.  2, 2006
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class UClass {
/*
    public static String getPackageName(Class clazz) {
       Package target = clazz.getPackage();
       if (target != null) {
           return (target.getName());
       } else {
           String className = clazz.getName();
           int index = className.lastIndexOf("."); 
           if (index == -1) {
               return (null);
           } else { // JDK problem?
               return (className.substring(0, index));
           }
       }
    }
*/

    public static String getSimpleName(String className) {
        className = getPlainClassName(className);
        int index = className.lastIndexOf('.');
        if (index == -1) {
            return className;
        } else {
            return className.substring(index + 1);
        }
    }

    public static String getPackageName(String className) {
        className = getPlainClassName(className);
        int index = className.lastIndexOf('.');
        if (index == -1) {
            return null;
        } else {
            return className.substring(0, index);
        }
    }

    // XXX Plain?
    public static String getPlainClassName(String className) {
        int index = className.indexOf('<');
        if (index == -1) {
            return className;
        } else {
            return className.substring(0, index);
        }
    }

    public static String getPropertyName(Method method) {
        return UJavaString.getPropertyName(method.getName());
    }

    public static Class getPropertyType(Method method) {
        if (isPropertyGetter(method)) {
            return method.getReturnType();
        } else if (isPropertySetter(method)) {
            return method.getParameterTypes()[0];
         } else {
            throw new IllegalArgumentException();
        }
    }

    public static boolean isPropertyGetter(Method method) {
        String name = method.getName();
        Class[] types = method.getParameterTypes();
        if (types.length != 0) {
            return false;
        }
        if ("getClass".equals(name)) {
            return false;
        }
        if (name.startsWith("get")) {
            return name.length() > "get".length();
        } else if (name.startsWith("is")) {
            return name.length() > "is".length();
        } else {
            return false;
        }
    }

    public static boolean isPropertySetter(Method method) {
        String name = method.getName();
        Class[] types = method.getParameterTypes();
        return (name.length() > "set".length() &&
                name.startsWith("set") &&
                types.length == 1);
    }

    public static boolean isService(Method method) {
        if (UClass.isPropertySetter(method)) {
            return false;
        }
        if (UClass.isPropertyGetter(method)) {
            return false;
        }
        String name = method.getName();
        if ("toString".equals(name) ||
                "hashCode".equals(name) ||
                "equals".equals(name) ||
                "notify".equals(name) ||
                "notifyAll".equals(name) ||
                "wait".equals(name) ||
                "getClass".equals(name)) {
            return false;
        }
        return true;
    }

    public static Object execute(Object object, String name, Object[] args) 
    throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, 
    InvocationTargetException {
        Method method = findOperationMethod(object, name, args);
        if (method == null) {
            throw new NoSuchMethodException();
        }
        return method.invoke(object, args);
    }

    public static Method findOperationMethod(Object object, String name, Object[] args) {
        for (Method method : object.getClass().getMethods()) {
            if (isMatchOperatoin(name, args, method)) {
                return method;
            }
        }
        return null;
    }

    public static boolean isMatchOperatoin(String name, Object[] args, Method method) {
        if (!name.equals(method.getName())) {
            return false;
        }
        return matchParams_(method, args);
    }

    private static boolean matchParams_(Method method, Object[] args) {
        Class<?>[] types = method.getParameterTypes();
        if (args.length != types.length) {
            return false;
        }
        for (int i = 0;i < args.length;i++) {
            if (!types[i].isAssignableFrom(args[i].getClass())) {
                return false;
            }
        }
        return true;
    }

    public static void setProperty(Object object, String name, Object value) 
    throws NoSuchMethodException, IllegalArgumentException, 
    IllegalAccessException, InvocationTargetException {
        Method method = findSetterMethod(object, name, value);
        if (method == null) {
            throw new NoSuchMethodException();
        }
        method.invoke(object, value);
    }

    public static Method findSetterMethod(Object object, String name, Object value) {
        for (Method method : object.getClass().getMethods()) {
            if (isMatchSetter(method, name, value)) {
                return method;
            }
        }
        return null;
    }

    public static boolean isMatchSetter(Method method, String name, Object value) {
        if (!UClass.isPropertySetter(method)) {
            return false;
        }
        if (!name.equals(UClass.getPropertyName(method))) {
            return false; 
        }
        Class<?>[] types = method.getParameterTypes();
        if (types.length != 1) {
            return false;
        }
        return types[0].isAssignableFrom(value.getClass());
    }
}
