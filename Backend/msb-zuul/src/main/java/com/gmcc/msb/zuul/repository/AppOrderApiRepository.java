package com.gmcc.msb.zuul.repository;

import com.gmcc.msb.zuul.entity.AppOrderApi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yuan Chunhai
 */
@Repository
public interface AppOrderApiRepository extends PagingAndSortingRepository<AppOrderApi, Integer> {


    /**
     * 通过appid查找订阅关系
     *
     * @param appId
     * @return
     */
    List<AppOrderApi> findAllByAppIdEquals(Integer appId);

    /**
     * 查询app订阅api的情况
     * @param appId 应用id
     * @param apiId api id
     * @return
     */
    @Query(value = "select a from AppOrderApi a where appId = ?1 and apiId = ?2 and status = 2 and CURRENT_TIME between startDate  and endDate  ")
    List<AppOrderApi> findAllAvaiableOrder(Integer appId, Integer apiId);

    /**
     * 查询所有的order
     *
     * @return
     */
    @Query(value = "select a from AppOrderApi a where status = 2 and CURRENT_TIME < endDate ")
    List<AppOrderApi> findAllOrders();
}
