package com.minidooray.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.minidooray.task.dto.task.TaskCreateRequest;
import com.minidooray.task.dto.task.TaskResponse;
import com.minidooray.task.model.Milestone;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.model.Task;
import com.minidooray.task.repository.MilestoneRepository;
import com.minidooray.task.repository.ProjectRepository;
import com.minidooray.task.repository.TaskRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private CommentService commentService;
    @Mock
    private TaskTagService taskTagService;

    @InjectMocks
    private TaskService taskService;

    private Project mockProject;
    private Milestone mockMilestone;
    private TaskCreateRequest createRequest;
    private Task mockTask;

    @BeforeEach
    void setUp() {
        // 더미 Project 및 Milestone 엔티티 준비
        mockProject = new Project(1L, "프로젝트 A", ProjectStatus.ACTIVE, Collections.emptyList());
        mockMilestone = new Milestone(2L, "마일스톤 B", LocalDateTime.now(), LocalDateTime.now().plusDays(10), mockProject);

        // Task 생성 요청 DTO 준비 (마일스톤 포함)
        createRequest = new TaskCreateRequest("제목", "내용", "userA", 2L);

        // 저장될 Task 객체 예상
        mockTask = new Task(3L, createRequest.title(), createRequest.content(), createRequest.username(),
                mockProject, mockMilestone);
    }

    @DisplayName("Task 생성 성공 (마일스톤 포함)")
    @Test
    void testCreateTask_successWithMilestone() {
        // given
        // 1. ProjectRepository가 ID로 조회 요청 시 mockProject 반환
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        // 2. MilestoneRepository가 ID로 조회 요청 시 mockMilestone 반환
        when(milestoneRepository.findById(2L)).thenReturn(Optional.of(mockMilestone));
        // 3. TaskRepository.save() 호출 시, ID가 부여된 mockTask 반환
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);

        // when
        TaskResponse response = taskService.createTask(1L, createRequest);

        // then
        // save 메서드가 한 번 호출되었는지 확인
        verify(taskRepository, times(1)).save(any(Task.class));

        // 반환된 DTO의 내용 확인
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.content()).isEqualTo("내용");
        assertThat(response.milestoneId()).isEqualTo(2L);
    }

    @DisplayName("Task 생성 실패 (존재하지 않는 프로젝트 ID)")
    @Test
    void testCreateTask_failInvalidProject() {
        // given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class, () ->
                taskService.createTask(99L, createRequest));
    }

    @DisplayName("Task 상세 조회 성공 (getTask)")
    @Test
    void testGetTask_success() {
        // given
        Long taskId = 3L;
        Long projectId = 1L;

        // TaskRepository 모킹: Task 엔티티 반환
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        // TagService 모킹: 빈 태그 리스트 반환
        when(taskTagService.getTaskTags(taskId)).thenReturn(Collections.emptyList());

        // CommentService 모킹: 빈 댓글 리스트 반환
        when(commentService.getAllCommentsByTaskId(taskId)).thenReturn(Collections.emptyList());

        // when
        taskService.getTask(projectId, taskId);

        // then
        // 모든 의존성 메서드가 호출되었는지 확인
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskTagService, times(1)).getTaskTags(taskId);
        verify(commentService, times(1)).getAllCommentsByTaskId(taskId);
    }

    @DisplayName("Task 상세 조회 실패 (ID는 존재하나 프로젝트가 다름)")
    @Test
    void testGetTask_failWrongProject() {
        // given
        Long taskId = 3L;
        Long wrongProjectId = 99L;

        // Mock Task는 projectId 1L에 속함
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                taskService.getTask(wrongProjectId, taskId));
    }

}