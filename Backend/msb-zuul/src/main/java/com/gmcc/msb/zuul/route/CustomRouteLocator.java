package com.gmcc.msb.zuul.route;

import com.gmcc.msb.zuul.service.DbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动态路由
 * @author Yuan Chunhai
 */
public class CustomRouteLocator extends SimpleRouteLocator {

    public static final Logger log = LoggerFactory.getLogger(CustomRouteLocator.class);

    public CustomRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    private List<ZuulProperties.ZuulRoute> cache = new ArrayList<>();

    @Autowired
    DbService dbService;

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        log.info("{}", "刷新路由");
        Map<String, ZuulProperties.ZuulRoute> routes = super.locateRoutes();

        List<ZuulProperties.ZuulRoute> r = dbService.getRoutes();
        if (r != null) {
            cache = r;
            log.info("刷新路由({})", cache.size());
            cache.forEach(res ->
                                  log.info("添加路由, {} {} {}", res.getId(), res.getServiceId(), res.getPath())
            );
        }
        cache.forEach(res ->
                              routes.put(res.getPath(), res)
        );

        return routes;
    }

    /**
     * 刷新的方法
     */
    public void refresh() {
        doRefresh();
    }

}
