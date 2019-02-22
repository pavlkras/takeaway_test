package com.takeaway.test.employee.services.impl;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.dao.EmployeeDao;
import com.takeaway.test.employee.model.entities.Employee;
import com.takeaway.test.employee.model.web.CreateEmployeeRequest;
import com.takeaway.test.employee.model.web.UpdateEmployeeRequest;
import com.takeaway.test.employee.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao dao;

    @Autowired
    public EmployeeServiceImpl(EmployeeDao dao) {
        this.dao = dao;
    }

    @Override
    public Employee createEmployee(CreateEmployeeRequest request) throws PersistenceException {
        return dao.create(Employee.builder()
            .email(request.getEmail())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .birthday(request.getBirthDay())
            .departmentId(request.getDepartmentId())
            .build());
    }

    @Override
    public Employee getEmployee(String uuid, Boolean returnExtended) throws PersistenceException {
        return dao.read(uuid, returnExtended);
    }

    @Override
    public Employee updateEmployee(UpdateEmployeeRequest request) throws PersistenceException {
        return dao.update(Employee.builder()
                .uuid(request.getUuid())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .birthday(request.getBirthDay())
                .departmentId(request.getDepartmentId())
                .build());
    }

    @Override
    public String deleteEmployee(String uuid) throws PersistenceException {
        return dao.delete(uuid);
    }
}
