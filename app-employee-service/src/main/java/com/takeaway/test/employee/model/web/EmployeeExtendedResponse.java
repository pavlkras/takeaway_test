package com.takeaway.test.employee.model.web;

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
public class EmployeeExtendedResponse extends EmployeeResponse {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthDay;
    private final Integer departmentId;
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
