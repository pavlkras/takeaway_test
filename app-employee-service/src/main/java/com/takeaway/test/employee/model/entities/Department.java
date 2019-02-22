package com.takeaway.test.employee.model.entities;

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
@Builder
public class Department implements Entity {
    private final Integer id;
    private final String name;
}
