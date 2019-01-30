package com.gmcc.msb.config.repository;

import com.gmcc.msb.config.entity.Config;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ConfigRepository extends PagingAndSortingRepository<Config, Integer> {

    List<Config> findByApplication(String application, Sort sort);

    List<Config> findByApplicationAndProfile(String application, String profile, Sort sort);

    @Modifying
    @Query("update Config c set c.propertyValue = ?2,c.propertyKey = ?3,c.label=?4,c.profile=?5,c.updateTime = current_timestamp() where c.id = ?1")
    @Transactional
    void modifyConfig(Integer id, String propertyValue, String propertyKey, String label, String profile);

    @Query("select count(1) from Config c where c.label=?1 and c.profile = ?2 and application=?3 and propertyKey = ?4")
    Integer countConfig(String label, String profile, String application, String propertyKey);

    @Query("select c from Config c where c.label=?1 and c.profile = ?2 and application=?3 and propertyKey = ?4")
    List<Config> findOneConfig(String label, String profile, String application, String propertyKey);

    /** 根据application，key，profile查询配置信息 */
    List<Config> findAllByApplicationEqualsAndPropertyKeyEqualsAndProfileEquals(String application, String propertyKey, String profile);

    /** 查询同一个key的所有配置 */
    List<Config> findAllByPropertyKeyEqualsAndProfileEquals(String propertyKey, String profile);

}
