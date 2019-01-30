package com.gmcc.msb.zuul.repository;

import com.gmcc.msb.zuul.entity.DataOrgUser;
import com.gmcc.msb.zuul.entity.DataOrgUserKey;
import org.springframework.data.repository.CrudRepository;

/**
 * @program: msb-zuul
 * @description: 用户数据组DAO
 * @author: zhifanglong
 * @create: 2018-11-09 11:22
 */
public interface DataOrgUserRepository extends CrudRepository<DataOrgUser,DataOrgUserKey> {
  int countByUserIdAndMainFlag(String userId,String mainFlag);
}
