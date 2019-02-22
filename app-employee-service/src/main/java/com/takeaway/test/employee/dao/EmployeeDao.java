package com.takeaway.test.employee.dao;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Employee;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public interface EmployeeDao extends Dao<Employee, String> {
    Employee read(String key, boolean extended) throws PersistenceException;

    @Override
    default Employee read(String key) throws PersistenceException {
        return read(key, false);
    }
}
