package com.minidooray.task.repository;

import com.minidooray.task.model.Milestone;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    private Project testProject;
    private Milestone testMilestone;


    @BeforeEach
    void setUp() {
        // 테스트에 사용할 Project와 Milestone을 미리 DB에 저장합니다.
        testProject = new Project(0L, "테스트 프로젝트", ProjectStatus.ACTIVE, null);
        testProject = projectRepository.saveAndFlush(testProject);

        testMilestone = new Milestone(0L, "테스트 마일스톤",
                LocalDateTime.now(), LocalDateTime.now().plusDays(7),
                testProject);
        testMilestone = milestoneRepository.saveAndFlush(testMilestone);
    }

    @DisplayName("Task 생성 후 ID로 조회 시 동일 데이터 반환")
    @Test
    void testSaveAndFindById(){
        // given
        Task newTask = new Task(0L, "새로운 Task 제목", "Task 내용입니다.", "tester",
                testProject, testMilestone);

        // when
        Task savedTask = taskRepository.saveAndFlush(newTask);
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        //then
        assertThat(foundTask.get().getTitle()).isEqualTo("새로운 Task 제목");
        assertThat(foundTask.get().getContent()).isEqualTo("Task 내용입니다.");
        assertThat(foundTask.get().getUsername()).isEqualTo("tester");
        assertThat(foundTask.get().getProject().getId()).isEqualTo(testProject.getId());
        assertThat(foundTask.get().getMilestone().getId()).isEqualTo(testMilestone.getId());

    }

    @DisplayName("Task 삭제 시 ID로 조회 불가")
    @Test
    void testDelete() {
        // given
        Task newTask = new Task(0L, "삭제할 Task", "곧 지워질 Task", "deleter",
                testProject, null);

        Task savedTask = taskRepository.saveAndFlush(newTask);
        // when
        taskRepository.delete(savedTask);
        // then
        assertThat(taskRepository.findById(savedTask.getId())).isEmpty();
    }

    @DisplayName("Task 수정 시 내용이 올바르게 반영되는지 확인")
    @Test
    void testUpdateTask() {
        Task updatedtask = new Task(0L, "제목", "내용", "updater", testProject, testMilestone);

        Task saved = taskRepository.save(updatedtask);

        saved.setTitle("수정된 제목");
        saved.setContent("수정된 내용");

        Task updatedTask = taskRepository.saveAndFlush(saved);

        assertThat(updatedTask.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedTask.getContent()).isEqualTo("수정된 내용");

    }
}
