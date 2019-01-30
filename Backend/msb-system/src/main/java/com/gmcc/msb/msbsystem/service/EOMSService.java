package com.gmcc.msb.msbsystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.entity.org.EOMSRequestEntity;
import com.gmcc.msb.msbsystem.entity.org.Org;
import com.gmcc.msb.msbsystem.property.AppProperties;
import com.gmcc.msb.msbsystem.util.EOMSAESCoderForCommon;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class EOMSService {

    private static final Logger logger = LoggerFactory.getLogger(EOMSService.class);

    @Autowired
    private AppProperties properties;

    @Autowired
    private OrgService orgService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sync(String fields) {
        OkHttpClient client = new OkHttpClient()
                                      .newBuilder()
                                      .connectTimeout(properties.getReqEomsConnectionTime(), TimeUnit.MILLISECONDS)
                                      .readTimeout(properties.getReqEomsReadTime(), TimeUnit.MILLISECONDS)
                                      .build();


        String url = properties.getEomsServiceUrl();
        if (StringUtils.isEmpty(url)) {
            throw new MsbException("0007-10010");
        }

        EOMSRequestEntity requestEntity = new EOMSRequestEntity();
        requestEntity.setOperation("ZHZX_ALL_ORGS");
        Map<String, String> data = new HashMap<>();
        data.put("fields", fields);
        requestEntity.setData(data);
        String reqStr;
        try {
            reqStr = objectMapper.writeValueAsString(requestEntity);
        } catch (JsonProcessingException e) {
            logger.error("请求eoms参数错误", e);
            throw new MsbException("0007-10011", e.getMessage());
        }

        String encodeStr;
        try {
            logger.debug("请求参数{}", reqStr);
            encodeStr = EOMSAESCoderForCommon.encryptUTF8(reqStr);
            logger.debug("加密后的请求参数{}", encodeStr);
        } catch (Exception e) {
            logger.error("加密请求参数错误", e);
            throw new MsbException("0007-10012", e.getMessage());
        }

        Request request = new Request.Builder()
                                  .url(url)
                                  .post(RequestBody.create(
                                          MediaType.parse("text/html; charset=utf-8"),
                                          encodeStr))
                                  .build();
        Response response = null;
        try {
            logger.info("请求 {} 同步组织架构", url);
            response = client.newCall(request).execute();
            int code = response.code();
            String respStr = response.body().string();
            if (code != 200) {
                logger.error("同步组织架构，{}, {}", code, respStr);
                throw new MsbException("0007-10013", "错误码" + code);
            }
            saveRespsonseData(respStr, fields);
            logger.info("请求 {} 同步组织架构成功", url);
        } catch (IOException e) {
            logger.error("同步组织架构失败，{}", ExceptionUtils.getMessage(e));
            throw new MsbException("0007-10014");
        }
    }


    private void saveRespsonseData(String respStr, String fields) {
        Map<String, Object> map;
        try {
            map = objectMapper.readValue(respStr, Map.class);
        } catch (IOException e) {
            throw new MsbException("0007-10015");
        }

        String rtCode = (String) map.get("rtCode");

        if ("000".equals(rtCode)) {
            Map dataMap = (Map) map.get("rtData");
            if (dataMap != null) {
                List list = (List) dataMap.get("orgList");
                saveData(list, fields);
            } else {
                throw new MsbException("0007-10016");
            }
        } else {
            throw new MsbException("0007-10017", "错误码" + rtCode);
        }
    }

    private void saveData(List list, String fields) {
        List<Org> saveList = new ArrayList<>();
        String[] fieldArr = StringUtils.split(",");
        if (list != null && !list.isEmpty()) {
            for (Object object : list) {
                Map<String, String> data = (Map) object;
                Org org = new Org();
                if (data.containsKey("ORGID")) {
                    org.setOrgid(getInt(data, "ORGID"));
                }
                if (data.containsKey("ORGCODE")) {
                    org.setOrgcode(data.get("ORGCODE"));
                }
                if (data.containsKey("ORGNAME")) {
                    org.setOrgname(data.get("ORGNAME"));
                }
                if (data.containsKey("PARENTORGID")) {
                    org.setParentorgid(getInt(data, "PARENTORGID"));
                }
                if (data.containsKey("ORGADDR")) {
                    org.setOrgaddr(data.get("ORGADDR"));
                }

                saveList.add(org);
            }
        }
        this.orgService.saveAllFromSync(saveList);
    }

    private Integer getInt(Map<String, String> data, String key) {

        if (data.containsKey(key)) {
            String value = data.get(key);
            if (!StringUtils.isEmpty(value)) {
                return Integer.parseInt(value);
            }
        }

        return null;
    }

}
