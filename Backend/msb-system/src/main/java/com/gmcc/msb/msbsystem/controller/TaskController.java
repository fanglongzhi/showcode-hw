package com.gmcc.msb.msbsystem.controller;

import com.gmcc.msb.common.vo.Response;
import com.gmcc.msb.msbsystem.entity.Task;
import com.gmcc.msb.msbsystem.entity.TaskLog;
import com.gmcc.msb.msbsystem.service.TaskService;
import com.gmcc.msb.msbsystem.vo.req.TaskLogRequest;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Yuan Chunhai
 * @Date 12/11/2018-6:35 PM
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/task/list")
    public Response<Task> taskLists() {
        return Response.ok(taskService.taskList());
    }

    @GetMapping("/tasklogs/{taskId}")
    public Response<Page<TaskLog>> taskLogs(@PathVariable @NotEmpty int taskId, @PageableDefault(sort = "runTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<TaskLog> page = taskService.taskLogs(taskId, pageable);
        return Response.ok(page);
    }

    @PostMapping("/tasklog")
    public Response<String> addTaskLog(@RequestBody @Valid TaskLogRequest taskLog) {
        taskService.addTaskLog(taskLog);
        return Response.ok();
    }


}
