package com.takeaway.test.employee.services;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.web.CreateDepartmentRequest;


/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public interface DepartmentService {
    Department createDepartment(CreateDepartmentRequest request) throws PersistenceException;

    Department getDepartment(int id) throws PersistenceException;
}
