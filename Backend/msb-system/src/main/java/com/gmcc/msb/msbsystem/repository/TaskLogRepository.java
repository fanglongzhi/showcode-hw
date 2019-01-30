package com.gmcc.msb.msbsystem.repository;

import com.gmcc.msb.msbsystem.entity.TaskLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


/**
 * @author Yuan Chunhai
 * @Date 12/11/2018-6:37 PM
 */
public interface TaskLogRepository extends CrudRepository<TaskLog, Integer> {

    Page<TaskLog> findAllByTaskIdEquals(int taskId, Pageable pageable);
}
