package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApiAudit;
import com.gmcc.msb.api.repository.ApiRepository;
import com.gmcc.msb.api.repository.AppOrderApiAuditRepository;
import com.gmcc.msb.api.repository.AppRepository;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppOrderApiAuditService {

    @Autowired
    private AppOrderApiAuditRepository repository;
    @Autowired
    private AppRepository appRepository;
    @Autowired
    private ApiRepository apiRepository;

    public AppOrderApiAudit save(AppOrderApiAudit entity){
        App app = appRepository.findOne(entity.getAppId());
        if(app==null){
            throw new MsbException("0001-10014");
        }
        BeanUtils.copyProperties(app, entity,"id",
                "appSecret","createTime","updateTime","user","updateBy");

        API api = apiRepository.findOne(entity.getApiId());
        if(api==null){
            throw new MsbException("0001-10013");
        }
        BeanUtils.copyProperties(api, entity,"id",
                "appSecret","createTime","updateTime","user","updateBy");

        entity.setAppCreateTime(app.getCreateTime());
        entity.setApplyBy(UserContextHolder.getContext().getUserId());
        entity.setApplyDate(new Date());
        return repository.save(entity);
    }

    public AppOrderApiAudit update(AppOrderApiAudit entity){
        entity.setAuditBy(UserContextHolder.getContext().getUserId());
        return repository.save(entity);
    }



    public List<AppOrderApiAudit> findAll(){

        return (List<AppOrderApiAudit>) repository.findAll();
    }

    public void delete(int id){
        repository.delete(id);
    }


    public AppOrderApiAudit findOne(Integer id){
        return repository.findOne(id);
    }

    public List<AppOrderApiAudit> findByOrderIdAndStatus(Integer id){
        return repository.findByOrderId(id);
    }


}
