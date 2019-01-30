package com.gmcc.msb.msbsystem.repository.module;

import com.gmcc.msb.msbsystem.entity.module.Module;
import com.gmcc.msb.msbsystem.entity.role.RoleAuth;
import com.gmcc.msb.msbsystem.entity.role.UserRole;
import com.gmcc.msb.msbsystem.entity.user.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @program: msb-system
 * @description: Module DAO UT
 * @author: zhifanglong
 * @create: 2018-10-11 15:04
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ModuleReposotoryTests {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ModuleRepository moduleRepository;

    @Test
    public void testCountByKey(){
       Module module = new Module();
       module.setKey("apiOrderCreate");

       entityManager.persist(module);

       int num=moduleRepository.countByKey(module.getKey());

        Assert.assertEquals(1,num);
    }
    @Test
    public void testCountByKey_id(){
        Module module = new Module();
        module.setKey("apiOrderCreate");
        long id1 = entityManager.persist(module).getId();

        Module module2 = new Module();
        module2.setKey("apiOrderCreate");
        long id2 = entityManager.persist(module2).getId();

        int num=moduleRepository.countByKey(module.getKey(),id2);
        Assert.assertEquals(1,num);

    }
    @Test
    public void testCountByModuleName(){
        Module module = new Module();
        module.setKey("apiOrderCreate");
        module.setModuleName("API发布");

        entityManager.persist(module);

        int num=moduleRepository.countByModuleName(module.getModuleName());

        Assert.assertEquals(1,num);
    }

    @Test
    public void testCountByModuleName_id(){
        Module module = new Module();
        module.setKey("apiOrderCreate");
        module.setModuleName("API发布");

        entityManager.persist(module);

        Module module2 = new Module();
        module2.setKey("apiOrderCreate");
        module2.setModuleName("API发布");
        long id2 = entityManager.persist(module2).getId();

        int num=moduleRepository.countByModuleName(module.getModuleName(),id2);

        Assert.assertEquals(1,num);
    }
    @Test
    public void testFindUserByModuleId(){

        User u1 = new User();
        u1.setUserId("1000");
        long id1=entityManager.persist(u1).getId();

        User u2 = new User();
        u2.setUserId("2000");
        long id2=entityManager.persist(u2).getId();

        User u3 = new User();
        u3.setUserId("3000");
        entityManager.persist(u3);

        UserRole ur = new UserRole();
        ur.setUserId(id1);
        ur.setRoleId(2l);

        entityManager.persist(ur);

        UserRole ur2 = new UserRole();
        ur2.setUserId(id2);
        ur2.setRoleId(2l);

        entityManager.persist(ur2);



        RoleAuth ra = new RoleAuth();
        ra.setRoleId(2l);
        ra.setAuthId(1l);

        entityManager.persist(ra);

        List<User> users = moduleRepository.findUserByModuleId(1l);

        Assert.assertNotNull(users);
        Assert.assertEquals(2,users.size());

        boolean hasU1=false,hasU2=false;
        for(User u:users){
            if(u.getId().equals(u1.getId())){
                hasU1=true;
            }
            if(u.getId().equals(u2.getId())){
                hasU2=true;
            }
        }

        Assert.assertTrue(hasU1&&hasU2);



    }
}
