package com.gmcc.msb.msbsystem.repository.org;

import com.gmcc.msb.msbsystem.entity.org.Org;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: msb-system
 * @description: 组织架构DAO层单元测试
 * @author: zhifanglong
 * @create: 2018-10-08 14:35
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class OrgRepositoryTest {
    @Autowired
    private OrgRepository orgRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void countByParentorgidTest(){
        Org org1 = new Org();
        org1.setParentorgid(1);
        entityManager.persist(org1);

        Org org2 = new Org();
        org2.setParentorgid(1);
        entityManager.persist(org2);

        Org org3 = new Org();
        org3.setParentorgid(2);
        entityManager.persist(org3);

       int amount = orgRepository.countByParentorgid(1);

        Assert.assertEquals(2,amount);

    }


}
