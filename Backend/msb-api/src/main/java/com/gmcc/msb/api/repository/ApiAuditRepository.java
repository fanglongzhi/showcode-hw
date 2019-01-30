package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.ApiAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiAuditRepository extends PagingAndSortingRepository<ApiAudit, Integer> {
    @Query("select a from ApiAudit a where a.user.orgId in ?1")
    Page<ApiAudit> findAllByConditions(List<Long> orgList, Pageable pageable);
}
