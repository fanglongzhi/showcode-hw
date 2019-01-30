package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.Serv;
import com.gmcc.msb.api.repository.ServRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhi Fanglong
 */
@Service
public class ServService {

    @Autowired
    ServRepository servRepository;

    public Serv findOne(Integer id){
        return this.servRepository.findOne(id);
    }

    public Serv findOne(String serviceId){
        return this.servRepository.findOneByServiceIdEquals(serviceId);
    }


    public Iterable<Serv> findAll(){
        return this.servRepository.findAll();
    }


    public long allServiceCount() {
        return servRepository.count();
    }

}
