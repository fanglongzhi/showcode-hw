package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.ApiGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiGroupRepository extends PagingAndSortingRepository<ApiGroup, Integer>{


    List<ApiGroup> findAllByNameEquals(String name);

    @Query("select a from ApiGroup a where a.user.orgId in ?1")
    Page<ApiGroup> findAllByConditions(List<Long> orgList, Pageable pageable);

}
