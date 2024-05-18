package io.github.oengajohn.departmentservice.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import io.github.oengajohn.departmentservice.AbstractTestContainersTest;
import io.github.oengajohn.departmentservice.entity.Department;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class DepartmentRepositoryTest extends AbstractTestContainersTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setup() {
        Department department = Department.builder().departmentName("Computer Science").build();
        departmentRepository.save(department);
    }

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAll();
    }

    @Test
    void shouldFindByDepartmentName() {
        // given
        // when
        Optional<Department> byDepartmentName = departmentRepository.findByDepartmentName("Computer Science");
        // then
        assertThat(byDepartmentName).isPresent();

    }

    @Test
    void shouldNotFindByDepartmentName() {
        // given
        // when
        Optional<Department> byDepartmentName = departmentRepository.findByDepartmentName("Information Technology");
        // then
        assertThat(byDepartmentName).isNotPresent();
    }
}
