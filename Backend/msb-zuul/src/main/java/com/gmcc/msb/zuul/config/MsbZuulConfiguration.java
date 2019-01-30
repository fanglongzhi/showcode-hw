package com.gmcc.msb.zuul.config;

import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.entity.AppOrderApi;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.route.CustomRouteLocator;
import com.netflix.config.*;
import com.netflix.config.sources.JDBCConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Set;

/**
 * @author Yuan Chunhai
 */
@Configuration
public class MsbZuulConfiguration {

    @Autowired
    private MsbZuulProperties msbZuulProperties;
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initDynamicConfig() {
        PolledConfigurationSource source = new JDBCConfigurationSource(dataSource, msbZuulProperties.getBreakerSelectSql(),
                msbZuulProperties.getBreakerKey(), msbZuulProperties.getBreakerValue());
        AbstractPollingScheduler scheduler = new FixedDelayPollingScheduler();
        DynamicConfiguration configuration = new DynamicConfiguration(source, scheduler);
        ConfigurationManager.install(configuration);
    }

    @Bean
    public CustomRouteLocator routeLocator(ZuulProperties zuulProperties, ServerProperties serverProperties) {
        return new CustomRouteLocator(serverProperties.getServletPrefix(), zuulProperties);
    }

    @Bean
    public RibbonCommandFactory ribbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties, Set<ZuulFallbackProvider> zuulFallbackProviders) {
        return new MyRibbonCommandFactory(clientFactory, zuulProperties,
                zuulFallbackProviders);
    }

    @Bean("apiInfoRedisTemplate")
    public RedisTemplate<String, ApiInfo> apiInfoRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, ApiInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ApiInfo.class));

        return redisTemplate;
    }

    @Bean("appInfoRedisTemplate")
    public RedisTemplate<String, App> appInfoRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, App> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(App.class));

        return redisTemplate;
    }

    @Bean("appOrderApiRedisTemplate")
    public RedisTemplate<String, AppOrderApi> appOrderApiRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, AppOrderApi> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(AppOrderApi.class));

        return redisTemplate;
    }


    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        // CORS 配置对所有接口都有效
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }


    @Bean(name = "ssoPublicKey")
    public RSAPublicKey rasPublicKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(msbZuulProperties.getRasPublikKey().getBytes()));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}
