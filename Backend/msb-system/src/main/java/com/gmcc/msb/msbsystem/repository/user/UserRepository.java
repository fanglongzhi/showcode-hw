package com.gmcc.msb.msbsystem.repository.user;

import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface UserRepository extends PagingAndSortingRepository<User,Long>,JpaSpecificationExecutor<User> {
    @Query("select count(1) from User u where u.operatorId = ?1 or u.userId = ?2 or u.loginId=?3 or u.personCardNo = ?4 or u.email = ?5 or u.mobile = ?6")
    Long countUser(Long operatorId,String userId,String loginId,String pId,String email,String mobile);
    @Modifying
    @Query("update User u set u.status = ?1,u.updateTime=CURRENT_TIMESTAMP() where u.id = ?2")
    @Transactional
    int updateUserStatus(UserStatus status,Long id);
    @Modifying
    @Query("update User u set u.isLock = ?1,u.updateTime=CURRENT_TIMESTAMP() where u.id = ?2")
    @Transactional
    int updateUserLock(Boolean isLock,Long id);

    Page<User> findAllByOrgId(Long orgid, Pageable pageable);

    @Query(value = "select " +
            "u.id as id, u.operator_id, u.user_id, u.login_id, " +
            "u.person_card_no, u.email, u.mobile, u.org_id, " +
            "u.org_name, u.name, u.status, u.is_lock, " +
            "do.id as dataOrgId, do.org_name as dataOrgName, u.create_time, u.update_time" +
            " from " +
            " t_user u left outer join t_data_org_user dou on" +
            " u.user_id = dou.user_id and dou.main_flag = '1' left outer join t_data_org do " +
            "on dou.org_id = do.id " +
            "where " +
            "( ?1 is null or operator_id = ?1) " +
            "and ( ?2 is null or name = ?2) " +
            "and ( ?3 is null or status = ?3) " +
            "and ( ?4 is null or is_lock = ?4) order by u.create_time ",nativeQuery = true)
    List<Object> findUserListWithDataOrgInfo(String operatorId, String name, UserStatus status, Boolean isLock);
}
