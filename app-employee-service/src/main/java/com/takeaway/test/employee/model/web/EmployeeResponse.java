package com.takeaway.test.employee.model.web;

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
public class EmployeeResponse {
    private final String uuid;
}
