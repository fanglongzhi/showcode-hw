package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.msbsystem.entity.role.RoleAuth;
import com.gmcc.msb.msbsystem.entity.role.UserRole;
import com.gmcc.msb.msbsystem.repository.role.RoleAuthRepository;
import com.gmcc.msb.msbsystem.repository.role.RoleRepository;
import com.gmcc.msb.msbsystem.repository.role.UserRoleRepository;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyRoleAuthParam;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyUserRoleParam;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RoleServiceTest {
    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private RoleAuthRepository roleModuleRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsRoleNameUnique(){
        when(roleRepository.countRoleByRoleName("test")).thenReturn(0L);
        boolean r1 = roleService.isRoleNameUnique("test");
        Assert.assertTrue(r1);

        when(roleRepository.countRoleByRoleName("test2")).thenReturn(1L);
        boolean r2 = roleService.isRoleNameUnique("test2");
        Assert.assertFalse(r2);

    }

    @Test
    public void testIsRoleNameUnique2(){
        when(roleRepository.countRoleByRoleName("test",1l)).thenReturn(0L);
        boolean r1 = roleService.isRoleNameUnique("test",1L);
        Assert.assertTrue(r1);

        when(roleRepository.countRoleByRoleName("test2",1l)).thenReturn(1L);
        boolean r2 = roleService.isRoleNameUnique("test2",1L);
        Assert.assertFalse(r2);

    }

    @Test
    public void testAddAuthList(){
        List<Long> AuthIdList = Lists.newArrayList(1l,2l,3l,4l);
        List<RoleAuth> rmList = Lists.newArrayList();
        rmList.add(buildRM(1l));
        rmList.add(buildRM(2l));

        ModifyRoleAuthParam param = new ModifyRoleAuthParam();
        param.setRoleId(1l);
        param.setAddAuthId(AuthIdList);

        when(roleModuleRepository.findByRoleId(any(Long.class))).thenReturn(rmList);

        List<RoleAuth> result =roleService.addAuthList(param);

        Assert.assertNotNull(result);
        Assert.assertEquals(2l,result.size());

        boolean has3 = false;
        boolean has4 = false;
        for(RoleAuth rm:result){
            if(rm.getAuthId()==3) has3=true;
            if(rm.getAuthId()==4) has4=true;
        }
        Assert.assertTrue(has3);
        Assert.assertTrue(has4);
    }

    private RoleAuth buildRM(Long AuthId){
        RoleAuth r1 = new RoleAuth();
        r1.setAuthId(AuthId);
        r1.setRoleId(1l);
        return r1;
    }

    @Test
    public void testManagerUserRole(){
        List<Long> addUser = Lists.newArrayList(1l,2l,3l,4l);
        List<Long> removeUser = Lists.newArrayList(5l,6l,7l);

        ModifyUserRoleParam param = new ModifyUserRoleParam();
        param.setRoleId(1l);
        param.setAddUserIdList(addUser);
        param.setRemoveUserIdList(removeUser);

        List<UserRole> urList = Lists.newArrayList();
        urList.add(buildUR(1l));
        urList.add(buildUR(2l));
        urList.add(buildUR(3l));
        urList.add(buildUR(7l));

        when(userRoleRepository.findByRoleId(any(Long.class))).thenReturn(urList);

        Map<String,List<UserRole>> result = roleService.managerUserRole(param);
        Assert.assertNotNull(result);
        Assert.assertEquals(1,result.get("add").size());
        Assert.assertEquals(1,result.get("remove").size());

        Assert.assertEquals(4,result.get("add").get(0).getUserId().longValue());
        Assert.assertEquals(7,result.get("remove").get(0).getUserId().longValue());
    }

    private UserRole buildUR(Long userId){
        UserRole ur = new UserRole();
        ur.setRoleId(1l);
        ur.setUserId(userId);

        return ur;
    }
}
