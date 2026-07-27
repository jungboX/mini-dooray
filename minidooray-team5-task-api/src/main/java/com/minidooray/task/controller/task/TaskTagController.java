package com.minidooray.task.controller.task;

import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.taskTag.TaskTagCreateRequest;
import com.minidooray.task.dto.taskTag.TaskTagResponse;
import com.minidooray.task.dto.taskTag.TaskTagUpdateRequest;
import com.minidooray.task.service.TaskTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks/")
public class TaskTagController {

    private final TaskTagService taskTagService;

    @Autowired
    public TaskTagController(TaskTagService taskTagService) {
        this.taskTagService = taskTagService;
    }
    // taskTag create
    @PostMapping("/{taskId}/tags")
    public ResponseEntity<TaskTagResponse> createTaskTag(@PathVariable Long taskId,
                                                         @RequestBody TaskTagCreateRequest request){
        TaskTagResponse response = taskTagService.createTaskTag(taskId,request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // taskTag read
    @GetMapping("/{taskId}/tags/{taskTagId}")
    public ResponseEntity<TaskTagResponse> getTaskTag(
            @PathVariable Long taskId, @PathVariable Long taskTagId){

        TaskTagResponse response = taskTagService.getTaskTag(taskId,taskTagId);

        return ResponseEntity.ok(response);
    }
    // taskId로 해당 모든 tag 조회
    @GetMapping("/{taskId}/tags")
    public ResponseEntity<List<TagResponse>> getTaskTags(@PathVariable Long taskId) {
        List<TagResponse> tags = taskTagService.getTaskTags(taskId);
        return ResponseEntity.ok(tags);
    }

    // tag 수정은 프로젝트에서 하기 때문에 생략
    @PutMapping("/{taskId}/tags/{taskTagId}")
    public ResponseEntity<TaskTagResponse> updateTaskTag(
            @PathVariable Long taskId,
            @PathVariable Long taskTagId,
            @RequestBody TaskTagUpdateRequest request) {

        TaskTagResponse response = taskTagService.updateTaskTag(taskId, taskTagId, request);

        // 200 OK 상태 코드 반환
        return ResponseEntity.ok(response);
    }


    // tag 삭제
    @DeleteMapping("/{taskId}/tags/{taskTagId}")
    public ResponseEntity<Void> deleteTaskTag(
            @PathVariable Long taskId, @PathVariable Long taskTagId){

        taskTagService.deleteTaskTag(taskId, taskTagId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }



}
