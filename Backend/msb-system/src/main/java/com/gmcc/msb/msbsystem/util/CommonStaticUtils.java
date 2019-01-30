package com.gmcc.msb.msbsystem.util;

public class CommonStaticUtils {
    public final static String shortDateFormat = "yyyyMMdd";
    public final static String dateFormat = "yyyyMMdd HH:mm:ss";

    public static Object toDefaultValue(Object target,Object defaultValue){
        if(target == null){
            return defaultValue;
        }
        return target;
    }
}
