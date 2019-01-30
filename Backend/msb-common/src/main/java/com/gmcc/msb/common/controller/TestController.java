package com.gmcc.msb.common.controller;

import com.gmcc.msb.common.property.MsbProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 12/7/2018-6:41 PM
 */
@RestController(value = "testCC")
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
public class TestController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MsbProperties msbProperties;

    @RequestMapping("/redis_test_get")
    public String redisTestGet(@RequestParam(name = "size", defaultValue = "100") int size) {

        if (!msbProperties.isEnabledTests()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("redis 查询").append('\n');

        stringBuilder.append("address:\n");
        stringBuilder.append(((JedisConnectionFactory) stringRedisTemplate.getConnectionFactory()).getHostName())
                .append('\n');
        stringBuilder.append("client list:\n");
        stringRedisTemplate.getClientList().forEach((v) -> {
            stringBuilder.append("\t").append(v.getAddressPort()).append("\n");
        });

        stringRedisTemplate.opsForValue().set("test", "1");
        long begin = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            String txt = "some str " + i;
            stringRedisTemplate.opsForValue().get("test");
        }
        long end = System.currentTimeMillis();

        long elapsed = end - begin;
        double avg = (double) elapsed / size;
        stringBuilder.append("总请求：" + size + "，总耗时：" + elapsed + "，平均耗时：" + avg)
                .append("\n");

        return stringBuilder.toString();
    }


    @RequestMapping("/redis_test_insert")
    public String test(@RequestParam(name = "size", defaultValue = "100") int size) {
        if (!msbProperties.isEnabledTests()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("redis 插入").append('\n');

        stringBuilder.append("address:\n");
        stringRedisTemplate.getClientList().forEach((v) -> {
            stringBuilder.append("\t").append(v.getAddressPort()).append("\n");
        });


        long begin = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            String txt = "some str " + i;
            stringRedisTemplate.boundListOps("test:list").leftPush(txt);
        }
        long end = System.currentTimeMillis();
        long elapsed = end - begin;
        double avg = (double) elapsed / size;
        stringBuilder.append("总请求：" + size + "，总耗时：" + elapsed + "，平均耗时：" + avg)
                .append("\n");

        return stringBuilder.toString();
    }


    @RequestMapping("/mysql_test_query")
    public String testMysql(@RequestParam(name = "size", defaultValue = "100") int size) {
        if (!msbProperties.isEnabledTests()) {
            return "";
        }
        long begin = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            String txt = "some str " + i;
            jdbcTemplate.query(msbProperties.getTestQuerySql(), new RowMapper<Object>() {
                @Override
                public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                    while (rs.next()) {
                        List<String> fields = msbProperties.getTestQuerySqlResultFileds();
                        for (String field : fields) {
                            rs.getString(field);
                        }
                    }
                    return null;
                }
            });
        }
        long end = System.currentTimeMillis();

        long elapsed = end - begin;
        double avg = (double) elapsed / size;
        return "总请求：" + size + "，总耗时：" + elapsed + "，平均耗时：" + avg;
    }


    @RequestMapping("/mysql_test_insert")
    public String testMysqlInsert(@RequestParam(name = "size", defaultValue = "100") int size) {
        if (!msbProperties.isEnabledTests()) {
            return "";
        }
        long begin = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            String txt = "some str " + i;
            jdbcTemplate.update(msbProperties.getTestInsertSql());
        }
        long end = System.currentTimeMillis();

        long elapsed = end - begin;
        double avg = (double) elapsed / size;
        return "总请求：" + size + "，总耗时：" + elapsed + "，平均耗时：" + avg;
    }


}
