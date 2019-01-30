package com.gmcc.msb.api.service;

import com.gmcc.msb.api.common.CommonConstant;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.repository.AppOrderApiRepository;
import com.gmcc.msb.api.repository.AppRepository;
import com.gmcc.msb.api.vo.request.AppRequest;
import com.gmcc.msb.api.vo.request.ModifyAppRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.common.service.client.MsbFluidClient;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Zhi Fanglong
 */

@Service
public class AppService {

    @Autowired
    private AppRepository repository;
    @Autowired
    private AppOrderApiRepository appOrderApiRepository;
    @Autowired
    private AppAuditService appAuditService;
    @Autowired
    @Lazy
    private MsbFluidClient msbFluidClient;

    @Autowired
    private RedisService redisService;

    /**
     * 订阅了api的应用
     */
    public Page<Map<String, Object>> appsOrderApiId(Integer appId, Pageable pageable) {
        return this.repository.appsOrderedApiId(appId, pageable);
    }

    /**
     * 添加一个应用
     * @param param
     * @return
     */
    @Transactional
    public App createApplication(AppRequest param) {
        App app = new App(param);
        app.setAppId(UUID.randomUUID().toString());
        app.setStatus(0);

        return repository.save(app);
    }

    /**
     * 查询已存在的应用名称的数量
     * @param appName
     * @return
     */
    public int countAppName(String appName) {
        return repository.countByAppName(appName);
    }

    public int countAppName(String appName,Integer id){
        return repository.countByAppNameAndIdNot(appName,id);
    }

    /**
     * 查询所有应用
     * @return
     */
    public List<App> findAllApp() {
       List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
       if(orgList.size()==0){
           return Lists.newArrayList();
       }else {
           return repository.findAppByOrgList(UserContextHolder.getContext().getDataOrgList(),
                   new Sort(Sort.Direction.DESC, "createTime"));
       }

    }

    /**
     * 查询应用详情
     * @param id
     * @return
     */
    public App findOneApp(Integer id){
        return repository.findOne(id);
    }

    /**
     * 删除一个应用
     * @param id
     */
    @Transactional
    public void deleteApp(Integer id){
        if(appOrderApiRepository.countByAppId(id)>0){
            throw new MsbException(CommonConstant.ERROR_APP_HAS_SUBSCRIBE_CODE);
        }
        App app = repository.findOne(id);
        if(app==null){
            throw new MsbException(CommonConstant.ERROR_RECORD_NOT_EXIST_CODE);
        }

        if(app.getStatus()==App.STATUS_AUDITING){
            throw new MsbException(CommonConstant.ERROR_APP_AUDITING_CODE);
        }
        if(msbFluidClient.judgeStrategyExist(CommonConstant.APP,id).getContent()){
            throw new MsbException(CommonConstant.ERROR_FLUID_STRATEGY_EXIST);
        }
        repository.delete(id);

        redisService.delApp(app);
    }

    /**
     * 应用申请生效
     * @param appId
     * @return
     */
    @Transactional
    public App applyApp(Integer appId){
        App app = repository.findOne(appId);
        if(app==null){
            throw new MsbException(CommonConstant.ERROR_RECORD_NOT_EXIST_CODE);
        }
        if(app.getStatus()!=App.STATUS_NEW&&app.getStatus()!=App.STATUS_AUDIT_FAIL){
            throw new MsbException(CommonConstant.ERROR_WRONG_STATUS_CODE);
        }
        app.setStatus(App.STATUS_AUDITING);

        App newApp = repository.save(app);

        appAuditService.create(app);

        return newApp;
    }

    public App modifyApp(Integer id, ModifyAppRequest param){
        App app = repository.findOne(id);
        if(app==null){
            throw new MsbException(CommonConstant.ERROR_RECORD_NOT_EXIST_CODE);
        }
        if(app.getStatus()!=App.STATUS_NEW&&app.getStatus()!=App.STATUS_AUDIT_FAIL){
            throw new MsbException(CommonConstant.ERROR_WRONG_STATUS_CODE);
        }
        app.setAppName(param.getAppName());
        app.setCompany(param.getCompany());
        app.setDescription(param.getDescription());
        app.setLinkEmail(param.getLinkEmail());
        app.setLinkMan(param.getLinkMan());
        app.setLinkTel(param.getLinkTel());

        app.setUpdateBy(UserContextHolder.getContext().getUserId());
        app.setUpdateTime(new Date());
        return repository.save(app);
    }

    public List<Map<String,Object>> findApiOfAppSubscribe(Integer id){
        return repository.findApiOfAppSubscribe(id);
    }


    public List<App> findByids(List<Integer> ids) {
        return repository.findAllByIdIn(ids);
    }


    /**
     * 获取所有可用应用列表
     *
     * @return
     */
    public int getAllAvaAppCount() {
        return this.repository.countAllByStatusEquals(App.STATUS_AVAILABLE);
    }

    public List<App> getAllAvaApps() {
        return this.repository.findAllByStatusEquals(App.STATUS_AVAILABLE);
    }
}
