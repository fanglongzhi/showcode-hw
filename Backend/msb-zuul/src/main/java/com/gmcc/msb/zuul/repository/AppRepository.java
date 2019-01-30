package com.gmcc.msb.zuul.repository;

import com.gmcc.msb.zuul.entity.App;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yuan Chunhai
 */
@Repository
public interface AppRepository extends PagingAndSortingRepository<App, Integer> {


    /**
     * 通过appid查找app信息
     * @param appId
     * @return
     */
    App findOneByAppIdEquals(String appId);

    List<App> findAllByStatusEquals(int status);

}
