package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.Serv;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Zhi Fanglong
 */
@Repository
public interface ServRepository extends PagingAndSortingRepository<Serv, Integer> {

    Serv findOneByServiceIdEquals(String serviceId);

}
