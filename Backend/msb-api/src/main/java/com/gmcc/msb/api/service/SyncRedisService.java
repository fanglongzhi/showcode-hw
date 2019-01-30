package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.entity.Serv;
import com.gmcc.msb.api.vo.ApiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;

/**
 * @author Yuan Chunhai
 * <p>
 * 同步redis
 */
@Service
public class SyncRedisService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncRedisService.class);

    @Autowired
    private ApiService apiService;

    @Autowired
    private ServService servService;

    @Autowired
    RedisService redisService;

    @Autowired
    AppService appService;

    @Autowired
    AppOrderApiService appOrderApiService;

    /**
     * 同步api信息
     */
    @Transactional(rollbackOn = Exception.class)
    public void syncApiInfo() {
        Iterable<Serv> all = servService.findAll();
        if (all != null) {
            Iterator<Serv> servIt = all.iterator();
            while (servIt.hasNext()) {
                Serv serv = servIt.next();
                LOGGER.info("同步服务{}下的API", serv.getServiceId());
                Iterable<API> allApis = apiService.findAllOnlineApisByServiceId(serv.getServiceId());
                this.redisService.deleteAllApiInfo(serv.getServiceId());
                if (allApis != null) {
                    Iterator<API> apiIt = allApis.iterator();
                    while (apiIt.hasNext()) {
                        ApiInfo apiinfo = new ApiInfo();
                        BeanUtils.copyProperties(apiIt.next(), apiinfo);
                        this.redisService.addApiInfo(apiinfo);
                    }
                }
            }
        }
    }


    public void syncApps() {
        List<App> apps = appService.getAllAvaApps();
        redisService.delAllApps();
        for (App app : apps ){
            redisService.saveApp(app);
        }
    }

    public void syncAppOrderApis() {

        List<App> apps = appService.getAllAvaApps();
        for (App app : apps){
            redisService.removeAllAppOrderApi(app);
        }

        List<AppOrderApi> appOrderApis = appOrderApiService.getAllAvailable();
        for (AppOrderApi appOrderApi : appOrderApis){
            redisService.addAppOrderApi(appOrderApi);
        }
    }

}
