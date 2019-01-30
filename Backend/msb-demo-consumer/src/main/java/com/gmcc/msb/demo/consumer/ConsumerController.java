package com.gmcc.msb.demo.consumer;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: msb-demo-consumer
 * @description: 示例消费者
 * @author: zhifanglong
 * @create: 2018-11-28 11:03
 */
@RestController
public class ConsumerController {
    private static final Logger log = LoggerFactory.getLogger(ConsumerController.class);

    /**
     * 报文头
     */
    private static final String APP_ID="app-id";

    private static final String TOKEN_HEAD="Authorization";

    private static final String SUCCESS="0";

    /**
     * 授权中心地址（获取匿名访问的TOKEN)
     */
    @Value("${msb.authUrl}")
    private String authUrl;

    /**
     * 授权中心TOKEN 验证地址
     */
    @Value("${msb.authCheckUrl}")
    private String authCheckUrl;

    /**
     * 服务提供者的根地址
     */
    @Value("${msb.demonServiceRootUrl}")
    private String serviceRootUrl;

    /**
     *应用ID
     */
    @Value("${appId}")
    private String appId;

    /**
     * 应用密钥
     */
    @Value("${appSecret}")
    private String appSecret;

    /**
     * sso登录验证地址
     */
    @Value("${ssoUrl}")
    private String ssoUrl;



    @Autowired
    private RestTemplate restTemplate;

    /**
     * 匿名访问的示例接口
     * @param name
     * @return
     */
    @GetMapping("/consume/{name}")
    public String sayHello(@PathVariable String name) {
       //1.请求MSB-AUTH服务，获取TOKEN
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.setContentType(MediaType.APPLICATION_JSON);
        authHeaders.add(APP_ID,appId);
        Map<String,String> params = new HashMap<>();
        params.put("app_secret",appSecret);
        Gson gson = new Gson();

        HttpEntity<String> requestEntity = new HttpEntity<>(gson.toJson(params), authHeaders);
        Response result = restTemplate.postForObject(authUrl, requestEntity, Response.class);

        if(SUCCESS.equals(result.getCode())){
            //2.访问服务提供者接口
            String token = (String)result.getContent().get("access_token");
            token="Bearer "+token;

            log.info("token:::"+token);

            HttpHeaders msbHeaders = new HttpHeaders();
            msbHeaders.add(TOKEN_HEAD,token);
            msbHeaders.add(APP_ID,appId);
            HttpEntity<String> requestEntity2 = new HttpEntity<>(msbHeaders);
            ResponseEntity<String> res = restTemplate.exchange(
                    serviceRootUrl+"say_hello/"+name, HttpMethod.GET,requestEntity2,String.class);
            return res.getBody();
        }else{
            return "FAIL:"+result.getCode()+"::"+result.getMessage();
        }

    }

    /**
     * 不可以匿名访问的接口示例
     * @param name
     * @return
     */
    @GetMapping("/consume/{name}/info")
    public String sayHelloPersonal(@PathVariable String name){
      //1.请求SSO服务，获取SSO TOKEN
        String ssoParams="username=user:dwhuzhiwen2&password=2b184554f6214b3d6e87740" +
                "d05be6a97e950344b6c2b63a9f667815f63e1e6a6069ec3c3d7571beba36d5e70" +
                "655d14418669fda06dee030ef533f0bf531ccc71f8cb9666ee8658a9eea3b85b4" +
                "c30a25cc17f11a571b450318a0bf38c27a6216d3cdda5cba7a85b43c94bd0d2bb6" +
                "665a1f386b3c522361e5a63f7d5f0301680a2&grant_type=password&" +
                "client_id=msb_gmcc_net";
        String result = restTemplate.postForObject(ssoUrl+"?"+ssoParams, null, String.class);
        Gson gson = new Gson();
        Map map =gson.fromJson(result,Map.class);
        Object error = map.get("error");
        if(error!=null){
            return result;
        }
        log.info("SSO TOKEN:"+result);
        String accessToken=(String)map.get("access_token");
        //2.在MSB系统验证TOKEN，验证成功MSB会将TOKEN存到REDIS，供后续访问的TOKEN鉴权使用
        HttpHeaders authHeaders = new HttpHeaders();
        authHeaders.add(APP_ID,appId);
        HttpEntity<String> authEntity = new HttpEntity<>(authHeaders);
        Response checkResult = restTemplate.postForObject(authCheckUrl+"?token="+accessToken, authEntity, Response.class);
        if(!SUCCESS.equals(checkResult.getCode())){
            return "FAIL:"+checkResult.getCode()+"::"+checkResult.getMessage();
        }

        //3.访问服务提供者接口
        String token="Bearer "+accessToken;

        log.info("token:::"+token);

        HttpHeaders msbHeaders = new HttpHeaders();
        msbHeaders.add(TOKEN_HEAD,token);
        msbHeaders.add(APP_ID,appId);
        HttpEntity<String> requestEntity = new HttpEntity<>(msbHeaders);
        ResponseEntity<String> res = restTemplate.exchange(
                serviceRootUrl+"say_hello/"+name+"/info", HttpMethod.GET,requestEntity,String.class);
        return res.getBody();
    }
}
