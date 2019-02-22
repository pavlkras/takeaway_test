package com.takeaway.test.employee.model.entities;

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
@Builder
public class Employee implements Entity {
    private final String uuid;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final Integer departmentId;
    private final Department department;
}
