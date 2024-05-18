package io.github.oengajohn.departmentservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import io.github.oengajohn.departmentservice.AbstractTestContainersTest;
import io.github.oengajohn.departmentservice.model.DepartmentRequest;
import io.github.oengajohn.departmentservice.model.DepartmentResponse;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DepartmentControllerTest extends AbstractTestContainersTest {

        private static final String API_BASE_PATH = "/api/department";

        @Autowired
        TestRestTemplate testRestTemplate;

        @Test
        void testCreateDepartment() {
                // given
                DepartmentRequest departmentRequest = DepartmentRequest.builder()
                                .departmentName("Business Commerce")
                                .build();
                // when
                ResponseEntity<DepartmentResponse> response = testRestTemplate.exchange(
                                API_BASE_PATH,
                                POST,
                                new HttpEntity<>(departmentRequest),
                                new ParameterizedTypeReference<DepartmentResponse>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                // make a call to list the data
                ResponseEntity<List<DepartmentResponse>> listResponse = testRestTemplate.exchange(
                                API_BASE_PATH,
                                GET,
                                null,
                                new ParameterizedTypeReference<List<DepartmentResponse>>() {
                                });
                assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
                DepartmentResponse createdDepartmentResponse = Objects.requireNonNull(listResponse.getBody())
                                .stream()
                                .filter(dr -> dr.getDepartmentName().equals(departmentRequest.getDepartmentName()))
                                .findFirst()
                                .orElseThrow();
                assertThat(createdDepartmentResponse.getDepartmentNumber()).isNotNull();
                assertThat(createdDepartmentResponse.getDepartmentName())
                                .isEqualTo(departmentRequest.getDepartmentName());

        }

        @Test
        void testDeleteDepartment() {
                // given
                DepartmentRequest departmentRequest = DepartmentRequest.builder()
                                .departmentName("Information Technology")
                                .build();
                // when
                ResponseEntity<DepartmentResponse> response = testRestTemplate.exchange(
                                API_BASE_PATH,
                                POST,
                                new HttpEntity<>(departmentRequest),
                                new ParameterizedTypeReference<DepartmentResponse>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                DepartmentResponse departmentResponse = Objects.requireNonNull(response.getBody());
                ResponseEntity<Map<String, Object>> deleteResponse = testRestTemplate.exchange(
                                API_BASE_PATH + "/" + departmentResponse.getDepartmentNumber(),
                                DELETE,
                                null,
                                new ParameterizedTypeReference<Map<String, Object>>() {
                                });
                Map<String, Object> deleteResponseMap = Objects.requireNonNull(deleteResponse.getBody());
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(deleteResponseMap).containsKey("success");
                assertThat(deleteResponseMap).containsValue(true);

        }

        @Test
        void testGetByDepartmentNumber() {
                // given
                DepartmentRequest departmentRequest = DepartmentRequest.builder()
                                .departmentName("Computer Science")
                                .build();
                // when
                ResponseEntity<DepartmentResponse> response = testRestTemplate.exchange(
                                API_BASE_PATH,
                                POST,
                                new HttpEntity<>(departmentRequest),
                                new ParameterizedTypeReference<DepartmentResponse>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                DepartmentResponse departmentResponse = Objects.requireNonNull(response.getBody());
                ResponseEntity<DepartmentResponse> getByDepartmentNumberResponse = testRestTemplate.exchange(
                                API_BASE_PATH + "/" + departmentResponse.getDepartmentNumber(),
                                GET,
                                null,
                                new ParameterizedTypeReference<DepartmentResponse>() {
                                });
                assertThat(getByDepartmentNumberResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
                DepartmentResponse departmentByNumber = Objects.requireNonNull(getByDepartmentNumberResponse.getBody());
                assertThat(departmentByNumber.getDepartmentNumber())
                                .isEqualTo(departmentResponse.getDepartmentNumber());
        }

        @Test
        void testUpdate() {
                // given
                DepartmentRequest departmentRequest = DepartmentRequest.builder()
                                .departmentName("Engineering")
                                .build();
                // when
                ResponseEntity<DepartmentResponse> response = testRestTemplate.exchange(
                                API_BASE_PATH,
                                POST,
                                new HttpEntity<>(departmentRequest),
                                new ParameterizedTypeReference<DepartmentResponse>() {
                                });
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                DepartmentResponse departmentResponse = Objects.requireNonNull(response.getBody());
                ResponseEntity<DepartmentResponse> updateResponse = testRestTemplate.exchange(
                                API_BASE_PATH + "/" + departmentResponse.getDepartmentNumber(),
                                PUT,
                                new HttpEntity<>(DepartmentRequest.builder().departmentName("Tourism and Hospitality")
                                                .build()),
                                new ParameterizedTypeReference<DepartmentResponse>() {
                                });
                assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
                DepartmentResponse updatedDepartmentResponse = Objects.requireNonNull(updateResponse.getBody());
                assertThat(updatedDepartmentResponse.getDepartmentName()).isEqualTo("Tourism and Hospitality");
                assertThat(updatedDepartmentResponse.getDepartmentNumber())
                                .isEqualTo(departmentResponse.getDepartmentNumber());

        }
}
