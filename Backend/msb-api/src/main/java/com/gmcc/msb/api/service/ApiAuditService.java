package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiAudit;
import com.gmcc.msb.api.entity.Serv;
import com.gmcc.msb.api.repository.ApiAuditRepository;
import com.gmcc.msb.common.property.UserContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ApiAuditService {

    @Autowired
    private ApiAuditRepository reporitory;

    @Autowired
    private ServService servService;


    public ApiAudit create(API api, Integer type) {
        ApiAudit apiAudit = new ApiAudit();
        BeanUtils.copyProperties(api, apiAudit,"id",
                "appSecret","createTime","updateTime","user","updateBy");
        apiAudit.setId(null);
        apiAudit.setApiId(api.getId());
        apiAudit.setApplyDate(new Date());
        apiAudit.setApplyType(type);
        apiAudit.setUpdateTime(apiAudit.getApplyDate());
        apiAudit.setApplyBy(UserContextHolder.getContext().getUserId());
        return this.reporitory.save(apiAudit);
    }

    public ApiAudit save(ApiAudit apiAudit) {
        return this.reporitory.save(apiAudit);
    }

    public ApiAudit findOne(Integer id) {
        return this.reporitory.findOne(id);
    }


    public Page<ApiAudit> findAll(Pageable pageable) {
        List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
        if(orgList.size()==0){

            return new PageImpl<ApiAudit>(new ArrayList<>(),pageable,0);
        }

        Page<ApiAudit> page = this.reporitory.findAllByConditions(orgList,pageable);

        Map<String, String> cache = new HashMap<>(20);
        if (page.getContent() != null) {
            for (ApiAudit api : page.getContent()) {
                if (cache.containsKey(api.getServiceId())) {
                    api.setServiceName(cache.get(api.getServiceId()));
                } else {
                    Serv one = this.servService.findOne(api.getServiceId());
                    if (one == null) {
                        cache.put(api.getServiceId(), api.getServiceId());
                    } else {
                        api.setServiceName(one.getServiceName());
                        cache.put(api.getServiceId(), api.getServiceName());
                    }
                }
            }
        }
        return page;
    }


}
