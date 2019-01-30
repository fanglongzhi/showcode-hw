package com.gmcc.msb.common.config;

import com.gmcc.msb.common.entity.Operator;
import com.gmcc.msb.common.property.UserContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.AuditorAware;

/**
 * @program: msb-common
 * @description: 用户信息自动注入识别类
 * @author: zhifanglong
 * @create: 2018-10-24 11:48
 */
public class UserInfoAware implements AuditorAware<Operator> {
    @Override
    public Operator getCurrentAuditor() {
        String userId = UserContextHolder.getContext().getUserId();
        Operator user = new Operator();
        user.setUserId(userId);

        if(StringUtils.isNotBlank(userId)) {
            user.setOrgId(UserContextHolder.getContext().getOrgId());
        }
        return user;
    }
}
