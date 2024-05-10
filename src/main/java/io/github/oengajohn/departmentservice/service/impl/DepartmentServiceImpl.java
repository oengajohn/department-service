package io.github.oengajohn.departmentservice.service.impl;

import org.springframework.stereotype.Service;

import io.github.oengajohn.departmentservice.repository.DepartmentRepository;
import io.github.oengajohn.departmentservice.service.DepartmentService;
@Service
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;
    public DepartmentServiceImpl(DepartmentRepository departmentRepository){
        this.departmentRepository=departmentRepository;
    }
}
