package com.takeaway.test.employee.model.web;

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
public class DepartmentExtendedResponse extends DepartmentResponse {
    private final String name;

    @Builder(builderMethodName = "extendedBuilder")
    public DepartmentExtendedResponse(int id, String name) {
        super(id);
        this.name = name;
    }
}
