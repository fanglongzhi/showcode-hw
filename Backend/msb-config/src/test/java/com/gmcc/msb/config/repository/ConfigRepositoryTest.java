package com.gmcc.msb.config.repository;

import com.gmcc.msb.config.entity.Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ConfigRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ConfigRepository configRepository;

    @Test
    public void findByApplicationTest(){
        Config config = new Config();
        config.setApplication("msb-test");
        config.setProfile("local");
        config.setLabel("master");
        config.setPropertyKey("a");
        config.setPropertyValue("avalue");

        configRepository.save(config);

        Config config2 = new Config();
        config2.setApplication("msb-test");
        config2.setProfile("local");
        config2.setLabel("master");
        config2.setPropertyKey("b");
        config2.setPropertyValue("bvalue");

        configRepository.save(config2);

        Config config3 = new Config();
        config3.setApplication("msb-test2");
        config3.setProfile("local");
        config3.setLabel("master");
        config3.setPropertyKey("b");
        config3.setPropertyValue("bvalue");

        configRepository.save(config3);

        List<Config> result = configRepository.findByApplication("msb-test",new Sort(Sort.Direction.ASC,"propertyKey"));
        Assert.assertNotNull(result);
        Assert.assertEquals(2,result.size());
        Assert.assertEquals("a",result.get(0).getPropertyKey());
        Assert.assertEquals("b",result.get(1).getPropertyKey());

    }

    @Test
    public void findByApplicationAndProfileTest(){
        Config config = new Config();
        config.setApplication("msb-test-profile");
        config.setProfile("local");
        config.setLabel("master");
        config.setPropertyKey("a");
        config.setPropertyValue("avalue");

        configRepository.save(config);

        Config config2 = new Config();
        config2.setApplication("msb-test-profile");
        config2.setProfile("local");
        config2.setLabel("master");
        config2.setPropertyKey("b");
        config2.setPropertyValue("bvalue");

        Config config9 = new Config();
        config9.setApplication("msb-test-profile");
        config9.setProfile("test");
        config9.setLabel("master");
        config9.setPropertyKey("c");
        config9.setPropertyValue("cvalue");

        configRepository.save(config2);

        Config config3 = new Config();
        config3.setApplication("msb-test2-profile");
        config3.setProfile("local");
        config3.setLabel("master");
        config3.setPropertyKey("b");
        config3.setPropertyValue("bvalue");

        configRepository.save(config3);

        List<Config> result = configRepository.findByApplicationAndProfile("msb-test-profile","local",new Sort(Sort.Direction.ASC,"propertyKey"));
        Assert.assertNotNull(result);
        Assert.assertEquals(2,result.size());
        Assert.assertEquals("a",result.get(0).getPropertyKey());
        Assert.assertEquals("b",result.get(1).getPropertyKey());

    }
    @Test
    public void countConfigTest(){
        Config config = new Config();
        config.setApplication("msb-test3");
        config.setProfile("local");
        config.setLabel("master");
        config.setPropertyKey("a");
        config.setPropertyValue("avalue");

        configRepository.save(config);

        int count = configRepository.countConfig("master","local","msb-test3","a");
        Assert.assertEquals(1,count);

    }
    @Test
    public void modifyConfigTest(){
        Config config = new Config();
        config.setApplication("msb-test4");
        config.setProfile("local");
        config.setLabel("master");
        config.setPropertyKey("a");
        config.setPropertyValue("avalue");

        Integer id =configRepository.save(config).getId();

        configRepository.modifyConfig(id,"newvalue","newa","develop","test");

        Config newConfig=configRepository.findOne(id);

        Assert.assertNotNull(newConfig);
        Assert.assertEquals("newvalue",newConfig.getPropertyValue());
        Assert.assertEquals("newa",newConfig.getPropertyKey());
        Assert.assertEquals("develop",newConfig.getLabel());
        Assert.assertEquals("test",newConfig.getProfile());

    }



}
