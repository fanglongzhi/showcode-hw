package com.gmcc.msb.msbsystem.repository.org;

import com.gmcc.msb.msbsystem.entity.org.DataOrg;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @program: msb-system
 * @description: 数据组DAO
 * @author: zhifanglong
 * @create: 2018-10-29 18:14
 */
public interface DataOrgRepository extends PagingAndSortingRepository<DataOrg, Integer> {
    int countByOrgName(String orgName);

    @Query("select count(1) from DataOrg a where a.orgName=?1 and a.id != ?2")
    int countByOrgName(String orgName, Integer id);

    @Query(value = "select do.* from t_data_org do where do.parent_list regexp ?1",
            nativeQuery = true)
    List<DataOrg> findChildren(String parent);

    @Query(value = "select distinct do.id from t_data_org do where do.parent_list regexp ?1", nativeQuery = true)
    List<Long> findAllOrgIdChildren(String parentRegexp);
    @Query(value="select count(1) from t_service t where t.org_id = ?1",nativeQuery = true)
    int countServiceInDataOrg(int orgId);
    @Query(value="select count(1) from t_app t where t.org_id = ?1",nativeQuery = true)
    int countAppInDataOrg(int orgId);
}
