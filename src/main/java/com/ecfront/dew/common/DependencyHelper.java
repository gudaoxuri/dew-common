package com.ecfront.dew.common;

/**
 * The type Dependency helper.
 *
 * @author gudaoxuri
 */
public class DependencyHelper {

    /**
     * Has dependency boolean.
     *
     * @param clazz the clazz
     * @return the boolean
     */
    public static boolean hasDependency(String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
