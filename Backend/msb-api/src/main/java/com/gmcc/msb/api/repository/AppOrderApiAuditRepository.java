package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.AppOrderApiAudit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppOrderApiAuditRepository extends PagingAndSortingRepository<AppOrderApiAudit, Integer> {
    List<AppOrderApiAudit> findByOrderId(Integer orderId);
    @Query("select a from AppOrderApiAudit a where a.user.orgId in ?1")
    List<AppOrderApiAudit> findAuditAll(List<Long> orgList);
}
