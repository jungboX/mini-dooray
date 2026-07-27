package com.minidooray.task.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.minidooray.task.dto.milestone.MilestoneCreateRequest;
import com.minidooray.task.dto.milestone.MilestoneResponse;
import com.minidooray.task.dto.milestone.MilestoneUpdateRequest;
import com.minidooray.task.model.Milestone;
import com.minidooray.task.model.Project;
import com.minidooray.task.model.ProjectStatus;
import com.minidooray.task.repository.MilestoneRepository;
import com.minidooray.task.repository.ProjectRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MilestoneServiceTest {

    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private MilestoneService milestoneService;

    private Project mockProject;
    private Milestone mockMilestone;
    private MilestoneCreateRequest createRequest;
    private MilestoneUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // 공통 Mock 객체 설정
        mockProject = new Project(1L, "테스트 프로젝트", ProjectStatus.ACTIVE, null);
        mockMilestone = new Milestone(10L, "초기 마일스톤",
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.of(2025, 1, 31, 23, 59),
                mockProject);

        // 생성 요청 DTO
        createRequest = new MilestoneCreateRequest(
                "새로운 마일스톤",
                LocalDateTime.of(2025, 2, 1, 0, 0),
                LocalDateTime.of(2025, 2, 28, 23, 59),
                1L, // projectId
                "testUser"
        );

        // 수정 요청 DTO
        updateRequest = new MilestoneUpdateRequest(
                "수정된 내용",
                null, // 시작일은 수정 안 함
                LocalDateTime.of(2025, 12, 31, 23, 59)
        );
    }

    @DisplayName("Milestone 생성 성공 테스트")
    @Test
    void testCreateMilestone_success() {
        // given
        // 1. ProjectRepository 모킹: 프로젝트 ID 1L로 조회 시 mockProject 반환
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));

        // 2. MilestoneRepository 모킹: save 호출 시 ID가 부여된 mockMilestone 반환
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(mockMilestone);

        // when
        MilestoneResponse response = milestoneService.createMilestone(1L, createRequest);

        // then
        // save 메서드가 한 번 호출되었는지 확인
        verify(milestoneRepository, times(1)).save(any(Milestone.class));

        // 반환된 DTO의 내용 확인
        assertThat(response.getContent()).isEqualTo("초기 마일스톤"); // save mock에 의해 결정됨
        assertThat(response.getProjectId()).isEqualTo(1L);
    }

    @DisplayName("Milestone 생성 실패 테스트 (존재하지 않는 프로젝트 ID)")
    @Test
    void testCreateMilestone_fail_projectNotFound() {
        // given
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class, () ->
                milestoneService.createMilestone(99L, createRequest));
    }

    @DisplayName("Milestone 단일 조회 성공 테스트")
    @Test
    void testGetMilestone_success() {
        // given
        when(milestoneRepository.findById(10L)).thenReturn(Optional.of(mockMilestone));

        // when
        MilestoneResponse response = milestoneService.getMilestone(1L, 10L);

        // then
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getContent()).isEqualTo("초기 마일스톤");
    }

    @DisplayName("Milestone 단일 조회 실패 테스트 (Task에 속하지 않는 마일스톤 ID)")
    @Test
    void testGetMilestone_fail_wrongProject() {
        // given
        when(milestoneRepository.findById(10L)).thenReturn(Optional.of(mockMilestone));

        // when & then
        // 프로젝트 ID 99L로 요청 시 mockMilestone의 프로젝트 ID 1L과 다르므로 예외 발생
        assertThrows(IllegalArgumentException.class, () ->
                milestoneService.getMilestone(99L, 10L));
    }

    @DisplayName("Milestone 수정 성공 테스트")
    @Test
    void testUpdateMilestone_success() {
        // given
        // updateDetails 로직이 엔티티 내부에 있으므로, Service에서는 save 호출만 확인
        when(milestoneRepository.findById(10L)).thenReturn(Optional.of(mockMilestone));

        // updateDetails가 mockMilestone을 변경한 후, save가 호출되면 변경된 mockMilestone을 반환하도록 설정
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(mockMilestone);

        // when
        MilestoneResponse response = milestoneService.updateMilestone(1L, 10L, updateRequest);

        // then
        // updateDetails에 의해 내용이 변경되었는지 확인 (실제 엔티티 메서드 호출됨)


        assertThat(response.getContent()).isEqualTo("수정된 내용");
        assertThat(response.getEndedAt()).isEqualTo(updateRequest.endedAt());
        verify(milestoneRepository, times(1)).save(any(Milestone.class));
    }



    @DisplayName("Milestone 목록 조회 성공 테스트")
    @Test
    void testGetMilestones_success() {
        // given
        Project otherProject = new Project(2L, "다른 프로젝트", ProjectStatus.ACTIVE, null);
        Milestone m2 = new Milestone(11L, "M2", LocalDateTime.now(), LocalDateTime.now(), mockProject);
        List<Milestone> mockMilestoneList = List.of(mockMilestone, m2);

        // 1. ProjectRepository 모킹: 프로젝트 ID 1L로 조회 시 mockProject 반환
        when(projectRepository.findById(1L)).thenReturn(Optional.of(mockProject));
        // 2. MilestoneRepository 모킹: 프로젝트 ID 1L에 해당하는 목록 반환
        when(milestoneRepository.findByProjectId(1L)).thenReturn(mockMilestoneList);

        // when
        List<MilestoneResponse> responseList = milestoneService.getMilestones(1L);

        // then
        assertThat(responseList).hasSize(2);
        assertThat(responseList.stream().map(MilestoneResponse::getContent).collect(Collectors.toList()))
                .containsExactlyInAnyOrder("초기 마일스톤", "M2");

        verify(milestoneRepository, times(1)).findByProjectId(1L);
    }
}