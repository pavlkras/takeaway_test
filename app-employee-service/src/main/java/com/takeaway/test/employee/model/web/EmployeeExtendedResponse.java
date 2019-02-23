package com.takeaway.test.employee.model.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.takeaway.test.common.converters.LocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Getter
@ApiModel(description = "Extended response model providing detailed employee info",
        parent = EmployeeResponse.class)
public class EmployeeExtendedResponse extends EmployeeResponse {
    @ApiModelProperty(required = true,
            value = "email of employee requested",
            readOnly = true)
    private final String email;
    @ApiModelProperty(required = true,
            value = "employee's first name",
            readOnly = true)
    private final String firstName;
    @ApiModelProperty(required = true,
            value = "employee's last name",
            readOnly = true)
    private final String lastName;
    @ApiModelProperty(required = true,
            value = "employee's birthday in format {YYYY-MM-DD}",
            readOnly = true)
    @JsonSerialize(using = LocalDateSerializer.class)

    private final LocalDate birthDay;
    @ApiModelProperty(required = false,
            value = "employee's department id",
            readOnly = true)
    private final Integer departmentId;
    @ApiModelProperty(required = false,
            value = "employee's department extended info",
            readOnly = true)
    private final DepartmentExtendedResponse department;

    @Builder(builderMethodName = "extendedBuilder")
    public EmployeeExtendedResponse(String uuid, String email, String firstName, String lastName, LocalDate birthDay, Integer departmentId, DepartmentExtendedResponse department) {
        super(uuid);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.departmentId = departmentId;
        this.department = department;
    }
}
