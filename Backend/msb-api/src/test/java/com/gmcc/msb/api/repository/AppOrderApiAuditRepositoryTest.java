package com.gmcc.msb.api.repository;

import com.gmcc.msb.api.entity.AppOrderApiAudit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppOrderApiAuditRepositoryTest {
    @Autowired
    private AppOrderApiAuditRepository appOrderApiAuditRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByOrderIdAndStatus(){

        int appId = 1;
        int typeId = 11;
        int status = 0;
        int orderId = 1;
        AppOrderApiAudit Audit = new AppOrderApiAudit();
        Audit.setOrderId(orderId);
        Audit.setAppId(appId);
        Audit.setApiId(typeId);
        Integer id = entityManager.persist(Audit).getId();

        List<AppOrderApiAudit> list = appOrderApiAuditRepository.findByOrderId(orderId);

        assertNotNull(list );
        assertTrue(list.size()>0);
        assertEquals(list.get(0).getId(),id);

    }
}
