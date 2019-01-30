package com.gmcc.msb.msbsystem.repository.org;

import com.gmcc.msb.msbsystem.entity.org.Org;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrgRepository extends PagingAndSortingRepository<Org, Integer> {

    Iterable<Org> findAllByParentorgid(Integer id);

    Page<Org> findAllByOrgnameContaining(String name, Pageable pageable);

    Iterable<Org> findAllByOrgidIn(Collection<Integer> ids);

    /**
     * 统计子节点数目
     * @param id
     * @return
     */
    int countByParentorgid(Integer id);
}
