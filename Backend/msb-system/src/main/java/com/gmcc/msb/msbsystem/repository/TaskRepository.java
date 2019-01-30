package com.gmcc.msb.msbsystem.repository;

import com.gmcc.msb.msbsystem.entity.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 12/11/2018-6:37 PM
 */
public interface TaskRepository extends CrudRepository<Task, Integer> {

    Task findOneByServiceIdEqualsAndTaskKeyEquals(String serviceId, String taskKey);

    @Override
    @Query("select t from Task t order by t.lastRunStatus desc ")
    List<Task> findAll();
}
