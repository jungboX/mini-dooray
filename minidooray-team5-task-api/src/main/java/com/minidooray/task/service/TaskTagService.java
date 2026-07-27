package com.minidooray.task.service;

import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.taskTag.TaskTagCreateRequest;
import com.minidooray.task.dto.taskTag.TaskTagResponse;
import com.minidooray.task.dto.taskTag.TaskTagUpdateRequest;
import com.minidooray.task.model.Tag;
import com.minidooray.task.model.Task;
import com.minidooray.task.model.TaskTag;
import com.minidooray.task.repository.TagRepository;
import com.minidooray.task.repository.TaskRepository;
import com.minidooray.task.repository.TaskTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TaskTagService {

    private final TaskTagRepository taskTagRepository;
    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;


    @Autowired
    public TaskTagService(TaskTagRepository taskTagRepository, TagRepository tagRepository, TaskRepository taskRepository) {
        this.taskTagRepository = taskTagRepository;
        this.tagRepository = tagRepository;
        this.taskRepository = taskRepository;
    }
    // TaskTag create
    @Transactional
    public TaskTagResponse createTaskTag(Long taskId, TaskTagCreateRequest request) {
        // taskId로 task 조회
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task ID " + taskId + "를 찾을 수 없습니다."));

        long tagId = request.tagId();
        // tag아이디로 tag 조회
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new NoSuchElementException("tag ID " + tagId + "를 찾을 수 없습니다."));

//        if (taskTagRepository.findByTask_IdAndTag_Id(taskId, tagId).isPresent()) {
//            // 중복이 발견되면 예외를 발생시켜 저장을 막습니다.
//            // IllegalStateException 또는 DuplicationException 등의 커스텀 예외를 사용할 수 있습니다.
//            throw new IllegalStateException(
//                    "Task ID " + taskId + "에는 이미 Tag ID " + tagId + "가 연결되어 있습니다. (중복 불가)");
//        }

        TaskTag taskTag = new TaskTag(0L, task, tag);
        taskTagRepository.save(taskTag);

        return new TaskTagResponse(taskTag.getId(),taskTag.getTask().getId(),taskTag.getTag().getId());
    }
    // TaskTag read
    @Transactional(readOnly = true)
    public TaskTagResponse getTaskTag(Long taskId, Long taskTagId) {
        TaskTag taskTag = taskTagRepository.findById(taskTagId)
                .orElseThrow(() -> new NoSuchElementException("Task ID " + taskTagId + "를 찾을 수 없습니다."));

        if(taskTag.getTask().getId() != taskId){
            throw new IllegalArgumentException(
                    "TaskTag ID " + taskTagId + "는 Task ID " + taskId + "에 속하지 않습니다.");
        }


        return new TaskTagResponse(taskTag.getId(),taskTag.getTask().getId(),taskTag.getTag().getId());
    }

    @Transactional
    public void deleteTaskTag(Long taskId, Long taskTagId) {
        TaskTag taskTag = taskTagRepository.findById(taskTagId)
                .orElseThrow(() -> new NoSuchElementException("Task ID " + taskTagId + "를 찾을 수 없습니다."));

        if(taskTag.getTask().getId() != taskId){
            throw new IllegalArgumentException(
                    "TaskTag ID " + taskTagId + "는 Task ID " + taskId + "에 속하지 않습니다.");
        }

        taskTagRepository.delete(taskTag);
    }

    @Transactional
    public void deleteAllTaskTag(Long taskId) {
        taskTagRepository.deleteAllByTaskId(taskId);
    }

    // Task의 모든 Tag 조회!
    @Transactional(readOnly = true)
    public List<TagResponse> getTaskTags(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Task ID " + taskId + "를 찾을 수 없습니다."));

        // @OneToMany 활용
        return task.getTaskTags().stream()
                .map(TaskTag::getTag)
                .map(tag -> new TagResponse(
                        tag.getId(),
                        tag.getContent(),
                        tag.getProject().getId()
                ))
                .collect(Collectors.toList());
    }


    // TaskTagService.java (추가)

    @Transactional
    public TaskTagResponse updateTaskTag(Long taskId, Long taskTagId, TaskTagUpdateRequest request) {
        // 1. 기존 TaskTag 조회 및 Task ID 일치 검증 (기존 Read/Delete와 동일)
        TaskTag taskTag = taskTagRepository.findById(taskTagId)
                .orElseThrow(() -> new NoSuchElementException("TaskTag ID " + taskTagId + "를 찾을 수 없습니다."));

        if (taskTag.getTask().getId() != taskId) {
            throw new IllegalArgumentException(
                    "TaskTag ID " + taskTagId + "는 Task ID " + taskId + "에 속하지 않습니다.");
        }

        long newTagId = request.newTagId();

        // 2. 새로운 Tag 조회
        Tag newTag = tagRepository.findById(newTagId)
                .orElseThrow(() -> new NoSuchElementException("새로운 Tag ID " + newTagId + "를 찾을 수 없습니다."));

        // 3. 중복 연결 확인 (선택적)
        // 이 Task(taskId)에 새로운 Tag(newTagId)가 이미 연결되어 있는지 확인합니다.
        if (taskTagRepository.findByTask_IdAndTag_Id(taskId, newTagId).isPresent()) {
            throw new IllegalStateException(
                    "Task ID " + taskId + "에 Tag ID " + newTagId + "가 이미 연결되어 있어 변경할 수 없습니다.");
        }


        // 4. TaskTag 엔티티 업데이트
        taskTag.updateTag(newTag); // TaskTag 엔티티에 updateTag(Tag newTag) 메서드가 필요함

        // JPA는 @Transactional 환경에서 변경 사항을 자동으로 감지하고 저장합니다.
        return new TaskTagResponse(taskTag.getId(), taskTag.getTask().getId(), taskTag.getTag().getId());
    }
}
