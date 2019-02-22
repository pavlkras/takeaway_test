package com.takeaway.test.employee.services.impl;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.dao.Dao;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.web.CreateDepartmentRequest;
import com.takeaway.test.employee.services.DepartmentService;
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
public class DepartmentServiceImpl implements DepartmentService {
    private final Dao<Department, Integer> dao;

    @Autowired
    public DepartmentServiceImpl(Dao<Department, Integer> dao) {
        this.dao = dao;
    }

    @Override
    public Department createDepartment(CreateDepartmentRequest request) throws PersistenceException {
        return dao.create(Department.builder()
                .name(request.getName())
                .build());
    }

    @Override
    public Department getDepartment(int id) throws PersistenceException {
        return dao.read(id);
    }
}
