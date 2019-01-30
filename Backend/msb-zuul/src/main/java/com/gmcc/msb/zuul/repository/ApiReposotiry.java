package com.gmcc.msb.zuul.repository;

import com.gmcc.msb.zuul.entity.ApiInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 12/18/2018-3:06 PM
 */
public interface ApiReposotiry extends CrudRepository<ApiInfo, Integer> {

    List<ApiInfo> findAllByServiceIdEqualsAndStatusEquals(String serviceId, int status);

    List<ApiInfo> findAllByStatusEquals(int status);
}
