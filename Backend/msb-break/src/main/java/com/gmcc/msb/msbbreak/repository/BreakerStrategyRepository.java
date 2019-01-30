package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.msbbreak.entity.BreakerStrategy;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BreakerStrategyRepository extends PagingAndSortingRepository<BreakerStrategy, Integer>, JpaSpecificationExecutor<BreakerStrategy> {
    BreakerStrategy findByIsDefault(Boolean isDefault);

    @Modifying
    @Query("update BreakerStrategy bs set bs.enableBreaker = ?2,bs.updateTime = current_timestamp() where bs.id = ?1")
    void updateBreakerStatus(Integer id, Boolean enableBreaker);

    @Query("select bs from BreakerStrategy bs,BreakerStrategyApi api where bs.id = api.strategyId and api.apiId = ?1 ")
    BreakerStrategy findBreakerStrategyByApiId(Integer id);

    @Query("select bs from BreakerStrategy bs where bs.id = ?1 and bs.isDefault = false ")
    BreakerStrategy findBreakerStrategyByIdNoDefault(Integer id);

    int countByStrategyName(String strategyName);

    @Query("select count(1) from BreakerStrategy bs where bs.id!=?2 and bs.strategyName = ?1")
    int countByStrategyNameAndIdNot(String strategyName, Integer id);
    @Query("select count(1) from BreakerStrategy bs where bs.isDefault = false and bs.strategyName = ?1")
    int countByStrategyNameAndIsDefaultNot(String strategyName);

}
