package com.takeaway.test.employee.model.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Extended response model providing department name, id",
    parent = DepartmentResponse.class)
public class DepartmentExtendedResponse extends DepartmentResponse {
    @ApiModelProperty(required = true,
            value = "Name of department requested",
            readOnly = true)
    private final String name;

    @Builder(builderMethodName = "extendedBuilder")
    public DepartmentExtendedResponse(int id, String name) {
        super(id);
        this.name = name;
    }
}
