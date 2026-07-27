package com.minidooray.task.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.minidooray.task.model.Milestone;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Testcontainers 사용 설정
public class MilestoneRepositoryTest {

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;
    private Milestone milestone1;
    private Milestone milestone2;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 Project를 먼저 저장
        testProject = new Project("테스트 프로젝트", ProjectStatus.ACTIVE);
        testProject = projectRepository.saveAndFlush(testProject);

        // 테스트에 사용할 Milestone들을 저장
        milestone1 = new Milestone(0L, "초기 버전 마일스톤",
                LocalDateTime.now(), LocalDateTime.now().plusDays(7),
                testProject);
        milestone2 = new Milestone(0L, "버그 수정 마일스톤",
                LocalDateTime.now().plusDays(8), LocalDateTime.now().plusDays(15),
                testProject);

        milestone1 = milestoneRepository.saveAndFlush(milestone1);
        milestone2 = milestoneRepository.saveAndFlush(milestone2);
    }

    @DisplayName("Milestone 생성 후 ID로 조회 시 동일 데이터 반환")
    @Test
    void testSaveAndFindById() {
        // given: setUp에서 이미 생성 및 저장됨
        Long milestoneId = milestone1.getId();

        // when
        Optional<Milestone> foundMilestone = milestoneRepository.findById(milestoneId);

        // then
        assertThat(foundMilestone).isPresent();
        assertThat(foundMilestone.get().getContent()).isEqualTo("초기 버전 마일스톤");
        assertThat(foundMilestone.get().getProject().getId()).isEqualTo(testProject.getId());
    }

    @DisplayName("Milestone 삭제 시 ID로 조회 불가")
    @Test
    void testDelete() {
        // given: milestone2를 삭제 대상으로 지정
        Long milestoneId = milestone2.getId();

        // when
        milestoneRepository.delete(milestone2);

        // then
        assertThat(milestoneRepository.findById(milestoneId)).isEmpty();
    }

    @DisplayName("findByProjectId로 해당 프로젝트의 모든 Milestone 조회")
    @Test
    void testFindByProjectId() {
        // given: setUp에서 testProject에 2개의 마일스톤 저장됨
        Long projectId = testProject.getId();

        // when
        List<Milestone> milestones = milestoneRepository.findByProjectId(projectId);

        // then
        assertThat(milestones).hasSize(2);
        assertThat(milestones).extracting(Milestone::getContent)
                .containsExactlyInAnyOrder("초기 버전 마일스톤", "버그 수정 마일스톤");
    }

    @DisplayName("Milestone 수정 후 변경된 내용이 반영되는지 확인")
    @Test
    void testUpdateMilestone() {
        // given
        LocalDateTime newEndDate = LocalDateTime.now().plusMonths(1);

        // when
        milestone1.updateDetails("수정된 마일스톤 내용", null, newEndDate);
        Milestone updatedMilestone = milestoneRepository.saveAndFlush(milestone1);

        // then
        assertThat(updatedMilestone.getContent()).isEqualTo("수정된 마일스톤 내용");
        assertThat(updatedMilestone.getEndedAt()).isEqualTo(newEndDate);
        // startedAt은 null이므로 변경되지 않아야 함 (엔티티 로직에 따라)
        assertThat(updatedMilestone.getStartedAt()).isNotNull();
    }
}