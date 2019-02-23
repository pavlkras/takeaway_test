package com.takeaway.test.employee.controllers;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.*;
import com.takeaway.test.employee.services.EmployeeService;
import io.swagger.annotations.*;
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
@Api(value = "Employees", description = "Operations related to Employees only")
public class EmployeesController {
    @Autowired
    private EmployeeService employeeService;

    @ApiOperation(value = "Create new employee", response = EmployeeResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "You are not authorized to perform request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 500, message = "Internal unexpected error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "extended",
                    value = "Allows to return extended response",
                    defaultValue = "false",
                    allowableValues = "true, false",
                    dataType = "boolean")
    })
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

    @ApiOperation(value = "Get employee info", response = EmployeeExtendedResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource reached"),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 404, message = "Resource you requested is not found"),
            @ApiResponse(code = 500, message = "Internal unexpected error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid",
                    value = "UUID of requested resource",
                    required = true),
            @ApiImplicitParam(name = "extended",
                    value = "Allows to return extended response",
                    defaultValue = "false",
                    allowableValues = "true, false",
                    dataType = "boolean")
    })
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

    @ApiOperation(value = "Update employee", response = EmployeeExtendedResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated"),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 404, message = "Resource you try to update is not found"),
            @ApiResponse(code = 500, message = "Internal unexpected error")
    })
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

    @ApiOperation(value = "Delete employee", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee deleted"),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 404, message = "Resource you requested is not found"),
            @ApiResponse(code = 500, message = "Internal unexpected error")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid",
                    value = "UUID of requested resource",
                    required = true)
    })
    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String uuid) throws PersistenceException {
        String deletedUuid = employeeService.deleteEmployee(uuid);

        return ResponseEntity.ok(deletedUuid);
    }
}
