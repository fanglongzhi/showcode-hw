package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.msbsystem.entity.role.Role;
import com.gmcc.msb.msbsystem.entity.role.RoleAuth;
import com.gmcc.msb.msbsystem.entity.role.RoleAuthKey;
import com.gmcc.msb.msbsystem.entity.role.UserRole;
import com.gmcc.msb.msbsystem.entity.user.User;
import com.gmcc.msb.msbsystem.repository.role.RoleAuthRepository;
import com.gmcc.msb.msbsystem.repository.role.RoleCustomerSpec;
import com.gmcc.msb.msbsystem.repository.role.RoleRepository;
import com.gmcc.msb.msbsystem.repository.role.UserRoleRepository;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyRoleAuthParam;
import com.gmcc.msb.msbsystem.vo.req.role.ModifyUserRoleParam;
import com.gmcc.msb.msbsystem.vo.req.role.QueryRoleParam;
import com.gmcc.msb.msbsystem.vo.resp.AuthorityVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    private static final Logger log= LoggerFactory.getLogger(RoleService.class);
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private RoleAuthRepository roleAuthRepository;

    /**
     * 创建一个角色
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createRole(Role role){
        Date createDate = new Date();
        role.setCreateTime(createDate);
        role.setUpdateTime(createDate);
        roleRepository.save(role);

    }

    /**
     * 查找一个Role
     * @param roleId
     * @return
     */
    public Role findRole(Long roleId){
        return roleRepository.findOne(roleId);
    }
    /**
     * 判断角色名称是否重复
     * @param roleName
     * @return
     */
    public boolean isRoleNameUnique(String roleName){
        return roleRepository.countRoleByRoleName(roleName)>0?false:true;

    }

    /**
     * 判断角色名称是否跟除自己之外的其它角色重复
     * @param roleName
     * @param roleId
     * @return
     */
    public boolean isRoleNameUnique(String roleName,Long roleId){

        return roleRepository.countRoleByRoleName(roleName,roleId)>0?false:true;

    }

    /**
     * 判断角色是否赋予了用户
     * @param roleId
     * @return
     */
    public boolean hasUserRole(Long roleId){
        return userRoleRepository.countUser(roleId)>0;
    }

    /**
     * 判断角色是否关联了授权
     * @param roleId
     * @return
     */
    public boolean hasAuth(Long roleId){
        return roleAuthRepository.countAuth(roleId)>0;
    }

    /**
     * 删除一个角色
     * @param roleId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId){
        if(roleRepository.findOne(roleId)==null){
            throw new MsbException("0007-10030");
        }
        roleRepository.delete(roleId);
    }

    /**
     * 查询角色列表
     * @param role
     * @return
     */
    public List<Role> findRoleList(QueryRoleParam role){

       return roleRepository.findAll(RoleCustomerSpec.queryRoleListSpec(role));
    }

    /**
     * 修改角色名称
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyRoleName(Role role){
        if(roleRepository.findOne(role.getId())==null){
            throw new MsbException("0007-10030");
        }
        roleRepository.modifyRoleName(role.getRoleName(),role.getId(), UserContextHolder.getContext().getUserId());
    }

    /**
     * 查询角色下的用户列表
     * @param roleId
     * @return
     */
    public List<User> findUserList(Long roleId){
        return userRoleRepository.findUserByRoleId(roleId);
    }

    /**
     * 查询角色下的权限列表
     * @param roleId
     * @return
     */
    public List<AuthorityVo> findAuthList(Long roleId){
        return roleAuthRepository.findAuthByRoleId(roleId);
    }

    /**
     * 批量添加权限到一个角色下
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<RoleAuth> addAuthList(ModifyRoleAuthParam param){

        List<Long> addAuthList = param.getAddAuthId()==null?Lists.newArrayList():param.getAddAuthId();
        List<RoleAuth> roleAuthList = roleAuthRepository.findByRoleId(param.getRoleId());
        Map<String,List<RoleAuth>> buildData = buildAddAuthList(addAuthList,roleAuthList,param.getRoleId(),new Date());
        List<RoleAuth> addList = buildData.get("add");

        if(!addList.isEmpty()){
            roleAuthRepository.save(addList);
        }

        List<RoleAuth> rmList = buildData.get("delete");

        if(!rmList.isEmpty()){
            roleAuthRepository.delete(rmList);
        }

        return addList;
    }

    /**
     * 删除一个role auth 关系
     * @param roleId
     * @param authId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleModule(Long roleId,Long authId){
        RoleAuthKey roleAuth = new RoleAuthKey();
        roleAuth.setRoleId(roleId);
        roleAuth.setAuthId(authId);

        roleAuthRepository.delete(roleAuth);

    }

    /**
     * 批量删除角色权限
     * @param param
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleModuleBatch(ModifyRoleAuthParam param){

        List<RoleAuth> list = Lists.newArrayList();
        for(Long authId:param.getAddAuthId()){
            RoleAuth ra = new RoleAuth();
            ra.setAuthId(authId);
            ra.setRoleId(param.getRoleId());

            list.add(ra);
        }

        if(!list.isEmpty()) {
            roleAuthRepository.delete(list);
        }

    }

    private Map<String,List<RoleAuth>> buildAddAuthList(List<Long> addAuthIdList, List<RoleAuth> roleAuthList, Long roleId, Date createDate){
        List<RoleAuth> addRoleAuthResult = Lists.newArrayList();
        List<RoleAuth> removeRoleAuthResult = Lists.newArrayList();
        List<Long> addId = Lists.newArrayList(addAuthIdList);
        List<Long> removeId = Lists.newArrayList();
        addAuthIdList.forEach(e->{
            if(roleAuthList!=null) {
                for (RoleAuth ur : roleAuthList) {
                    if (ur.getAuthId().equals(e)) {
                        addId.remove(e);
                    }
                }
            }
        });

        if(roleAuthList!=null){
            roleAuthList.forEach(e->{
                if(!addAuthIdList.contains(e.getAuthId())){
                    removeId.add(e.getAuthId());
                }
            });
        }

        for(Long AuthId:addId){
            RoleAuth rm = new RoleAuth();
            rm.setAuthId(AuthId);
            rm.setRoleId(roleId);
            rm.setCreateTime(createDate);
            rm.setCreateBy(UserContextHolder.getContext().getUserId());
            addRoleAuthResult.add(rm);
        }

        for(Long AuthId:removeId){
            RoleAuth rm = new RoleAuth();
            rm.setAuthId(AuthId);
            rm.setRoleId(roleId);
            removeRoleAuthResult.add(rm);
        }

        Map<String,List<RoleAuth>> result = Maps.newHashMap();
        result.put("add",addRoleAuthResult);
        result.put("delete",removeRoleAuthResult);


        return result;
    }

    /**
     * 批量添加和删除用户到一个角色下
     * @param param
     * @return
     */
    @Transactional
    public Map<String,List<UserRole>> managerUserRole(ModifyUserRoleParam param){

        List<Long> addUserIdList = param.getAddUserIdList()==null? Lists.newArrayList():param.getAddUserIdList();
        List<Long> removeUserIdList = param.getRemoveUserIdList()==null?Lists.newArrayList():param.getRemoveUserIdList();

        List<UserRole> userRoleList = userRoleRepository.findByRoleId(param.getRoleId());

        List<UserRole> addUserRoleList= buildAddUserList(addUserIdList,userRoleList,param.getRoleId(),new Date());
        List<UserRole> removeUserRoleList= buildRemoveUserList(removeUserIdList,userRoleList, param.getRoleId());

        if(addUserRoleList.size()>0){
            userRoleRepository.save(addUserRoleList);
        }

        if(removeUserRoleList.size()>0){
            userRoleRepository.delete(removeUserRoleList);
        }

        Map result = new HashMap();
        result.put("add",addUserRoleList);
        result.put("remove",removeUserRoleList);
        return result;

    }



    private List<UserRole> buildAddUserList(List<Long> addUserIdList,List<UserRole> userRoleList,Long roleId,Date createDate){
        List<UserRole> result = Lists.newArrayList();
        List<Long> addId = Lists.newArrayList(addUserIdList);
        addUserIdList.forEach(e->{
            if(userRoleList!=null) {
                for (UserRole ur : userRoleList) {
                    if (ur.getUserId().equals(e)) {
                        addId.remove(e);
                    }
                }
            }
        });

        for(Long userId:addId){
            result.add(buildUserRole(roleId,userId,createDate));
        }
        return result;
    }

    private List<UserRole> buildRemoveUserList(List<Long> removeUserIdList,List<UserRole> userRoleList,Long roleId){
        List<UserRole> result = Lists.newArrayList();
        List<Long> addId = Lists.newArrayList();
        removeUserIdList.forEach(e->{
            if(userRoleList!=null) {
                for (UserRole ur : userRoleList) {
                    if (ur.getUserId().equals(e)) {
                        addId.add(e);
                    }
                }
            }
        });

        for(Long userId:addId){
            result.add(buildUserRole(roleId,userId,null));
        }
        return result;
    }

    private UserRole buildUserRole(Long roleId,Long userId,Date createDate){
        UserRole userRole = new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        userRole.setCreateTime(createDate);

        return userRole;
    }
}
