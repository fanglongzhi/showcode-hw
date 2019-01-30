package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.AppOrderApi;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface AppOrderApiRepository extends PagingAndSortingRepository<AppOrderApi, Integer> {


    long countAllByApiIdEquals(Integer apiId);

    @Query("SELECT new map(b.id as id,b.orderId as orderId,b.appId as appId," +
                   "b.apiId as apiId," +
                   "b.startDate as startDate,b.endDate as endDate," +
                   "a.appName as appName," +
                   "c.apiName as apiName,s.serviceId as serviceId,s.serviceName as serviceName," +
                   "c.path as path,c.method as method," +
                   "b.applyType as applyType, " +
                   "b.auditResult as auditResult, b.reason as reason," +
                   "b.applyDate as applyDate,b.auditDate as auditDate,b.updateDate as updateDate ) FROM App a," +
                   "AppOrderApiAudit b,API c,Serv s where a.user.orgId in ?1 and a.id = b.appId " +
                   "and b.apiId = c.id and s.serviceId = c.serviceId order by b.updateDate desc")
    List<Map<String, Object>> getAuditAll(List<Long> orgList);

    @Query("SELECT new map(b.id as id,b.appId as appId," +
                   "b.apiId as apiId," +
                   "b.startDate as startDate,b.endDate as endDate," +
                   "b.status as status,a.appName as appName," +
                   "c.apiName as apiName,s.serviceId as serviceId,s.serviceName as serviceName," +
                   "c.path as path,c.method as method," +
                   "b.createDate as createDate,b.updateDate as updateDate) FROM App a," +
                   "AppOrderApi b,API c,Serv s where a.user.orgId in ?1 and a.id = b.appId " +
                   "and b.apiId = c.id and s.serviceId = c.serviceId order by b.updateDate desc")
    List<Map<String, Object>> getAll(List<Long> orgList);

    @Query("SELECT new map(b.id as id,b.appId as appId," +
            "b.apiId as apiId," +
            "b.startDate as startDate,b.endDate as endDate," +
            "b.status as status,a.appName as appName," +
            "c.apiName as apiName,s.serviceId as serviceId,s.serviceName as serviceName," +
            "c.path as path,c.method as method," +
            "b.createDate as createDate,b.updateDate as updateDate) FROM App a," +
            "AppOrderApi b,API c,Serv s where a.id = ?2 and a.user.orgId in ?1 and a.id = b.appId " +
            "and b.apiId = c.id and s.serviceId = c.serviceId order by b.updateDate desc")
    List<Map<String, Object>> getAll(List<Long> orgList,Integer appId);

    List<AppOrderApi> findAllByAppIdAndApiId(Integer appId, Integer apiId);

    long countByAppId(Integer appId);

    @Modifying
    @Transactional
    @Query("update AppOrderApi set status = 3 where id in ?1 and status =2")
    void batchUnsubscribe(List<Integer> ids);

    @Modifying
    @Transactional
    @Query("delete from AppOrderApi where id in ?1")
    void deleteList(List<Integer> ids);

    List<AppOrderApi> findAllByIdIn(List<Integer> ids);

    /**
     * 计算是否存在时间重叠
     *
     * @param appId
     * @param apiId
     * @param status
     * @return
     */
    @Query("select a from AppOrderApi a where appId = ?1 and apiId = ?2 and status in ?3 " +
                   "and (" +
                   "( ?4 between startDate and endDate ) " +
                   "or (?5 between startDate and endDate )    " +
                   "or (?4 < startDate and ?5 > endDate ) " +
                   ")")
    List<AppOrderApi> countByTimeOverlap(
            Integer appId, Integer apiId, List<Integer> status, Date startDate, Date endDate
    );

    List<AppOrderApi> findAllByStatusEqualsAndEndDateAfter(int status, Date endDate);
}
