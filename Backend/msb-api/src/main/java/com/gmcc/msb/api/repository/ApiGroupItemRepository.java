package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.ApiGroupItem;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApiGroupItemRepository extends PagingAndSortingRepository<ApiGroupItem, Integer> {

    long countAllByApiIdEquals(Integer appId);

    @Modifying
    @Transactional
    @Query("delete from ApiGroupItem where apiId = ?1")
    void deleteByApiId(Integer appId);

    Iterable<ApiGroupItem> findAllByGroupIdEquals(Integer groupId);

    long countAllByGroupIdEquals(Integer groupId);

    void deleteAllByGroupIdEquals(Integer groupId);
}
