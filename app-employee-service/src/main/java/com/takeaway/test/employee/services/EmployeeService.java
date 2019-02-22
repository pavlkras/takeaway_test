package com.takeaway.test.employee.services;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.CreateEmployeeRequest;
import com.takeaway.test.employee.model.web.UpdateEmployeeRequest;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public interface EmployeeService {
    Employee createEmployee(CreateEmployeeRequest request) throws PersistenceException;
    Employee getEmployee(String uuid, Boolean returnExtended) throws PersistenceException;
    Employee updateEmployee(UpdateEmployeeRequest payload) throws PersistenceException;
    String deleteEmployee(String uuid) throws PersistenceException;
}
