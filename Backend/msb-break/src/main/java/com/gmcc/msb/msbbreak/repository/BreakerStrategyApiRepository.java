package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.msbbreak.entity.BreakerStrategyApi;
import com.gmcc.msb.msbbreak.vo.resp.ApiVo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BreakerStrategyApiRepository extends PagingAndSortingRepository<BreakerStrategyApi, Integer> {
    void deleteByApiId(Integer id);

    void deleteByStrategyId(Integer id);

    @Query("select new com.gmcc.msb.msbbreak.vo.resp.ApiVo(sa.id,sa.serviceId,sa.apiName,sa.path,sa.method," +
            "bs.id,bs.strategyName,bs.enableBreaker,bs.requestVolume,bs.sleep,bs.failRate,bs.timeout) from BreakerStrategy bs,BreakerStrategyApi ba,ServiceApi sa " +
            "where bs.id = ba.strategyId and ba.apiId = sa.id and sa.orgId in ?1")
    List<ApiVo> findApiVoInOrgs(List<Long> orgIdList);

    @Modifying
    @Query("delete from BreakerStrategyApi ba where ba.apiId in ?1")
    void deleteByApiIdList(List<Integer> apiIdList);

}
