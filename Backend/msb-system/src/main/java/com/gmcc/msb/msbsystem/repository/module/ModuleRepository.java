package com.gmcc.msb.msbsystem.repository.module;

import com.gmcc.msb.msbsystem.entity.module.Module;
import com.gmcc.msb.msbsystem.entity.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @program: msb-system
 * @description: Module DAO
 * @author: zhifanglong
 * @create: 2018-10-11 10:47
 */
public interface ModuleRepository extends PagingAndSortingRepository<Module,Long> {
    /**
     * 查询特定key是否已经存在
     * @param key
     * @return
     */
    int countByKey(String key);

    /**
     * 查询排除特定模块的KEY是否已经存在
     * @param key
     * @param id
     * @return
     */
    @Query("select count(1) from Module m where m.id!=?2 and key=?1")
    int countByKey(String key,Long id);
    /**
     * 查询特定Module name 是否已经存在
     * @param moduleName
     * @return
     */
    int countByModuleName(String moduleName);

    /**
     * 查询排除特定模块的Module name 是否已经存在
     * @param moduleName
     * @param id
     * @return
     */
    @Query("select count(1) from Module m where m.id!=?2 and moduleName=?1")
    int countByModuleName(String moduleName,Long id);

    /**
     * 查询订阅一个模块的用户
     * @param moduleId
     * @return
     */
    @Query("select distinct u from User u,UserRole ur,RoleAuth ra where ra.authId=?1 and ra.roleId = ur.roleId and ur.userId = u.id")
    List<User> findUserByModuleId(Long moduleId);

    /**
     * 更新模块状态
     * @param id
     * @param status
     * @param updateTime
     * @param updateBy
     * @return
     */
    @Modifying
    @Query("update Module m set m.status=?2,m.updateTime=?3,m.updateBy=?4 where m.id=?1")
    int updateModuleStatus(Long id,String status,Date updateTime,String updateBy);


}
