package com.minidooray.task.service;

import com.minidooray.task.dto.comment.CommentResponse;
import com.minidooray.task.dto.milestone.MilestoneCreateRequest;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.task.*;
import com.minidooray.task.model.*;
import com.minidooray.task.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;
    private final CommentService commentService;
    private final TaskTagService taskTagService;
    private final CommentRepository commentRepository;
    private final TaskTagRepository taskTagRepository;
    private final TagRepository tagRepository;
    private final TagService tagService;


    @Autowired
    public TaskService(TaskRepository taskRepository,
                       MilestoneRepository milestoneRepository,
                       ProjectRepository projectRepository,
                       CommentService commentService,
                       TaskTagService taskTagService, CommentRepository commentRepository, TaskTagRepository taskTagRepository, TagRepository tagRepository, TagService tagService) {
        this.taskRepository = taskRepository;
        this.milestoneRepository = milestoneRepository;
        this.projectRepository = projectRepository;
        this.commentService = commentService;
        this.taskTagService = taskTagService;
        this.commentRepository = commentRepository;
        this.taskTagRepository = taskTagRepository;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }
    // Task 생성
    @Transactional
    public TaskResponse createTask(Long projectId, TaskCreateRequest request){
        // 프로젝트 아이디로 프로젝트 엔티티 조회
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("프로젝트 ID " + projectId + "를 찾을 수 없습니다."));

        Long milestoneId = request.milestoneId();

        Milestone milestone = null;
        // 마일 스톤 아이디로 마일스톤 엔티티 조회
        if (milestoneId != null) {
            milestone = milestoneRepository.findById(milestoneId)
                    .orElseThrow(() -> new NoSuchElementException("마일스톤 ID " + milestoneId + "를 찾을 수 없습니다."));
        }

        // task 생성
        Task task = new Task(0L, request.title(), request.content(), request.username(), project, milestone);
        Task savedTask = taskRepository.save(task);

        return convertToResponse(savedTask);

    }

    @Transactional(readOnly = true) // 데이터 변경이 없으므로 readOnly = true 설정
    public TaskDetailResponse getTask(Long projectId, Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task ID " + taskId + "를 찾을 수 없습니다."));

        if (task.getProject().getId() != projectId) {
            throw new IllegalArgumentException("Task ID " + taskId + "는 프로젝트 ID " + projectId + "에 속하지 않습니다.");
        }

        List<TagResponse> taskTags = taskTagService.getTaskTags(taskId);
        List<CommentResponse> comments = commentService.getAllCommentsByTaskId(taskId);

        MilestoneResponse milestoneResponse = convertToMilestoneResponse(task.getMilestone());
        return new TaskDetailResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getUsername(),
                milestoneResponse,
                taskTags,
                comments
        );
    }

    @Transactional(readOnly = true)
    public List<TaskSummaryResponse> getTaskList(Long projectId) {

        List<Task> tasks = taskRepository.findALlByProjectId(projectId);
        List<TaskSummaryResponse> taskSummaryResponses = new ArrayList<>();

        for (Task task : tasks) {
            taskSummaryResponses.add(new TaskSummaryResponse(task.getId(), task.getTitle()));
        }

        return taskSummaryResponses;
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskUpdateRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        if(request.title() != null){
            task.setTitle(request.title());
        }

        if(request.content() != null){
            task.setContent(request.content());
        }

        if(request.milestoneId() != null){
            Milestone milestone = milestoneRepository.findById(request.milestoneId())
                    .orElseThrow(() -> new NoSuchElementException("Milestone not found"));
            task.setMilestone(milestone);
        }

        taskTagRepository.deleteAllByTaskId(taskId);

        List<Long> tags = request.tagIds();
        for (Long tag : tags) {
            Optional<Tag> tag1 = tagRepository.findById(tag);

            Optional<Task> task1 = taskRepository.findById(taskId);

            TaskTag taskTag = new TaskTag(0L, task1.get(), tag1.get());

            taskTagRepository.save(taskTag);
        }


        Task updatedTask = taskRepository.save(task);

        return convertToResponse(updatedTask);

    }
    @Transactional
    public void deleteTask(Long projectId, Long tasksId) {
        Task task = taskRepository.findById(tasksId)
                .orElseThrow(() -> new NoSuchElementException("마일스톤 ID " + tasksId + "를 찾을 수 없습니다."));

        if(task.getProject().getId() != projectId){
            throw new IllegalArgumentException("task ID " + tasksId + "는 프로젝트 ID " + projectId + "에 속하지 않습니다.");
        }

        commentRepository.deleteAllByTaskId(tasksId);

        // 삭제
        taskRepository.delete(task);


    }


    private TaskResponse convertToResponse(Task task) {
        // Milestone은 nullable 필드이므로 null 체크가 필요
        Long milestoneId = (task.getMilestone() != null)
                ? task.getMilestone().getId()
                : null;


        // TaskResponse 레코드 생성자 호출
        return new TaskResponse(
                task.getTitle(),
                task.getContent(),
                task.getUsername(),
                milestoneId

        );
    }

    private MilestoneResponse convertToMilestoneResponse(Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return new MilestoneResponse(
                milestone.getId(),
                milestone.getContent(),
                milestone.getStartedAt(),
                milestone.getEndedAt(),
                milestone.getProject().getId()
        );
    }


}
