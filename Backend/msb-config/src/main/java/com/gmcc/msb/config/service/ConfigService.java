package com.gmcc.msb.config.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.config.common.CommonConstant;
import com.gmcc.msb.config.config.MyProperties;
import com.gmcc.msb.config.entity.Config;
import com.gmcc.msb.config.entity.ServiceItem;
import com.gmcc.msb.config.repository.ConfigRepository;
import com.gmcc.msb.config.repository.ServiceRepository;
import com.gmcc.msb.config.utils.RefreshCallable;
import com.gmcc.msb.config.utils.RefreshTask;
import com.gmcc.msb.config.vo.ConfigVo;
import com.gmcc.msb.config.vo.RefreshResultVo;
import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.gmcc.msb.config.common.CommonConstant.ENCRYPTION_FLAG_YES;

@Service
public class ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private MyProperties myProperties;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private TextEncryptor textEncryptor;

    @Value("${spring.profiles.active:local}")
    private String profile;

    @Transactional(rollbackOn = Exception.class)
    public Config createConfig(ConfigVo configVo) {
        configVo.setId(null);
        Config config = new Config(configVo);
        config.setCreateTime(new Date());

        if (ENCRYPTION_FLAG_YES.equals(configVo.getEncryptionFlag())) {
            String propertyValue = textEncryptor.encrypt(configVo.getPropertyValue());
            propertyValue = "{cipher}" + propertyValue;
            config.setPropertyValue(propertyValue);
        }
        return configRepository.save(config);
    }

    public int countConfig(ConfigVo configVo) {
        configVo.setLabel(configVo.getLabel() == null ? "master" : configVo.getLabel());
        return configRepository.countConfig(configVo.getLabel(), configVo.getProfile(), configVo.getApplication(), configVo.getPropertyKey());
    }

    @Transactional(rollbackOn = Exception.class)
    public Config modifyConfig(ConfigVo configVo) {

        if(configRepository.findOne(configVo.getId())==null){
            throw new MsbException("0003-10003");
        }

        if (configVo.getLabel() == null) {
            configVo.setLabel("master");
        }

        List<Config> existConfig = configRepository.findOneConfig(configVo.getLabel(), configVo.getProfile(), configVo.getApplication(), configVo.getPropertyKey());
        if (existConfig != null && !existConfig.isEmpty()) {
            for (Config config : existConfig) {
                if (!config.getId().equals(configVo.getId())) {
                    throw new MsbException(CommonConstant.ERROR_KEY_EXIST, CommonConstant.ERROR_KEY_EXIST_MSG);
                }
            }
        }

        if (ENCRYPTION_FLAG_YES.equals(configVo.getEncryptionFlag())) {
            String propertyValue = textEncryptor.encrypt(configVo.getPropertyValue());
            propertyValue = "{cipher}" + propertyValue;
            configVo.setPropertyValue(propertyValue);
        }
        configRepository.modifyConfig(configVo.getId(), configVo.getPropertyValue(), configVo.getPropertyKey(), configVo.getLabel(), configVo.getProfile());

        return new Config(configVo);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteConfig(Integer id) {
        if(configRepository.findOne(id)==null){
            throw new MsbException("0003-10003");
        }
        configRepository.delete(id);
    }

    public List<Config> findByApplication(String application) {
        Sort sort = new Sort(Sort.Direction.ASC, "propertyKey");

        return configRepository.findByApplication(application, sort);
    }

    public List<Config> findByApplicationAndProfile(String application, String profile) {
        Sort sort = new Sort(Sort.Direction.ASC, "propertyKey");

        return configRepository.findByApplicationAndProfile(application, profile, sort);
    }

    public RefreshResultVo refreshConfig(String application) throws MsbException {

        RefreshResultVo resultVo = new RefreshResultVo();

        List<Map<String, String>> successRes = resultVo.getSuccessList();
        List<Map<String, String>> failRes = resultVo.getFailList();

        List<ServiceInstance> instances = discoveryClient.getInstances(application);

        if (instances != null && !instances.isEmpty()) {
            resultVo.setAmount(instances.size());
            logger.info("需要刷新{}个节点", instances.size());

            //线程同步计数器
            final CountDownLatch latch = new CountDownLatch(instances.size());

            OkHttpClient client = new OkHttpClient()
                                          .newBuilder()
                                          .connectTimeout(myProperties.getRequestRefreshConnectionTime(), TimeUnit.MILLISECONDS)
                                          .readTimeout(myProperties.getRequestRefreshReadTime(), TimeUnit.MILLISECONDS)
                                          .build();
            ExecutorService pool = Executors.newCachedThreadPool();
            List<Future> resList = Lists.newArrayList();

            Integer managementPort = null;
            List<Config> managementPorts = configRepository.findAllByApplicationEqualsAndPropertyKeyEqualsAndProfileEquals(
                    application, "management.port", profile
            );

            if (managementPorts != null && !managementPorts.isEmpty()) {
                try {
                    managementPort = Integer.parseInt(managementPorts.get(0).getPropertyValue());
                } catch (NumberFormatException e) {
                    logger.error("management port ", e);
                }
            }

            for (ServiceInstance instance : instances) {
                RefreshTask task = new RefreshTask(instance, client, myProperties, managementPort);
                Future fut = pool.submit(new RefreshCallable(latch, task));
                resList.add(fut);
            }
            pool.shutdown();
            try {
                latch.await();
                logger.info("刷新完毕");
                for (Future future : resList) {
                    Map<String, String> oneResult = (Map) future.get(1, TimeUnit.SECONDS);

                    if ("0".equals(oneResult.get("code"))) {
                        successRes.add(oneResult);
                    } else {
                        failRes.add(oneResult);
                    }
                }

            } catch (Exception e) {
                logger.error("error is ", e);
            }
        } else {
            throw new MsbException("0003-10017", "未获取到" + application + "微服务信息，无法同步配置");
        }

        return resultVo;
    }


    public ServiceItem findServiceByServiceId(String serviceId) {
        return serviceRepository.findByServiceId(serviceId);
    }

    @Transactional(rollbackOn = Exception.class)
    public void saveServiceItem(ServiceItem serviceItem) {
        ServiceItem si = serviceRepository.findByServiceId(serviceItem.getServiceId());
        if(si!=null){
            si.setServiceName(serviceItem.getServiceName());
            si.setServiceSecret(serviceItem.getServiceSecret());
        }else{
            si = serviceItem;
        }
        serviceRepository.save(si);
    }

    public List<Config> getServiceSignKey(String serviceId, String profile) {
        return this.configRepository.findAllByApplicationEqualsAndPropertyKeyEqualsAndProfileEquals(
                serviceId, myProperties.getSignKeyPropertyName(), profile);
    }

    public Map<String, String> getServiceSignKeys(String profile) {
        List<Config> list = this.configRepository.findAllByPropertyKeyEqualsAndProfileEquals(myProperties.getSignKeyPropertyName(), profile);
        Map<String, String> map = new HashMap<>();
        if (list != null) {
            for (Config config : list) {
                map.put(config.getApplication(), config.getPropertyValue());
            }
        }
        return map;
    }
}
