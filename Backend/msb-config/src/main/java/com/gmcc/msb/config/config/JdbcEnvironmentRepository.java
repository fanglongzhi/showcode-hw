package com.gmcc.msb.config.config;

import com.gmcc.msb.config.utils.PropertiesResultSetExtractor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import java.util.*;

@ConfigurationProperties("spring.cloud.config.server.jdbc")
@Configuration
public class JdbcEnvironmentRepository implements EnvironmentRepository, Ordered {
    private int order = 2147483637;
    private final JdbcTemplate jdbc;
    private String sql = "SELECT property_key, property_value from t_config where application=? and profile=? and label=?";
    private final PropertiesResultSetExtractor extractor = new PropertiesResultSetExtractor();

    public JdbcEnvironmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return this.sql;
    }

    @Override
    public Environment findOne(String application, String profile, String label) {
        String config = application;
        if (StringUtils.isEmpty(label)) {
            label = "master";
        }

        if (StringUtils.isEmpty(profile)) {
            profile = "default";
        }

        if (!profile.startsWith("default")) {
            profile = "default," + profile;
        }

        String[] profiles = StringUtils.commaDelimitedListToStringArray(profile);
        Environment environment = new Environment(application, profiles, label, (String)null, (String)null);
        if (!application.startsWith("application")) {
            config = "application," + application;
        }

        List<String> applications = new ArrayList(new LinkedHashSet(Arrays.asList(StringUtils.commaDelimitedListToStringArray(config))));
        List<String> envs = new ArrayList(new LinkedHashSet(Arrays.asList(profiles)));
        Collections.reverse(applications);
        Collections.reverse(envs);
        Iterator var9 = applications.iterator();

        while(var9.hasNext()) {
            String app = (String)var9.next();
            Iterator var11 = envs.iterator();

            while(var11.hasNext()) {
                String env = (String)var11.next();
                Map<String, String> next = this.jdbc.query(this.sql, new Object[]{app, env, label}, this.extractor);
                if (!next.isEmpty()) {
                    environment.add(new PropertySource(app + "-" + env, next));
                }
            }
        }

        return environment;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
