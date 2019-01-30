package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.msbbreak.entity.BreakerStrategyLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BreakerStrategyLogRepository extends PagingAndSortingRepository<BreakerStrategyLog, Integer>, JpaSpecificationExecutor<BreakerStrategyLog> {

}
