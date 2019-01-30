package com.gmcc.msb.msbservice.repository;

import com.gmcc.msb.msbservice.entity.ServiceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author zhi fanglong
 */
public interface ServiceLogRepository extends PagingAndSortingRepository<ServiceLog, Integer> {


    /**
     * 查找时间和内容相同的记录
     * @param time
     * @param content
     * @return
     */
    ServiceLog findOneByTimeEqualsAndContentEquals(Long time, String content);

    @Query("select l from ServiceLog l,ServiceItem si where si.serviceId = l.serviceId and si.user.orgId in ?1")
    Page<ServiceLog> findServiceLogByOrgs(List<Long> orgIds, Pageable pageable);



}
