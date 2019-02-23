package com.takeaway.test.employee.model.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Data
@ApiModel(description = "Map of attributes required to create department")
public class CreateDepartmentRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    @ApiModelProperty(required = true,
        value = "Name of departemnt to be created")
    private String name;
}
