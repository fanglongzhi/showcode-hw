package com.gmcc.msb.msbbreak.repository;

import com.gmcc.msb.msbbreak.entity.ServiceApi;
import com.gmcc.msb.msbbreak.vo.resp.ApiVo;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ServiceApiRepository extends PagingAndSortingRepository<ServiceApi,Integer>,JpaSpecificationExecutor<ServiceApi> {
    @Query("select new com.gmcc.msb.msbbreak.vo.resp.ApiVo(sa.id,sa.serviceId,sa.apiName,sa.path,sa.method)" +
            " from ServiceApi sa " +
            "where sa.orgId in ?1")
    List<ApiVo> findApiVoInOrgs(List<Long> orgIdList);

    @Query("select sa from ServiceApi sa where sa.id in ?1")
    List<ServiceApi> findApiByIdList(List<Integer> ids);
}
