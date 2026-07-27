package com.minidooray.task.service;

import com.minidooray.task.dto.tag.TagCreateRequest;
import com.minidooray.task.dto.tag.TagResponse;
import com.minidooray.task.dto.tag.TagUpdateRequest;
import com.minidooray.task.dto.task.TaskResponse;
import com.minidooray.task.model.*;
import com.minidooray.task.repository.ProjectRepository;
import com.minidooray.task.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagRepository tagRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TagService tagService;

    private Project mockProject;
    private Tag mockTag;
    private Task mockTask;
    private TaskTag mockTaskTag;

    private final Long projectId = 1L;
    private final Long tagId = 10L;

    @BeforeEach
    void setUp() {
        // Mock Project, Tag 설정
        mockProject = new Project(projectId, "Test Project", ProjectStatus.ACTIVE, null);
        mockTag = new Tag(tagId, "Feature", mockProject, new ArrayList<>());

        // Mock Task 설정 (TaskTagService.getTasksByTag 테스트용)
        mockTask = new Task(100L, "Task for Tag", "Content", "userA", mockProject, null);
        mockTaskTag = new TaskTag(1000L, mockTask, mockTag);

        // Tag 엔티티에 TaskTag 연관 관계 설정
        mockTag.getTaskTags().add(mockTaskTag);
    }

    @DisplayName("태그 생성 성공")
    @Test
    void testCreateTag_success() {
        // given
        TagCreateRequest request = new TagCreateRequest("Bugfix");
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));
        when(tagRepository.save(any(Tag.class))).thenReturn(mockTag);

        // when
        TagResponse response = tagService.create(projectId, request);

        // then
        verify(tagRepository, times(1)).save(any(Tag.class));
        assertThat(response.content()).isEqualTo("Feature"); // mockTag의 content
        assertThat(response.projectId()).isEqualTo(projectId);
    }

    @DisplayName("태그 단일 조회 성공")
    @Test
    void testReadProjectId_success() {
        // given
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mockTag));

        // when
        TagResponse response = tagService.readProjectId(projectId, tagId);

        // then
        assertThat(response.id()).isEqualTo(tagId);
        assertThat(response.content()).isEqualTo("Feature");
        assertThat(response.projectId()).isEqualTo(projectId);
    }

    @DisplayName("태그 단일 조회 실패 (프로젝트 소속 불일치)")
    @Test
    void testReadProjectId_fail_wrongProject() {
        // given
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mockTag)); // mockTag는 projectId 1L 소속

        // when & then
        // 다른 projectId (99L)로 조회 시 IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class, () ->
                tagService.readProjectId(99L, tagId));
    }

    @DisplayName("태그 수정 성공 (content만 수정)")
    @Test
    void testUpdateTag_success_contentOnly() {
        // given
        TagUpdateRequest request = new TagUpdateRequest("Refactoring", null);
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mockTag));
        when(tagRepository.save(any(Tag.class))).thenReturn(mockTag); // 변경된 mockTag 반환

        // when
        TagResponse response = tagService.updateTag(tagId, request);

        // then
        verify(tagRepository, times(1)).save(any(Tag.class));
        assertThat(response.content()).isEqualTo("Refactoring"); // mock 객체의 setter가 호출되어 변경됨
        assertThat(response.projectId()).isEqualTo(projectId);
    }

    @DisplayName("태그 수정 성공 (project 포함 수정)")
    @Test
    void testUpdateTag_success_withProject() {
        // given
        Long newProjectId = 2L;
        Project newProject = new Project(newProjectId, "New Project", ProjectStatus.ACTIVE, null);
        TagUpdateRequest request = new TagUpdateRequest(null, newProjectId);

        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mockTag));
        when(projectRepository.findById(newProjectId)).thenReturn(Optional.of(newProject));
        when(tagRepository.save(any(Tag.class))).thenReturn(mockTag);

        // when
        tagService.updateTag(tagId, request);

        // then
        // mockTag의 project가 newProject로 변경되었는지 확인
        assertThat(mockTag.getProject().getId()).isEqualTo(newProjectId);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @DisplayName("태그 삭제 성공")
    @Test
    void testDeleteTag_success() {
        // given
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mockTag));

        // when
        tagService.deleteTag(tagId, projectId);

        // then
        verify(tagRepository, times(1)).delete(mockTag);
    }

    @DisplayName("Tag가 사용된 모든 Task 조회 성공")
    @Test
    void testGetTasksByTag_success() {
        // given
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(mockTag));

        // when
        List<TaskResponse> responseList = tagService.getTasksByTag(tagId);

        // then
        assertThat(responseList).hasSize(1);
        TaskResponse response = responseList.get(0);
        assertThat(response.title()).isEqualTo("Task for Tag");
        assertThat(response.username()).isEqualTo("userA");
    }
}