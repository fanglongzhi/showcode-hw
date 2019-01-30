package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.entity.AppOrderApiAudit;
import com.gmcc.msb.api.entity.Serv;
import com.gmcc.msb.api.repository.AppOrderApiAuditRepository;
import com.gmcc.msb.api.repository.AppOrderApiRepository;
import com.gmcc.msb.common.property.UserContextHolder;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Zhi Fanglong
 */
@Service
public class AppOrderApiService {

    @Autowired
    AppOrderApiRepository repository;
    @Autowired
    AppOrderApiAuditRepository appOrderApiAuditRepository;
    @Autowired
    ServService servService;


    /**
     * api是否被应用订阅
     */
    public boolean isApiOrderedByApp(Integer apiId) {
        return repository.countAllByApiIdEquals(apiId) > 0;
    }


    public List<AppOrderApi> findAll() {
        return (List<AppOrderApi>) repository.findAll();
    }

    public List<AppOrderApi> findAllByIdIn(List<Integer> ids) {
        return repository.findAllByIdIn(ids);
    }

    public List<Map<String, Object>> getAll(Integer appId) {
        List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
        if (orgList.size() == 0) {

            return Lists.newArrayList();
        }

        if(appId==null) {
            return repository.getAll(orgList);
        }else{
            return repository.getAll(orgList,appId);
        }
    }

    public List<AppOrderApiAudit> getAuditAll() {
        List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
        if (orgList.size() == 0) {

            return Lists.newArrayList();
        }

        List<AppOrderApiAudit> result = appOrderApiAuditRepository.findAuditAll(orgList);

        Map<String, String> cache = new HashMap<>(20);
        if (result != null) {
            for (AppOrderApiAudit api : result) {
                if (cache.containsKey(api.getServiceId())) {
                    api.setServiceName(cache.get(api.getServiceId()));
                } else {
                    Serv one = servService.findOne(api.getServiceId());
                    if (one == null) {
                        cache.put(api.getServiceId(), api.getServiceId());
                    } else {
                        api.setServiceName(one.getServiceName());
                        cache.put(api.getServiceId(), api.getServiceName());
                    }
                }
            }
        }

        return result;
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public AppOrderApi save(AppOrderApi entity) {
        this.setStartDate(entity);
        this.setEndDate(entity);
        return repository.save(entity);
    }

    public AppOrderApi findOne(Integer id) {
        return repository.findOne(id);
    }

    public List<AppOrderApi> findAppIdAndTypeId(Integer appId, Integer typeId) {
        return repository.findAllByAppIdAndApiId(appId, typeId);
    }

    public void batchUnsubscribe(List<Integer> ids) {
        repository.batchUnsubscribe(ids);
    }

    public void deleteList(List<Integer> ids) {
        repository.deleteList(ids);
    }

    /**
     * 设置开始时间为当天00:00:00
     */
    public AppOrderApi setStartDate(AppOrderApi entity) {
        if (entity.getStartDate() != null) {
            Date newDate = DateUtils.truncate(entity.getStartDate(), Calendar.DAY_OF_MONTH);
            entity.setStartDate(newDate);
        }
        return entity;
    }

    /**
     * 设置结束时间为当天的23:59:59
     */
    public AppOrderApi setEndDate(AppOrderApi entity) {
        if (entity.getEndDate() != null) {
            Date newDate = DateUtils.truncate(entity.getEndDate(), Calendar.DAY_OF_MONTH);
            entity.setEndDate(DateUtils.addSeconds(newDate, 86399));
        }
        return entity;
    }


    /**
     * 检查时间是否重叠，同时查询已订阅和申请
     *
     * @return
     */
    public List<AppOrderApi> checkTimeOverlap(AppOrderApi newOrder) {
        return repository.countByTimeOverlap(newOrder.getAppId(), newOrder.getApiId(),
                Arrays.asList(AppOrderApi.ORDERED, AppOrderApi.APPLY),
                newOrder.getStartDate(), newOrder.getEndDate());

    }

    /**
     * 所有已订阅的，并且结束时间大于当前时间
     * @return
     */
    public List<AppOrderApi> getAllAvailable(){
        return repository.findAllByStatusEqualsAndEndDateAfter(AppOrderApi.ORDERED, new Date());
    }
}
