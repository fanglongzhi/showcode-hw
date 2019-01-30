package com.gmcc.msb.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-3:07 PM
 */
public class BeanUtil {

    private BeanUtil() {
    }

    /**
     * Copies properties from one object to another
     *
     * @param source
     * @return
     * @destination
     */
    public static void copyNonNullProperties(Object source, Object destination, String... ignoreProperties) {
        BeanUtils.copyProperties(source, destination,
                getNullPropertyNames(source, ignoreProperties));
    }

    /**
     * Returns an array of null properties of an object
     *
     * @param source
     * @param ignoreProperties
     * @return
     */
    private static String[] getNullPropertyNames(Object source, String... ignoreProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet();
        for (java.beans.PropertyDescriptor pd : pds) {
            //check if value of this property is null then add it to the collection
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            } else {
                if (srcValue instanceof String && ((String) srcValue).length() == 0) {
                    emptyNames.add(pd.getName());
                }
            }
        }
        emptyNames.addAll(Arrays.asList(ignoreProperties));
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
