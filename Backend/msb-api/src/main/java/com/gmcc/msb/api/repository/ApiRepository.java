package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.API;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiRepository extends PagingAndSortingRepository<API, Integer> {

    @Query("select a from API a, Serv s where a.serviceId = s.serviceId "
                   + "and ( ?1 = null or a.apiName like ?1) "
                   + "and ( ?2 = null or a.serviceId like ?2)"
                   + "and ( ?3 = null or s.serviceName like ?3) "
                   + "and ( ?4 = null or a.status = ?4) and a.user.orgId in ?5"
    )
    Page<API> findAllByConditions(String apiName, String serviceId, String serviceName,
                                  Integer status, List<Long> orgList,Pageable pageable);

    @Query("select a from API a, Serv s where a.serviceId = s.serviceId "
            + "and ( ?1 = null or a.apiName like ?1) "
            + "and ( ?2 = null or a.serviceId like ?2)"
            + "and ( ?3 = null or s.serviceName like ?3) "
            + "and ( ?4 = null or a.status = ?4) "
    )
    Page<API> findAll(String apiName, String serviceId, String serviceName,
                                  Integer status, Pageable pageable);

    /** 通过服务id和状态查找所有的api */
    Iterable<API> findAllByServiceIdEqualsAndStatusEquals(String serviceId, Integer status);


    @Query("select a from API a where a.id in ( select apiId from ApiGroupItem g where g.groupId = ?1)")
    List<API> findAllInGroup(Integer groupId);

    Page<API> findAllByStatusIn(List<Integer> status, Pageable pageable);

    Iterable<API> findByServiceIdAndPathAndMethod(String serviceId,String path,String method);

    List<API> findAllByServiceIdEquals(String serviceId);

    List<API> findAllByIdIn(List<Integer> ids);


    @Query(value = "select count(1) from t_rate_limit_strategy st,t_service_api sa " +
            "where st.object_id=sa.id and st.type='api' and sa.id=?1 ",nativeQuery = true)
    int hasFluid(Integer apiId);
    @Query(value = "select count(1) from t_breaker_strategy_api bs,t_service_api sa " +
            "where bs.api_id = sa.id and sa.id=?1",nativeQuery = true)
    int hasCircuit(Integer apiId);


    /**
     * 通过状态统计api数量
     */
    int countAllByStatusEquals(int status);
}
