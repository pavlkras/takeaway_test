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
@ApiModel(description = "Map of attributes required to create employee")
public class CreateEmployeeRequest {
    @Email
    @NotNull
    @Size(min = 3, max = 254)
    @ApiModelProperty(required = true,
            value = "email of employee",
            example = "mail@email.test")
    private String email;

    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    @ApiModelProperty(required = true,
            value = "employee's first name")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    @ApiModelProperty(required = true,
            value = "employee's last name")
    private String lastName;

    @NotNull
    @Positive
    @ApiModelProperty(required = true,
            value = "department id")
    private Integer departmentId;

    @NotNull
    @Past
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @ApiModelProperty(required = true,
            value = "employee's birthday in format {YYYY-MM-DD}",
            example = "2019-02-23")
    private LocalDate birthDay;
}
