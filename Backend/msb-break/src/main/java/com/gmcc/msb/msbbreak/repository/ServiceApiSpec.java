package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbbreak.entity.ServiceApi;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceApiSpec {
    private ServiceApiSpec(){}
    public static Specification<ServiceApi> queryApiListSpec(ServiceApi param) {
        return (Root<ServiceApi> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder)-> {

                List<Predicate> list = new ArrayList<>();
                if (StringUtils.isNotBlank(param.getApiName())){

                    list.add(builder.equal(root.get("apiName").as(String.class),param.getApiName()));
                }

            Expression<Long> exp = root.get("orgId");
            list.add(exp.in(UserContextHolder.getContext().getDataOrgList()));
                Predicate[] p = new Predicate[list.size()];
                return builder.and(list.toArray(p));
            };

    }
}
