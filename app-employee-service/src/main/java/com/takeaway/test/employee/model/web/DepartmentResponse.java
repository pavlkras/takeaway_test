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
@ApiModel(description = "Basic response model providing department id",
    subTypes = DepartmentExtendedResponse.class)
public class DepartmentResponse {
    @ApiModelProperty(required = true,
        value = "Id of department requested",
        readOnly = true)
    private final int id;
}
