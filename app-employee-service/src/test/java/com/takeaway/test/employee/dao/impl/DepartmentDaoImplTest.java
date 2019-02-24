package com.takeaway.test.employee.dao.impl;

import com.takeaway.test.common.exceptions.ConstraintException;
import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.model.entities.Department;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class DepartmentDaoImplTest {
    @Autowired
    private DepartmentDaoImpl dao;

    @Test
    public void create_filledName_success() throws PersistenceException {
        Department department = Department.builder()
                .name("test_dept")
                .build();
        Department result = dao.create(department);
        assertEquals("test_dept", result.getName());
    }

    @Test
    public void create_emptyName_success() throws PersistenceException {
        Department department = Department.builder()
                .name("")
                .build();
        Department result = dao.create(department);
        assertEquals("", result.getName());
    }

    @Test(expected = ConstraintException.class)
    public void create_nullName_throwException() throws PersistenceException {
        Department department = Department.builder()
                .build();
        dao.create(department);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void read_nonExistentId_throwException() throws PersistenceException {
        Integer id = Integer.MAX_VALUE;
        dao.read(id);
    }

    @Test
    public void read_existentId_success() throws PersistenceException {
        Department department = Department.builder()
                .name("test_dept")
                .build();
        Department created = dao.create(department);
        Department result = dao.read(created.getId());
        assertEquals(created.getName(), result.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void update_throwsException() throws Exception {
        Department department = Department.builder().build();
        dao.update(department);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete_throwsException() throws Exception {
        dao.delete(1);
    }
}