package com.gmcc.msb.config.repository;

import com.gmcc.msb.config.entity.ServiceItem;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<ServiceItem,Integer>{
    ServiceItem findByServiceId(String serviceId);
}
