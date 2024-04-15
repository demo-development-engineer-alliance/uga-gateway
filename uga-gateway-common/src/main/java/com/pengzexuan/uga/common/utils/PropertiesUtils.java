package com.pengzexuan.uga.common.utils;

import java.lang.reflect.Method;
import java.util.Properties;

public abstract class PropertiesUtils {

    public static void properties2Object(final Properties p, final Object object, String prefix) {
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            String mn = method.getName();
            if (mn.startsWith("set")) {
                try {
                	// set
                    String tmp = mn.substring(4);
                    // 	
                    String first = mn.substring(3, 4);

                    String key = prefix + first.toLowerCase() + tmp;
                    String property = p.getProperty(key);
                    if (property != null) {
                        Class<?>[] pt = method.getParameterTypes();
                        if (pt.length > 0) {
                            String cn = pt[0].getSimpleName();
                            Object arg = null;
                            switch (cn) {
                                case "int":
                                case "Integer":
                                    arg = Integer.parseInt(property);
                                    break;
                                case "long":
                                case "Long":
                                    arg = Long.parseLong(property);
                                    break;
                                case "double":
                                case "Double":
                                    arg = Double.parseDouble(property);
                                    break;
                                case "boolean":
                                case "Boolean":
                                    arg = Boolean.parseBoolean(property);
                                    break;
                                case "float":
                                case "Float":
                                    arg = Float.parseFloat(property);
                                    break;
                                case "String":
                                    arg = property;
                                    break;
                                default:
                                    continue;
                            }
                            method.invoke(object, arg);
                        }
                    }
                } catch (Throwable ignored) {
                }
            }
        }
    }
    
    public static void properties2Object(final Properties p, final Object object) {
        properties2Object(p, object, "");
    }
 
}
