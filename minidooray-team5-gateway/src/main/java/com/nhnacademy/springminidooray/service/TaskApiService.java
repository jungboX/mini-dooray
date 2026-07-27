package com.nhnacademy.springminidooray.service;

import com.nhnacademy.springminidooray.model.ProjectCreateRequest;
import com.nhnacademy.springminidooray.model.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskApiService {
    @Value("${api.task.url}")
    private String TASK_API_URL;

    private final RestTemplate restTemplate;

    public List<ProjectResponse> getProjectList(String username) {
        String requestUrl = TASK_API_URL + "/api/projects?username=" + username;

        // List<ProjectResponse> 제네릭 타입 정보를 담는 참조체 생성
        ParameterizedTypeReference<List<ProjectResponse>> responseType =
                new ParameterizedTypeReference<List<ProjectResponse>>() {};

        try {
            // exchange 메서드로 API 호출
            // GET 요청 -> 요청 본문은 null로 전송
            return restTemplate.exchange(requestUrl, HttpMethod.GET, null, responseType).getBody();
        } catch (HttpClientErrorException e) {
            log.debug("프로젝트 목록 조회 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public ProjectResponse createProject(ProjectCreateRequest createRequest) {
        String requestUrl = TASK_API_URL + "/api/projects";

        try {
            return restTemplate.postForObject(requestUrl, createRequest, ProjectResponse.class);
        } catch (HttpClientErrorException e) {
            log.debug("프로젝트 등록 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }

    }

    public ProjectDetailResponse getProjectDetails(int projectId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId;

        try {
            return restTemplate.getForObject(requestUrl, ProjectDetailResponse.class);
        } catch (HttpClientErrorException e) {
            log.debug("프로젝트 조회 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public TaskDetailResponse getTaskDetails(int projectId, int taskId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId;

        try {
            return restTemplate.getForObject(requestUrl, TaskDetailResponse.class);
        } catch (HttpClientErrorException e) {
            log.debug("테스크 조회 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public List<MilestoneResponse> getMilestoneList(int projectId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/milestones";

        ParameterizedTypeReference<List<MilestoneResponse>> responseType =
                new ParameterizedTypeReference<List<MilestoneResponse>>() {};

        try {
            return restTemplate.exchange(requestUrl, HttpMethod.GET, null, responseType).getBody();
        } catch (HttpClientErrorException e) {
            log.debug("마일스톤 목록 조회 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void createMilestone(MilestoneCreateRequest request) {
        String requestUrl = TASK_API_URL + "/api/projects/" + request.projectId() + "/milestones";

        try {
            restTemplate.postForObject(requestUrl, request, MilestoneResponse.class);

        } catch (HttpClientErrorException e) {
            log.debug("마일스톤 생성 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void createTask(TaskCreateRequest request) {
        String requestUrl = TASK_API_URL + "/api/projects/" + request.projectId() + "/tasks";

        try {
            restTemplate.postForObject(requestUrl, request, TaskDto.class);
        } catch (HttpClientErrorException e) {
            log.debug("태스크 생성 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void updateTask(int projectId, int taskId, TaskUpdateRequest request) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId;

        try {
            restTemplate.put(requestUrl, request);
        } catch (HttpClientErrorException e) {
            log.debug("태스크 업데이트 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteTask(int projectId, int taskId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId;

        try {
            restTemplate.delete(requestUrl);
        } catch (HttpClientErrorException e) {
            log.debug("태스크 삭제 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public List<TagResponse> getTagList(int projectId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tags";

        ParameterizedTypeReference<List<TagResponse>> responseType =
                new ParameterizedTypeReference<List<TagResponse>>() {};

        try {
            return restTemplate.exchange(requestUrl, HttpMethod.GET, null, responseType).getBody();
        } catch (HttpClientErrorException e) {
            log.debug("태그 목록 조회 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public List<TagResponse> getTagListByTaskId(int projectId, int taskId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId + "/tags";

        ParameterizedTypeReference<List<TagResponse>> responseType =
                new ParameterizedTypeReference<List<TagResponse>>() {};
        
        try {
            return restTemplate.exchange(requestUrl, HttpMethod.GET, null, responseType).getBody();
        } catch (HttpClientErrorException e) {
            log.debug("태스크 태그 목록 조회 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteTag(int projectId, int tagId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tags/" + tagId;

        try {
            restTemplate.delete(requestUrl);
        } catch (HttpClientErrorException e) {
            log.debug("태그 삭제 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void createTag(int projectId, TagCreateRequest request) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tags";

        try {
            restTemplate.postForObject(requestUrl, request, Void.class);
        } catch (HttpClientErrorException e) {
            log.debug("태그 생성 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

//    public List<CommentResponse> getCommentList(int projectId, int taskId) {
//        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId + "/comments";
//
//        ParameterizedTypeReference<List<CommentResponse>> responseType =
//                new ParameterizedTypeReference<List<CommentResponse>>() {};
//
//        try {
//            return restTemplate.exchange(requestUrl, HttpMethod.GET, null, responseType).getBody();
//        } catch (HttpClientErrorException e) {
//            log.debug("댓글 목록 조회 시도 중 오류 발생: {}", e.getMessage());
//            throw e;
//        }
//    }

    public void createComment(int projectId, int taskId, CommentCreateRequest request) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId + "/comments";

        try {
            restTemplate.postForObject(requestUrl, request, CommentResponse.class);
        } catch (HttpClientErrorException e) {
            log.debug("댓글 생성 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void updateComment(int projectId, int taskId, int commentId, String content) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId + "/comments/" + commentId;

        CommentUpdateRequest request = new CommentUpdateRequest(content);
        try {
            HttpEntity<CommentUpdateRequest> requestEntity = new HttpEntity<>(request);
            restTemplate.exchange(requestUrl, HttpMethod.PUT, requestEntity, Void.class);
        } catch (HttpClientErrorException e) {
            log.debug("댓글 업데이트 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteComment(int projectId, int taskId, int commentId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/tasks/" + taskId + "/comments/" + commentId;

        try {
            restTemplate.delete(requestUrl);
        } catch (HttpClientErrorException e) {
            log.debug("댓글 삭제 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteMilestone(int projectId, int milestoneId) {
        String requestUrl = TASK_API_URL + "/api/projects/" + projectId + "/milestones/" + milestoneId;

        try {
            restTemplate.delete(requestUrl);
        } catch (HttpClientErrorException e) {
            log.debug("마일스톤 삭제 시도 중 오류 발생: {}", e.getMessage());
            throw e;
        }
    }
}
