package com.gmcc.msb.msbsystem.repository.org;

import com.gmcc.msb.msbsystem.entity.org.DataOrgUser;
import com.gmcc.msb.msbsystem.entity.org.DataOrgUserKey;
import com.gmcc.msb.msbsystem.entity.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @program: msb-system
 * @description: 用户数据组DAO
 * @author: zhifanglong
 * @create: 2018-10-24 15:42
 */
public interface DataOrgUserRepository extends CrudRepository<DataOrgUser, DataOrgUserKey> {
    List<DataOrgUser> findByUserIdAndMainFlag(String userId, String mainFlag);

    @Query(value = "select du.org_id from t_data_org_user du where du.user_id = ?1",nativeQuery = true)
    List<Long> findByUserId(String userId);

    @Query("select du.userId from DataOrgUser du where du.orgId = ?1 and du.mainFlag = ?2 ")
    List<String> findUserIdInOrg(Integer orgId, String type);

    @Query("select du.userId from DataOrgUser du where du.orgId = ?1")
    List<String> findUserIdInOrg(Integer orgId);

    int countByOrgId(Integer orgId);

    @Modifying
    @Query("delete from DataOrgUser du where du.userId in ?1 and du.mainFlag = ?2")
    void deleteByUserId(List<String> userId, String type);

    @Modifying
    @Query("delete from DataOrgUser du where du.userId in ?1")
    void deleteByUserId(List<String> userId);

    @Query("select u from User u,DataOrgUser d where u.userId = d.userId and d.orgId = ?1 and d.mainFlag=?2 ")
    List<User> findUserList(Integer orgId, String type);

    @Query("select u from User u,DataOrgUser d where u.userId = d.userId  and d.mainFlag='1' ")
    List<User> findUserListMain();

    @Query("select u from User u,DataOrgUser d where u.userId = d.userId and d.orgId in ?1 and d.mainFlag='1' ")
    List<User> findChildrenUserListMain(List<Integer> orgList);
}
