package io.github.oengajohn.departmentservice.service;

import java.util.List;
import java.util.Map;

import io.github.oengajohn.departmentservice.model.DepartmentRequest;
import io.github.oengajohn.departmentservice.model.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest departmentRequest);

    List<DepartmentResponse> listAll();

    DepartmentResponse getByDepartmentNumber(Integer departmentNumber);

    DepartmentResponse updateDepartment(Integer departmentNumber, DepartmentRequest departmentRequest);

    Map<String, Object> deleteDepartment(Integer departmentNumber);

    List<DepartmentResponse> getDepartmentsByIds(List<Integer> ids);

}
