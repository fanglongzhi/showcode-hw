package com.gmcc.msb.msbsystem.repository.role;

import com.gmcc.msb.msbsystem.entity.role.Role;
import com.gmcc.msb.msbsystem.vo.req.role.QueryRoleParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class RoleCustomerSpec {
    public static Specification<Role> queryRoleListSpec(QueryRoleParam param) {
        return new Specification<Role>() {
            @Override
            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                List<Predicate> list = new ArrayList<Predicate>();

                if (param.getId()!=null) {

                    list.add(builder.equal(root.get("id").as(Long.class),param.getId()));
                }

                if (StringUtils.isNotBlank(param.getRoleName())) {

                    list.add(builder.equal(root.get("roleName").as(String.class),param.getRoleName()));
                }

                Expression<Long> exp = root.get("user").get("orgId");
                list.add(exp.in(param.getOrgIds()));

                Predicate[] p = new Predicate[list.size()];
                return builder.and(list.toArray(p));
            }
        };
    }
}
