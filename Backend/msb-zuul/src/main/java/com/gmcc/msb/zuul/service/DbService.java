package com.gmcc.msb.zuul.service;

import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Yuan Chunhai
 */
@Service
public class DbService {

    public static final String PATH_SEPARATE = "/";
    public static final String PATH_SUFFIX = "**";

    Logger logger = LoggerFactory.getLogger(DbService.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private MsbZuulProperties msbZuulProperties;

    /**
     * 获取路由配置
     */
    public List<ZuulProperties.ZuulRoute> getRoutes() {
        List<ZuulProperties.ZuulRoute> results = null;
        try {
            results =
                    this.jdbcTemplate.query(msbZuulProperties.getRouteSelectSql(),
                            resultSet -> {
                                List<ZuulProperties.ZuulRoute> routes = new ArrayList<>();
                                while (resultSet.next()) {
                                    try {
                                        ZuulProperties.ZuulRoute zuulRoute = getZuulRoute(resultSet);
                                        routes.add(zuulRoute);
                                    } catch (Exception e) {
                                        logger.error("路由配置有误，{}", e.getMessage());
                                    }
                                }
                                return routes;
                            });


            /* 通过path排序，是为了对付这个情况
             *  /route/
             *  /route/1
             *  zuul会优先匹配到/route/，就返回了，不会匹配到下面的
             *  通过path排序，将/route/1排到上面就可以先匹配到/route/1了
             *
             * */
            Collections.sort(results, (o1, o2) -> o2.getPath().compareTo(o1.getPath()));

        } catch (Exception e) {
            logger.error("获取路由配置失败 - {}", e.getMessage());
        }
        return results;
    }

    private ZuulProperties.ZuulRoute getZuulRoute(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String serviceId = resultSet.getString("service_id").trim();
        final String path = resultSet.getString("path");

        ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
        zuulRoute.setId(String.valueOf(id));
        zuulRoute.setServiceId(serviceId);

        // /a, /a/ /a** /a/** /a/**/
        StringBuilder pathStringBuilder = new StringBuilder(path);
        if (!pathStringBuilder.toString().startsWith(PATH_SEPARATE)) {
            pathStringBuilder.insert(0, PATH_SEPARATE);
        }

        if (!pathStringBuilder.toString().endsWith(PATH_SEPARATE) && !path.endsWith(PATH_SUFFIX)) {
            pathStringBuilder.append(PATH_SEPARATE);
        }

        if (pathStringBuilder.toString().endsWith(PATH_SEPARATE)) {
            pathStringBuilder.append(PATH_SUFFIX);
        }

        zuulRoute.setPath(pathStringBuilder.toString());
        return zuulRoute;
    }


    public List<ApiInfo> getApiInfo() {
        return jdbcTemplate.query(msbZuulProperties.getApiSelectSql(), new BeanPropertyRowMapper<>(ApiInfo.class));
    }

}
