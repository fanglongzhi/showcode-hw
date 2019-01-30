package com.gmcc.msb.msbbreak.service;

import com.gmcc.msb.msbbreak.entity.BreakerStrategyLog;
import com.gmcc.msb.msbbreak.repository.BreakerStrategyLogRepository;
import com.gmcc.msb.msbbreak.repository.ServiceApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BreakerStrategyLogService {

    @Autowired
    private BreakerStrategyLogRepository breakerStrategyLogRepository;

    public BreakerStrategyLog save(BreakerStrategyLog log){
        breakerStrategyLogRepository.save(log);
        return log;
    }

    public Page<BreakerStrategyLog> findAll(Long beginDate, Long endDate, String strategyName, String apis, Pageable pageable) {
        Specification querySpecifi = new Specification<BreakerStrategyLog>() {
            @Override
            public Predicate toPredicate(Root<BreakerStrategyLog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(beginDate!=null && beginDate >0){

                    predicates.add(criteriaBuilder.greaterThan(root.get("createTime"),new Date(beginDate)));
                }
                if(endDate!=null && endDate>0){
                    predicates.add(criteriaBuilder.lessThan(root.get("createTime"), new Date(endDate)));
                }
                if(!StringUtils.isEmpty(strategyName)){
                    predicates.add(criteriaBuilder.like(root.get("strategyName"), "%"+strategyName+"%"));
                }

                if(!StringUtils.isEmpty(apis)){
                    predicates.add(criteriaBuilder.like(root.get("apis"), "%"+apis+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return breakerStrategyLogRepository.findAll(querySpecifi,pageable);
    }
}
