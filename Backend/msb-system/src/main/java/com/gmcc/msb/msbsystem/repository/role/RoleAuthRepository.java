package com.gmcc.msb.msbsystem.repository.role;

import com.gmcc.msb.msbsystem.entity.role.RoleAuth;
import com.gmcc.msb.msbsystem.entity.role.RoleAuthKey;
import com.gmcc.msb.msbsystem.vo.resp.AuthorityVo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleAuthRepository extends CrudRepository<RoleAuth,RoleAuthKey> {
    List<RoleAuth> findByRoleId(Long roleId);
    @Query("select count(1) from RoleAuth u where u.roleId = ?1")
    int countAuth(Long roleId);
    @Query("select new com.gmcc.msb.msbsystem.vo.resp.AuthorityVo(" +
            "m.id as moduleId,m.moduleName as moduleName,m.key as key,m.apis as apis," +
            "ra.createTime as createTime,ra.createBy as createBy) " +
            " from RoleAuth ra,Module m where ra.roleId=?1 " +
            "and ra.authId = m.id")
    List<AuthorityVo>findAuthByRoleId(Long roleId);

    /**
     * 查询模块被授予角色的数量
     * @param authId
     * @return
     */
    int countByAuthId(Long authId);
}
