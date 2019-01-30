package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.msbbreak.entity.ServiceBreaker;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ServiceBreakerRepository extends PagingAndSortingRepository<ServiceBreaker, Integer> {
    void deleteByApiId(Integer apiId);

    @Modifying
    @Query("delete from ServiceBreaker sb where sb.apiId in ?1")
    void deleteByApiIdList(List<Integer> ids);

    void deleteByStrategyId(Integer strategyId);

    @Modifying
    @Query("update ServiceBreaker sb set sb.propertyValue = ?1,sb.updateTime = current_timestamp() where sb.strategyId=?2 and sb.type=?3")
    void updateStrategy(String propertyValue, Integer strategyId, String type);

    void deleteByStrategyIdAndType(Integer strategyId, String type);

}
