package com.gmcc.msb.msbsystem.repository.user;

import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.vo.req.user.QueryUserParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserCustomerSpec {
    public static Specification<User> queryUserListSpec(QueryUserParam param) {
        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                List<Predicate> list = new ArrayList<Predicate>();

                if (StringUtils.isNotBlank(param.getOperatorId())) {

                    list.add(builder.equal(root.get("operatorId").as(Long.class),Long.valueOf(param.getOperatorId())));
                }

                if (StringUtils.isNotBlank(param.getName())) {

                    list.add(builder.equal(root.get("name").as(String.class),param.getName()));
                }

                if (param.getStatus()!=null) {

                    list.add(builder.equal(root.get("status").as(UserStatus.class),param.getStatus()));
                }

                if (param.getIsLock()!=null) {

                    list.add(builder.equal(root.get("isLock").as(Boolean.class),param.getIsLock()));
                }

                Predicate[] p = new Predicate[list.size()];
                return builder.and(list.toArray(p));
            }
        };
    }
}
