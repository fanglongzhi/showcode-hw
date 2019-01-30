package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Zhi Fanglong
 */

@Repository
public interface AppRepository extends PagingAndSortingRepository<App, Integer> {


    @Query(value = "select new map(" +
                           "b.id as id , a.appId as appId, a.appName as appName," +
                           "a.company as company, a.description as description," +
                           "a.createTime as createDate, b.startDate as startDate," +
                           "b.endDate as endDate, a.status as status" +
                           ") from App a, AppOrderApi b " +
                           "where a.id = b.appId and b.apiId = ?1")
    /** 订阅了api的应用*/
    Page<Map<String, Object>> appsOrderedApiId(Integer apiId, Pageable pageable);

    int countByAppName(String appName);

    int countByAppNameAndIdNot(String appName, Integer id);

    @Query("SELECT new map(b.id as id, c.serviceId as serviceId,s.serviceName as serviceName," +
                   "c.apiName as apiName,c.path as path," +
                   "c.method as method, b.status as status,c.isAnnoymousAccess as isAnnoymousAccess) FROM App a," +
                   "AppOrderApi b,API c,Serv s where a.id = b.appId " +
                   "and b.apiId = c.id and c.serviceId = s.serviceId and a.id=?1")
    List<Map<String,Object>> findApiOfAppSubscribe(Integer id);

    @Query("select a from App a where a.user.orgId in ?1")
    List<App> findAppByOrgList(List<Long> orgList,Sort sort);

    /**
     * 批量查询app
     * @param ids app的id集合
     * @return
     */
    List<App> findAllByIdIn(List<Integer> ids);


    /**
     * 根据状态统计数量
     *
     * @param status
     * @return
     */
    int countAllByStatusEquals(int status);

    List<App> findAllByStatusEquals(int status);
}
