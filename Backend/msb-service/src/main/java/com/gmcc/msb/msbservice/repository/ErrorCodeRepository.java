package com.gmcc.msb.msbservice.repository;

import com.gmcc.msb.msbservice.entity.ErrorCode;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-2:23 PM
 */
@Repository
public interface ErrorCodeRepository extends PagingAndSortingRepository<ErrorCode, Integer> {


    /**
     * 通过code查找记录
     *
     * @param code
     * @return
     */
    ErrorCode findOneByCodeEquals(String code);


    /**
     * 查询微服务的所有错误码
     *
     * @param serviceId
     * @return
     */
    List<ErrorCode> findAllByServiceIdEquals(String serviceId);
}
