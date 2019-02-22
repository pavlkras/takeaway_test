package com.takeaway.test.employee.controllers;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.web.DepartmentExtendedResponse;
import com.takeaway.test.employee.model.web.CreateDepartmentRequest;
import com.takeaway.test.employee.model.web.DepartmentResponse;
import com.takeaway.test.employee.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 21/02/2019
 */

@RestController
@RequestMapping("/department")
public class DepartmentsController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentsController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody CreateDepartmentRequest payload,
                                                               @RequestParam(name = "extended", defaultValue = "false", required = false) Boolean returnExtended)
            throws PersistenceException {
        Department department = departmentService.createDepartment(payload);

        DepartmentResponse response = returnExtended
                ? DepartmentExtendedResponse.extendedBuilder().id(department.getId()).name(department.getName()).build()
                : DepartmentResponse.builder().id(department.getId()).build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, "/departments/" + response.getId())
                .body(response);
    }

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentExtendedResponse> getDepartment(@PathVariable int id)
            throws PersistenceException {
        Department department = departmentService.getDepartment(id);

        return ResponseEntity
                .ok(DepartmentExtendedResponse.extendedBuilder()
                        .id(department.getId())
                        .name(department.getName()).build());
    }
}
