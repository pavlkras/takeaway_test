package com.takeaway.test.employee.controllers;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.web.DepartmentExtendedResponse;
import com.takeaway.test.employee.model.web.CreateDepartmentRequest;
import com.takeaway.test.employee.model.web.DepartmentResponse;
import com.takeaway.test.employee.services.DepartmentService;
import io.swagger.annotations.*;
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
@Api(value = "Departments", description = "Operations related to Departments only")
public class DepartmentsController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentsController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @ApiOperation(value = "Create new department", response = DepartmentExtendedResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created"),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 401, message = "You are not authorized to perform request"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 500, message = "Internal unexpected error")
    })
    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentResponse> createDepartment(@Valid @RequestBody CreateDepartmentRequest payload,
                                                               @ApiParam(value = "Allows to return extended response",
                                                               defaultValue = "false",
                                                               allowableValues = "true, false",
                                                               type = "boolean")
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

    @ApiOperation(value = "Get department info", response = DepartmentExtendedResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource reached"),
            @ApiResponse(code = 400, message = "Request is invalid"),
            @ApiResponse(code = 404, message = "Resource you requested is not found"),
            @ApiResponse(code = 500, message = "Internal unexpected error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepartmentExtendedResponse> getDepartment(@ApiParam(value = "Id of requested resource", required = true) @PathVariable int id)
            throws PersistenceException {
        Department department = departmentService.getDepartment(id);

        return ResponseEntity
                .ok(DepartmentExtendedResponse.extendedBuilder()
                        .id(department.getId())
                        .name(department.getName()).build());
    }
}
