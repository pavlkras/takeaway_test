package com.takeaway.test.employee.services.impl;

import com.takeaway.test.common.exceptions.ConstraintException;
import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.dao.impl.DepartmentDaoImpl;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.web.CreateDepartmentRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class DepartmentServiceImplTest {
    @Autowired
    private DepartmentServiceImpl departmentService;

    @MockBean
    private DepartmentDaoImpl dao;

    @Test
    public void createDepartment_validName_success() throws PersistenceException {
        Mockito.when(dao.create(any(Department.class))).thenReturn(Department.builder().build());
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("valid_name");

        Department department = departmentService.createDepartment(request);
        assertNotNull(department);
    }

    @Test(expected = ConstraintException.class)
    public void createDepartment_invalidName_throwException() throws PersistenceException {
        Mockito.when(dao.create(any(Department.class))).thenThrow(ConstraintException.class);
        CreateDepartmentRequest request = new CreateDepartmentRequest();

        departmentService.createDepartment(request);
    }

    @Test
    public void getDepartment_exisingId_success() throws PersistenceException {
        Mockito.when(dao.read(anyInt())).thenReturn(Department.builder().build());
        Department department = departmentService.getDepartment(1);
        assertNotNull(department);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void getDepartment_nonExistingId_throwException() throws PersistenceException {
        Mockito.when(dao.read(anyInt())).thenThrow(ResourceNotFoundException.class);
        departmentService.getDepartment(1);
    }
}