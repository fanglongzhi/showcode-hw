package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.AppAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppAuditRepository extends PagingAndSortingRepository<AppAudit, Integer>{
    @Query("select a from AppAudit a where a.user.orgId in ?1")
    Page<AppAudit> findAllByConditions(List<Long> orgList, Pageable pageable);
}
