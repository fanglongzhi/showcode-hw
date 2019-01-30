package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbbreak.entity.BreakerStrategy;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class BreakerStrategySpec {
    private BreakerStrategySpec() {
    }

    public static Specification<BreakerStrategy> queryBreakerListSpec(BreakerStrategy param) {
        return (Root<BreakerStrategy> root, CriteriaQuery<?> query,
                CriteriaBuilder builder) -> {

            List<Predicate> list = new ArrayList<>();
            if (StringUtils.isNotBlank(param.getStrategyName())) {

                list.add(builder.equal(root.get("strategyName").as(String.class), param.getStrategyName()));
            }

              list.add(builder.equal(root.get("isDefault").as(Boolean.class),false));
            Predicate[] p = new Predicate[list.size()];

            Expression<Long> exp = root.get("user").get("orgId");
            list.add(exp.in(UserContextHolder.getContext().getDataOrgList()));


            return builder.and(list.toArray(p));
        };
    }

}
