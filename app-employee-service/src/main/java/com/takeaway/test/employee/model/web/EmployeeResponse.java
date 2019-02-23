package com.takeaway.test.employee.model.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Getter
@AllArgsConstructor
@Builder
@ApiModel(description = "Basic response model providing employee's uuid",
        subTypes = EmployeeExtendedResponse.class)
public class EmployeeResponse {
    @ApiModelProperty(required = true,
            value = "UUID of employee requested",
            readOnly = true)
    private final String uuid;
}
