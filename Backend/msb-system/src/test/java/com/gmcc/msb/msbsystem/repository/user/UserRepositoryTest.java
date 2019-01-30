package com.gmcc.msb.msbsystem.repository.user;

import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void countUserTest(){
        List<User> userList = Lists.newArrayList();
        User user1 = new User();
        user1.setOperatorId(100L);
        User user2 = new User();
        user2.setUserId("101");
        User user3 = new User();
        user3.setEmail("test@email.com");
        User user4 = new User();
        user4.setLoginId("999");
        User user5 = new User();
        user5.setMobile("18922768323");
        User user6 = new User();
        user6.setPersonCardNo("619321");

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);
        userList.add(user6);

        userRepository.save(userList);

        long count = userRepository.countUser(100L,"101","999","619321","test@email.com","18922768323");

        Assert.assertEquals(6,count);

    }
    @Test
    public void updateUserStatus(){
        User user = new User();
        user.setStatus(UserStatus.VALID);

        long id = userRepository.save(user).getId();

        int re = userRepository.updateUserStatus(UserStatus.INVALID,id);

        User newUser = userRepository.findOne(id);

        Assert.assertEquals(1,re);
        Assert.assertNotNull(newUser);
        Assert.assertEquals(UserStatus.INVALID,newUser.getStatus());

    }
    @Test
    public void updateUserLock(){
        User user = new User();
        user.setIsLock(false);

        long id = userRepository.save(user).getId();
        int re = userRepository.updateUserLock(true,id);

        User newUser = userRepository.findOne(id);
        Assert.assertEquals(1,re);
        Assert.assertNotNull(newUser);
        Assert.assertTrue(newUser.getIsLock());

    }

}
