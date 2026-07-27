package com.minidooray.task.service;

import com.minidooray.task.dto.comment.CommentCreateRequest;
import com.minidooray.task.dto.comment.CommentResponse;
import com.minidooray.task.model.Comment;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectMember;
import com.minidooray.task.model.Task;
import com.minidooray.task.repository.CommentRepository;
import com.minidooray.task.repository.ProjectMemberRepository;
import com.minidooray.task.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @InjectMocks
    private CommentService commentService;

    private Task mockTask;
    private Comment mockComment;
    private Long projectId = 1L;
    private Long taskId = 10L;
    private Long commentId = 100L;

    @BeforeEach
    void setUp(){
        Project mockProject = new Project(projectId, "Mock Project", null, null);
        mockTask = new Task(taskId, "Mock Task", "Content", "taskUser", mockProject, null);

        mockComment = new Comment(commentId, mockTask, "commenter", "Original Comment");

    }

    @DisplayName("댓글 생성 성공")
    @Test
    void testCreateComment_success(){
        // given
        CommentCreateRequest request = new CommentCreateRequest("newCommenter", "새로운 댓글");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        when(projectMemberRepository.findByProjectIdAndUsername(anyLong(), eq("newCommenter")))
                .thenReturn(Optional.of(mock(ProjectMember.class))); // 프로젝트 멤버만 댓글 생성

        // when
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        CommentResponse response = commentService.createComment(taskId, request);

        verify(taskRepository,times(1)).findById(taskId);
        verify(commentRepository, times(1)).save(any(Comment.class));

        // then
        assertThat(response.id()).isEqualTo(commentId);
        assertThat(response.username()).isEqualTo("commenter");
        assertThat(response.taskId()).isEqualTo(taskId);


    }

    @DisplayName("댓글 생성 실패 (존재하지 않는 Task ID)")
    @Test
    void testCreateComment_fail_taskNotFound() {
        // given
        CommentCreateRequest request = new CommentCreateRequest("user", "content");
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class, () ->
                commentService.createComment(999L, request));
    }

    @DisplayName("댓글 단일 조회 성공")
    @Test
    void testGetComment_success() {
        // given
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        CommentResponse response = commentService.getComment(taskId, commentId);

        // then
        assertThat(response.id()).isEqualTo(commentId);
        assertThat(response.content()).isEqualTo("Original Comment");
    }

    @DisplayName("댓글 단일 조회 실패 (Task 소속 불일치)")
    @Test
    void testGetComment_fail_wrongTaskId() {
        // given: mockComment는 taskId=10L에 속함
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when & then
        // 다른 taskId (99L)로 조회 시 IllegalArgumentException 발생
        assertThrows(IllegalArgumentException.class, () ->
                commentService.getComment(99L, commentId));
    }

    @DisplayName("댓글 목록 조회 성공")
    @Test
    void testGetAllCommentsByTaskId_success() {
        // given
        Comment comment2 = new Comment(101L, mockTask, "commenter2", "Second Comment");
        List<Comment> mockComments = List.of(mockComment, comment2);

        when(commentRepository.findAllByTaskId(taskId)).thenReturn(mockComments);

        // when
        List<CommentResponse> responseList = commentService.getAllCommentsByTaskId(taskId);

        // then
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).id()).isEqualTo(commentId);
        assertThat(responseList.get(1).content()).isEqualTo("Second Comment");
        verify(commentRepository, times(1)).findAllByTaskId(taskId);
    }

    @DisplayName("댓글 수정 성공 (Content 변경)")
    @Test
    void testUpdateComment_success() {
        // given
        String newContent = "Updated Content";
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        // save 호출 시 변경된 mockComment 객체를 반환하도록 설정
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        // when
        CommentResponse response = commentService.updateComment(taskId, commentId, newContent);

        // then
        // save가 호출되었는지 확인
        verify(commentRepository, times(1)).save(any(Comment.class));
        // 내용이 실제로 변경되었는지 확인
        assertThat(response.content()).isEqualTo(newContent);
    }

    @DisplayName("댓글 삭제 성공")
    @Test
    void testDeleteComment_success() {
        // given
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        // when
        commentService.deleteComment(taskId, commentId);

        // then
        verify(commentRepository, times(1)).delete(mockComment);
    }

}
