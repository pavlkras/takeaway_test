package com.takeaway.test.employee.controllers;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.*;
import com.takeaway.test.employee.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 22/02/2019
 */

@RestController
@RequestMapping("/employee")
public class EmployeesController {
    @Autowired
    private EmployeeService employeeService;


    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest payload,
                                                           @RequestParam(name = "extended", defaultValue = "false", required = false) Boolean returnExtended)
            throws PersistenceException {
        Employee employee = employeeService.createEmployee(payload);

        EmployeeResponse response = returnExtended
                ? EmployeeExtendedResponse.extendedBuilder()
                    .uuid(employee.getUuid())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .birthDay(employee.getBirthday())
                    .departmentId(employee.getDepartmentId())
                    .build()
                : EmployeeResponse.builder()
                    .uuid(employee.getUuid())
                    .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(path = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeExtendedResponse> getEmployee(@PathVariable String uuid,
                                                                @RequestParam(name = "extended", defaultValue = "false", required = false) Boolean returnExtended)
            throws PersistenceException {
        Employee employee = employeeService.getEmployee(uuid, returnExtended);

        return ResponseEntity
                .ok(EmployeeExtendedResponse.extendedBuilder()
                    .uuid(employee.getUuid())
                .email(employee.getEmail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .birthDay(employee.getBirthday())
                .departmentId(employee.getDepartmentId())
                .department(returnExtended
                        ? DepartmentExtendedResponse.extendedBuilder()
                            .id(employee.getDepartment().getId())
                            .name(employee.getDepartment().getName())
                            .build()
                        : null)
                .build());
    }

    @PostMapping(path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeExtendedResponse> updateEmployee(@Valid @RequestBody UpdateEmployeeRequest payload)
            throws PersistenceException {
        Employee employee = employeeService.updateEmployee(payload);

        return ResponseEntity
                .ok(EmployeeExtendedResponse.extendedBuilder()
                    .uuid(employee.getUuid())
                    .firstName(employee.getFirstName())
                    .lastName(employee.getLastName())
                    .email(employee.getEmail())
                    .birthDay(employee.getBirthday())
                    .departmentId(employee.getDepartmentId())
                    .build());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String uuid) throws PersistenceException {
        String deletedUuid = employeeService.deleteEmployee(uuid);

        return ResponseEntity.ok(deletedUuid);
    }
}
