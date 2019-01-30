package com.gmcc.msb.msbsystem.service;

import com.gmcc.msb.msbsystem.config.MyProperties;
import com.gmcc.msb.msbsystem.entity.Task;
import com.gmcc.msb.msbsystem.entity.TaskLog;
import com.gmcc.msb.msbsystem.repository.TaskLogRepository;
import com.gmcc.msb.msbsystem.repository.TaskRepository;
import com.gmcc.msb.msbsystem.vo.req.TaskLogRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 12/11/2018-6:37 PM
 */
@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskLogRepository taskLogRepository;

    @Autowired
    private MyProperties properties;


    public List<Task> taskList() {
        return taskRepository.findAll();
    }

    @Transactional(rollbackOn = Exception.class)
    public void addTaskLog(TaskLogRequest taskLogReq) {
        Task task = taskRepository.findOneByServiceIdEqualsAndTaskKeyEquals(
                taskLogReq.getServiceId(), taskLogReq.getTaskKey());
        if (task == null) {
            log.warn("任务未定义，{} {} ", taskLogReq.getServiceId(), taskLogReq.getTaskKey());
            return;
        }
        if (properties.getTaskNames().containsKey(taskLogReq.getTaskKey())) {
            task.setTaskName(properties.getTaskNames().get(taskLogReq.getTaskKey()));
        } else {
            task.setTaskName("未定义");
        }
        if (task.getLastRunTime() == null || task.getLastRunTime().before(taskLogReq.getRunTime())) {
            task.setLastRunTime(taskLogReq.getRunTime());
            task.setLastRunStatus(taskLogReq.getRunStatus());
        }
        taskRepository.save(task);

        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setRunTime(taskLogReq.getRunTime());
        taskLog.setRunStatus(taskLogReq.getRunStatus());
        taskLog.setErrorMessage(taskLogReq.getErrorMessage());
        taskLog.setElasped(taskLogReq.getElasped());

        taskLogRepository.save(taskLog);
    }


    public Page<TaskLog> taskLogs(int taskId, Pageable pageable) {
        return this.taskLogRepository.findAllByTaskIdEquals(taskId, pageable);
    }
}
