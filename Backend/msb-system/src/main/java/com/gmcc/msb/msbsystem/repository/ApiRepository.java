package com.gmcc.msb.msbsystem.repository;

import com.gmcc.msb.msbsystem.entity.API;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @program: msb-system
 * @description: API DAO
 * @author: zhifanglong
 * @create: 2018-10-11 14:16
 */
public interface ApiRepository extends CrudRepository<API,Integer> {
    /**
     * 批量查找API
     * @param ids
     * @return
     */
    List<API> findByIdIn(List<Integer> ids);
}
