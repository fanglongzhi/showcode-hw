package com.gmcc.msb.config.controller;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.service.client.MsbServiceClient;
import com.gmcc.msb.config.common.CommonConstant;
import com.gmcc.msb.config.common.Result;
import com.gmcc.msb.config.entity.Config;
import com.gmcc.msb.config.entity.ServiceItem;
import com.gmcc.msb.config.service.ConfigService;
import com.gmcc.msb.config.service.SignKeyRefreshService;
import com.gmcc.msb.config.vo.ConfigVo;
import com.gmcc.msb.config.vo.RefreshResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ConfigController {
    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);
    @Autowired
    private ConfigService configService;
    @Value("${spring.profiles.active:local}")
    private String profile;
    @Autowired
    @Lazy
    private MsbServiceClient serviceClient;

    @Autowired
    @Lazy
    private SignKeyRefreshService signKeyRefreshService;

    @PostMapping("/serviceConfig")
    public Result createServiceConfig(@RequestBody @Valid ConfigVo configVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG, bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_MSG);
        }

        if (configService.countConfig(configVo) > 0) {
            throw new MsbException(CommonConstant.ERROR_KEY_EXIST, CommonConstant.ERROR_KEY_EXIST_MSG);
        }

        return Result.success(configService.createConfig(configVo));
    }

    @GetMapping("/serviceConfig/profile")
    public Result findSystemProfile() {
        return Result.success(profile);
    }

    @PutMapping("/serviceConfig/{id}")
    public Result modifyServiceConfig(@PathVariable("id") Integer id, @RequestBody @Valid ConfigVo configVo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(CommonConstant.ERROR_VALIDATE_LOG_MSG, bindingResult.getFieldErrors());
            throw new MsbException(CommonConstant.ERROR_VALIDATE_CODE, CommonConstant.ERROR_VALIDATE_LOG_MSG);
        }

        configVo.setId(id);

        return Result.success(configService.modifyConfig(configVo));
    }

    @GetMapping("/application/{application}/serviceConfig")
    public Result findServiceConfigForApplication(@PathVariable("application") String application) {
        return Result.success(configService.findByApplication(application));
    }

    @GetMapping("/application/{application}/{profile}/serviceConfig")
    public Result findServiceConfigForApplicationByProfile(
            @PathVariable("application") String application,
            @PathVariable("profile") String profile) {
        return Result.success(configService.findByApplicationAndProfile(application, profile));
    }

    @DeleteMapping("/serviceConfig/{id}")
    public Result deleteServiceConfig(@PathVariable("id") Integer id) {
        configService.deleteConfig(id);

        return Result.success();
    }

    @PostMapping("/{application}/serviceConfig/refresh")
    public Result refresh(@PathVariable("application") String application) throws MsbException {
        RefreshResultVo resultVo = configService.refreshConfig(application);

        Result result = Result.success();

        if (resultVo.getFailList().size() > 0) {
            result = Result.fail("0003-10002","刷新失败");
        } else {
            try {
                serviceClient.updateRefreshData(application);
            } catch (Exception e) {
                log.error("更新刷新时间失败", e);
            }
            try {
                signKeyRefreshService.refresh(application);
            } catch (Exception e) {
                log.error("刷新签名key失败", e    );
            }
        }

        result.setContent(resultVo);

        return result;
    }

    @PostMapping("/serviceConfig/serviceItem")
    public Result syncServiceItemInfo(@RequestBody ServiceItem serviceItem) {
        configService.saveServiceItem(serviceItem);

        return Result.success();
    }


    @GetMapping("/serviceConfig/signKeyConfig")
    public Result<String> getServiceSignKeys(@RequestParam("serviceId") String serviceId,
                                             @RequestParam("profile") String profile) {
        String key = serviceId;
        List<Config> list = configService.getServiceSignKey(serviceId, profile);
        if (list != null && !list.isEmpty()) {
            key = list.get(0).getPropertyValue();
        }
        return Result.success(key);
    }

    @GetMapping("/serviceConfig/signKeyConfigs")
    public Result<Map<String, String>> getServiceSignKeys(@RequestParam("profile") String profile) {
        Map<String, String> map = configService.getServiceSignKeys(profile);
        return Result.success(map);
    }


}

