package com.gmcc.msb.demo.provider.util;

/**
 * @program:
 * @description: 登录用户信息持有类
 * @author: zhifanglong
 * @create: 2018-10-17 14:38
 */
public class UserContextHolder {
    private static final ThreadLocal<UserContext> userContext =
            new ThreadLocal<>();
    public static final UserContext getContext(){
        UserContext context = userContext.get();

        if(context==null){
            context=new UserContext();
            userContext.set(context);
        }

        return context;
    }
}
