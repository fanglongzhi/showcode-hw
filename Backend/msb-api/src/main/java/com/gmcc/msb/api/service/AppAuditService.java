package com.gmcc.msb.api.service;

import com.gmcc.msb.api.common.CommonConstant;
import com.gmcc.msb.api.common.KeyBuilder;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppAudit;
import com.gmcc.msb.api.repository.AppAuditRepository;
import com.gmcc.msb.api.repository.AppRepository;
import com.gmcc.msb.api.vo.request.AuditRequest;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AppAuditService {


    @Autowired
    private AppAuditRepository appAuditRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private RedisService redisService;


    public AppAudit create(App app) {
        AppAudit appAudit = new AppAudit();
        BeanUtils.copyProperties(app, appAudit,"id",
                "appSecret","createTime","updateTime","user","updateBy");
        appAudit.setId(null);
        appAudit.setAppId(app.getId());
        appAudit.setApplyDate(new Date());
        appAudit.setApplyBy(UserContextHolder.getContext().getUserId());
        appAudit.setUpdateTime(appAudit.getApplyDate());
        appAudit.setAppCreateTime(app.getCreateTime());
        return this.appAuditRepository.save(appAudit);
    }

    public Page<AppAudit> findAll(Pageable pageable) {
        List<Long> orgList = UserContextHolder.getContext().getDataOrgList();
        if(orgList.size()==0){

            return new PageImpl<AppAudit>(new ArrayList<>(),pageable,0);
        }
        return this.appAuditRepository.findAllByConditions(orgList,pageable);
    }


    @Transactional
    public App audit(Integer apiAuditId, AuditRequest vo) {

        Assert.notNull(apiAuditId, CommonConstant.ERROR_ID_MISS_CODE);

        Assert.notNull(vo, CommonConstant.ERROR_REQUEST_DATA_CODE);
        Assert.notNull(vo.getResult(), CommonConstant.ERROR_AUDIT_RESULT_MISS_CODE);
        AppAudit apiAudit = this.appAuditRepository.findOne(apiAuditId);
        Assert.notNull(apiAudit, CommonConstant.ERROR_AUDIT_DATA_NOT_EXIST_CODE);

        App app = this.appRepository.findOne(apiAudit.getAppId());
        Assert.notNull(app,CommonConstant.ERROR_APP_NOT_EXIST_CODE);

        if (App.STATUS_AUDITING != app.getStatus()) {
            throw new MsbException(CommonConstant.ERROR_NOT_AUDITING_CODE);
        }
        if (0 == vo.getResult()) {
            app.setStatus(App.STATUS_AVAILABLE);
            app.setAppSecret(KeyBuilder.getKey());
            redisService.saveApp(app);
        } else {
            app.setStatus(App.STATUS_AUDIT_FAIL);
        }

        App saved = this.appRepository.save(app);
        apiAudit.setAuditResult(vo.getResult());
        apiAudit.setAuditDate(new Date());
        apiAudit.setAuditBy(UserContextHolder.getContext().getUserId());
        apiAudit.setUpdateTime(apiAudit.getAuditDate());

        this.appAuditRepository.save(apiAudit);



        return saved;
    }

}
