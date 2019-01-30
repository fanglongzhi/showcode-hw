package com.gmcc.msb.msbservice.repository;

import com.gmcc.msb.msbservice.entity.ServiceItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @author zhi fanglong
 */
public interface ServiceItemRepository extends PagingAndSortingRepository<ServiceItem,Integer> {

    /**
     * 计算服务id相同记录的数量
     * @param serviceIdLower
     * @param serviceIdUpper
     * @return
     */
    @Query("select count(1) from ServiceItem s where s.serviceId = ?1 or s.serviceId = ?2")
    public int countByServiceId(String serviceIdLower,String serviceIdUpper);

    /**
     * 统计服务id和服务名相同的记录
     * @param serviceIdLower
     * @param serviceIdUpper
     * @param serviceName
     * @return
     */
    @Query("select count(1) from ServiceItem s where s.serviceId = ?1 or s.serviceId = ?2 or s.serviceName = ?3")
    public Integer countByServiceIdAndServiceName(String serviceIdLower,String serviceIdUpper,String serviceName);


    /**
     * 通过服务ID查找
     *
     * @param serviceId
     * @return
     */
    ServiceItem findOneByServiceIdEquals(String serviceId);

    /**
     * 通过服务CODE查找
     *
     * @param serviceCode
     * @return
     */
    List<ServiceItem> findByServiceCodeEquals(String serviceCode);

    /**
     * 记录服务最后一次配置刷新成功的时间
     * @param serviceId
     * @param currDate
     */
    @Modifying
    @Query("update ServiceItem si set si.refreshDate = ?2 where si.serviceId=?1")
    void updateRefreshDate(String serviceId,Date currDate);

    @Query("select t from ServiceItem t where t.user.orgId in ?1")
    List<ServiceItem> findServiceItemInOrgs(List<Long> orgIds,Sort sort);

    ServiceItem findFirstByOrderByServiceCodeDesc();
}
