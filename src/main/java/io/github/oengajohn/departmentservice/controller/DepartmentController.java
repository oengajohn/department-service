package io.github.oengajohn.departmentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.oengajohn.departmentservice.model.DepartmentRequest;
import io.github.oengajohn.departmentservice.model.DepartmentResponse;
import io.github.oengajohn.departmentservice.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/department")
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public DepartmentResponse createDepartment(@RequestBody DepartmentRequest departmentRequest) {
        return departmentService.createDepartment(departmentRequest);
    }

    @GetMapping
    List<DepartmentResponse> list() {
        return departmentService.listAll();
    }

    @GetMapping("{departmentNumber}")
    public DepartmentResponse getByDepartmentNumber(@PathVariable Integer departmentNumber) {
        log.info("Getting information -> getByDepartmentNumber");
        return departmentService.getByDepartmentNumber(departmentNumber);
    }

    @PutMapping("{departmentNumber}")
    public DepartmentResponse update(@PathVariable Integer departmentNumber,
            @RequestBody DepartmentRequest departmentRequest) {
        return departmentService.updateDepartment(departmentNumber, departmentRequest);
    }

    @DeleteMapping("{departmentNumber}")
    public Map<String, Object> deleteDepartment(@PathVariable Integer departmentNumber) {
        return departmentService.deleteDepartment(departmentNumber);
    }

    @PostMapping("batch")
    public List<DepartmentResponse> getDepartmentsByIds(@RequestBody List<Integer> ids) {
        log.info("Getting information -> getDepartmentsByIds");
        return departmentService.getDepartmentsByIds(ids);
    }

}
