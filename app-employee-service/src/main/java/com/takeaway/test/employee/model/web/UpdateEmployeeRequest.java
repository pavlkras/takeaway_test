package com.takeaway.test.employee.model.web;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Data
@ApiModel(description = "Update employee data request")
public class UpdateEmployeeRequest {
    @NotBlank
    @ApiModelProperty(required = true,
            value = "UUID of employee to be modified")
    private String uuid;

    @Email
    @Size(min = 3, max = 254)
    @ApiModelProperty(required = false,
            value = "email of employee requested",
            example = "email@mail.test")
    private String email;

    @Size(min = 1, max = 100)
    @ApiModelProperty(required = false,
            value = "employee's first name")
    private String firstName;

    @Size(min = 1, max = 100)
    @ApiModelProperty(required = false,
            value = "employee's first name")
    private String lastName;

    @Positive
    @ApiModelProperty(required = false,
            value = "employee's department id",
            readOnly = true)
    private Integer departmentId;

    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(required = false,
            value = "employee's birthday in format {YYYY-MM-DD}",
            example = "2019-02-23")
    private LocalDate birthDay;

}
