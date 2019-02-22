package com.takeaway.test.employee.model.web;

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
public class CreateDepartmentRequest {

    @NotNull
    @Size(min = 1, max = 100)
    @NotBlank
    private String name;
}
