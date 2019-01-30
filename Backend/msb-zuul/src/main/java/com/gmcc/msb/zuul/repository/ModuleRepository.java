package com.gmcc.msb.zuul.repository;

import com.gmcc.msb.zuul.entity.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @program: msb-zuul
 * @description: 模块DAO
 * @author: zhifanglong
 * @create: 2018-10-12 15:38
 */
public interface ModuleRepository extends CrudRepository<Module,Long>{

    @Query(value="select count(1) from t_module m,t_role_auth ra,t_user u,t_user_role ur " +
            "where u.user_id=?2 and ur.user_id=u.id and ur.role_id = ra.role_id " +
            "and ra.auth_id=m.id and m.apis regexp ?1 and m.status = '0' ",nativeQuery = true)
    int countApi(String apiIdRegex,String userId);
}
