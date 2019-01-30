package com.gmcc.msb.msbsystem.repository.role;

import com.gmcc.msb.msbsystem.entity.role.Role;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role,Long>,JpaSpecificationExecutor<Role> {
    @Query("select count(1) from Role u where u.roleName = ?1")
    Long countRoleByRoleName(String roleName);
    @Query("select count(1) from Role u where u.id !=?2 and u.roleName = ?1")
    Long countRoleByRoleName(String roleName,Long roleId);
    @Modifying
    @Query("update Role u set u.roleName = ?1,u.updateBy = ?3,u.updateTime = current_timestamp() where u.id = ?2")
    int modifyRoleName(String roleName,Long id,String updateBy);
}
