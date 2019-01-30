package com.gmcc.msb.msbsystem.repository.role;

import com.gmcc.msb.msbsystem.entity.role.UserRole;
import com.gmcc.msb.msbsystem.entity.role.UserRoleKey;
import com.gmcc.msb.msbsystem.entity.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole,UserRoleKey> {
    List<UserRole> findByRoleId(Long roleId);
    @Query("select count(1) from UserRole u where u.roleId = ?1")
    int countUser(Long roleId);
    @Query("select u from UserRole ur,User u where ur.roleId = ?1 and ur.userId = u.id")
    List<User> findUserByRoleId(Long roleId);
}
