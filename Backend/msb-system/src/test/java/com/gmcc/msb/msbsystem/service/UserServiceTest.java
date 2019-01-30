package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.msbsystem.common.PageInfo;
import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.common.resp.Result;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.repository.user.UserRepository;
import com.gmcc.msb.msbsystem.vo.req.user.QueryUserParam;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @InjectMocks
    private UserService userService = new UserService();
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveUser() throws Exception{
        User user = new User();
        user.setOperatorId(1l);
        user.setPersonCardNo("11");
        user.setMobile("11");
        user.setLoginId("11");
        user.setEmail("11");
        user.setUserId("1l");
        when(userRepository.countUser(anyLong(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(0l);
        when(userRepository.save(any(User.class))).thenReturn(user);

        Result<User> result = userService.saveUser(user);

        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());
        Assert.assertEquals("success", result.getMessage());
    }

    @Test(expected = MsbException.class)
    public void testSaveUser_exist() throws MsbException {
        User user = new User();
        user.setOperatorId(1l);
        user.setPersonCardNo("11");
        user.setMobile("11");
        user.setLoginId("11");
        user.setEmail("11");
        user.setUserId("1l");
        when(userRepository.countUser(anyLong(),anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(1l);
        Result<User> result = userService.saveUser(user);
    }

    @Test
    public void testFindOneUser() {
      User user = new User();
      user.setOperatorId(1L);
      user.setName("test");

      when(userRepository.findOne(eq(1l))).thenReturn(user);

      Result<User> result = userService.findOneUser(user.getOperatorId());
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());
        Assert.assertNotNull(result.getContent());
        Assert.assertEquals(Long.valueOf(1),result.getContent().getOperatorId() );
    }

    @Test
    public void testFindUserListPage(){
        QueryUserParam param = new QueryUserParam();
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageSize(10);
        pageInfo.setCurrentPage(0);
        param.setPageInfo(pageInfo);

        Result<Page<User>> result = userService.findUserListPage(param);
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());
    }

    @Test
    public void testFindUserList(){
        QueryUserParam param = new QueryUserParam();

        Result<List<User>> result = userService.findUserList(param);
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());
    }
    @Test
    public void testModifyUserLock(){
        QueryUserParam.ModifyUserParam param = new QueryUserParam.ModifyUserParam();
        param.setId(1l);
        param.setIsLock(true);


        Result<Page<User>> result = userService.modifyUserLock(param);
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());
    }

    @Test
    public void testModifyUserStatus(){
        QueryUserParam.ModifyUserParam param = new QueryUserParam.ModifyUserParam();
        param.setId(1l);
        param.setStatus(UserStatus.VALID);
        Result<Page<User>> result = userService.modifyUserStatus(param);
        Assert.assertNotNull(result);
        Assert.assertEquals("0", result.getCode());
    }

}
