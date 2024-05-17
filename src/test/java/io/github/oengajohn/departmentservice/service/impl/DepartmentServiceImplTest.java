package io.github.oengajohn.departmentservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import io.github.oengajohn.departmentservice.entity.Department;
import io.github.oengajohn.departmentservice.model.DepartmentRequest;
import io.github.oengajohn.departmentservice.model.DepartmentResponse;
import io.github.oengajohn.departmentservice.repository.DepartmentRepository;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    private ModelMapper modelMapper = new ModelMapper();

    private DepartmentServiceImpl departmentServiceImpl;

    @BeforeEach
    void setup() {
        departmentServiceImpl = new DepartmentServiceImpl(departmentRepository, modelMapper);
    }

    @Test
    void testCreateDepartment() {
        // given
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .departmentName("Computer Science")
                .build();
        // when
        when(departmentRepository.save(any(Department.class))).thenReturn(
                Department.builder()
                        .departmentName("Computer Science")
                        .departmentNumber(1)
                        .build());
        DepartmentResponse actualDepartmentResponse = departmentServiceImpl.createDepartment(departmentRequest);
        // then
        DepartmentResponse expectedResponse = DepartmentResponse.builder()
                .departmentName("Computer Science")
                .departmentNumber(1)
                .build();
        assertThat(actualDepartmentResponse.getDepartmentNumber()).isNotNull();
        assertThat(actualDepartmentResponse.getDepartmentName()).isEqualTo(expectedResponse.getDepartmentName());
        assertThat(actualDepartmentResponse.getDepartmentNumber()).isEqualTo(1);

    }

    @Test
    void testListAll() {
        // given
        // when
        departmentServiceImpl.listAll();
        // then
        verify(departmentRepository).findAll();

    }

    @Test
    void testGetByDepartmentNumberShouldHaveValueWhenFound() {
        // given
        Integer departmentNumber = 1;
        Department department = Department.builder()
                .departmentName("Computer Science")
                .departmentNumber(1)
                .build();
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.of(department));
        // when
        DepartmentResponse departmentResponse = departmentServiceImpl.getByDepartmentNumber(departmentNumber);
        // then
        assertThat(departmentResponse.getDepartmentNumber()).isEqualTo(departmentNumber);
        assertThat(departmentResponse.getDepartmentName()).isEqualTo(department.getDepartmentName());

    }

    @Test
    void testGetByDepartmentNumberShouldThrowARuntimeExceptionWhenNotFound() {
        // given
        Integer departmentNumber = 1;
        when(departmentRepository.findById(anyInt())).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> departmentServiceImpl.getByDepartmentNumber(departmentNumber))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Department with " + departmentNumber + " does not exist");

    }

    @Test
    void testDeleteDepartment() {
        // given
        Integer departmentNumber = 1;
        Map<String, Object> deleteDepartment = departmentServiceImpl.deleteDepartment(departmentNumber);
        verify(departmentRepository).deleteById(departmentNumber);
        assertThat(deleteDepartment).containsKey("success");
        assertThat(deleteDepartment).containsValue(true);
    }

    @Test
    void testUpdateDepartment() {
        // given
        Integer departmentNumber = 1;
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .departmentName("Information Technology")
                .build();
        Department department = Department.builder()
                .departmentName("Computer Science")
                .departmentNumber(1)
                .build();
        when(departmentRepository.findById(eq(departmentNumber))).thenReturn(Optional.of(department));
        when(departmentRepository.findByDepartmentName(eq(departmentRequest.getDepartmentName())))
                .thenReturn(Optional.empty());// not taken
        when(departmentRepository.save(any(Department.class))).thenReturn(
                Department.builder()
                        .departmentName("Information Technology")
                        .departmentNumber(1)
                        .build());
        // when
        DepartmentResponse departmentResponse = departmentServiceImpl.updateDepartment(departmentNumber,
                departmentRequest);
        assertThat(departmentResponse.getDepartmentName()).isEqualTo(departmentRequest.getDepartmentName());

    }

    @Test
    void testUpdateDepartmentWhenNameAlreadyExists() {
        // given
        Integer departmentNumber = 1;
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .departmentName("Information Technology")
                .build();
        Department department = Department.builder()
                .departmentName("Information Technology")
                .departmentNumber(1)
                .build();
        when(departmentRepository.findById(eq(departmentNumber))).thenReturn(Optional.of(department));
        when(departmentRepository.findByDepartmentName(eq(departmentRequest.getDepartmentName())))
                .thenReturn(Optional.of(department));// taken
        // when
        assertThatThrownBy(() -> departmentServiceImpl.updateDepartment(departmentNumber,
                departmentRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Department name  " + departmentRequest.getDepartmentName() + " has already been taken");
        verify(departmentRepository, never()).save(any());

    }

    @Test
    void testUpdateDepartmentWhenDepartmentDoesNotExist() {
        // given
        Integer departmentNumber = 1;
        DepartmentRequest departmentRequest = DepartmentRequest.builder()
                .departmentName("Information Technology")
                .build();
        when(departmentRepository.findById(eq(departmentNumber))).thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> departmentServiceImpl.updateDepartment(departmentNumber,
                departmentRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Department with " + departmentNumber + " does not exist");
        verify(departmentRepository, never()).save(any());
        verify(departmentRepository, never()).findByDepartmentName(anyString());

    }
}
