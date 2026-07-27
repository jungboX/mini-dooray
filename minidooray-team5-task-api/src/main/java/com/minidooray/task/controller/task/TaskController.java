package com.minidooray.task.controller.task;

import com.minidooray.task.dto.task.TaskCreateRequest;
import com.minidooray.task.dto.task.TaskDetailResponse;
import com.minidooray.task.dto.task.TaskResponse;
import com.minidooray.task.dto.task.TaskUpdateRequest;
import com.minidooray.task.service.TaskService;
import com.minidooray.task.service.TaskTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    // task 생성
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId,
                                                   @RequestBody TaskCreateRequest request){
        TaskResponse response = taskService.createTask(projectId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // task 단일 조회
    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskDetailResponse> getTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId) {

        TaskDetailResponse response = taskService.getTask(projectId, taskId);

        return ResponseEntity.ok(response);
    }

    // task 수정
    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponse> patchTask(@PathVariable Long taskId,
                                                  @RequestBody TaskUpdateRequest request){
        TaskResponse updateTask = taskService.updateTask(taskId, request);

        return ResponseEntity.ok(updateTask); // 200 OK

    }
    // task 삭제
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long projectId, @PathVariable Long taskId){
        taskService.deleteTask(projectId, taskId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
