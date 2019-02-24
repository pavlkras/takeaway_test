package com.takeaway.test.employee.dao.impl;

import com.takeaway.test.common.exceptions.ConstraintException;
import com.takeaway.test.common.exceptions.DuplicateResourceException;
import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.entities.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class EmployeeDaoImplTest {
    @Autowired
    private EmployeeDaoImpl dao;

    @Autowired
    private DepartmentDaoImpl departmentDao;

    @Test
    public void create_validRequest_success() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee result = dao.create(employee);
        assertEquals("test@email.il", result.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthday());
        assertEquals("last_test", result.getLastName());
        assertEquals("first_test", result.getFirstName());
        assertEquals(department.getId(), result.getDepartmentId());
        assertNull(result.getDepartment());
        assertNotNull(result.getUuid());
    }

    @Test(expected = ConstraintException.class)
    public void create_nonValidField_throwException() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName(null)
                .firstName("first_test")
                .email("test@email.il")
                .build();
        dao.create(employee);
    }

    @Test(expected = DuplicateResourceException.class)
    public void create_duplicateEmail_throwException() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee.EmployeeBuilder employeeBuilder = Employee.builder()
                .email("test@email.il");
        dao.create(employeeBuilder
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .build());
        dao.create(employeeBuilder
                .departmentId(department.getId())
                .birthday(LocalDate.of(2002, 1, 1))
                .lastName("last_test_2")
                .firstName("first_test_2")
                .build());
    }

    @Test(expected = ConstraintException.class)
    public void create_departmentNotExists_throwException() throws PersistenceException {
        Employee employee = Employee.builder()
                .departmentId(Integer.MAX_VALUE)
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        dao.create(employee);
    }

    @Test
    public void read_validUuid_success() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = dao.create(employee);
        Employee result = dao.read(created.getUuid());
        assertEquals("test@email.il", result.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthday());
        assertEquals("last_test", result.getLastName());
        assertEquals("first_test", result.getFirstName());
        assertEquals(department.getId(), result.getDepartmentId());
        assertNull(result.getDepartment());
        assertNotNull(result.getUuid());
    }

    @Test
    public void read_validUuidExtended_success() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = dao.create(employee);
        Employee result = dao.read(created.getUuid(), true);
        assertEquals("test@email.il", result.getEmail());
        assertEquals(LocalDate.of(2000, 1, 1), result.getBirthday());
        assertEquals("last_test", result.getLastName());
        assertEquals("first_test", result.getFirstName());
        assertEquals(department.getId(), result.getDepartment().getId());
        assertEquals("dept_name", result.getDepartment().getName());
        assertNull(result.getDepartmentId());
        assertNotNull(result.getUuid());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void read_nonExistentUuid_throwException() throws PersistenceException {
        dao.read("nonexistent_uuid");
    }

    @Test
    public void update_fullUpdate_success() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = dao.create(employee);
        Employee result = dao.update(Employee.builder()
                .uuid(created.getUuid())
                .departmentId(department.getId())
                .birthday(LocalDate.of(1999, 1,1))
                .lastName("last_name_2")
                .firstName("first_name_2")
                .email("test_2@email.il").build());
        assertEquals("test_2@email.il", result.getEmail());
        assertEquals(LocalDate.of(1999, 1,1), result.getBirthday());
        assertEquals("last_name_2", result.getLastName());
        assertEquals("first_name_2", result.getFirstName());
        assertEquals(department.getId(), result.getDepartmentId());
        assertEquals(created.getUuid(), result.getUuid());
        assertNull(result.getDepartment());
    }

    @Test
    public void update_partialUpdate_success() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = dao.create(employee);
        Employee result = dao.update(Employee.builder()
                .uuid(created.getUuid())
                .lastName("last_name_2")
                .firstName("first_name_2")
                .build());
        assertEquals("test@email.il", result.getEmail());
        assertEquals(LocalDate.of(2000, 1,1), result.getBirthday());
        assertEquals("last_name_2", result.getLastName());
        assertEquals("first_name_2", result.getFirstName());
        assertEquals(department.getId(), result.getDepartmentId());
        assertEquals(created.getUuid(), result.getUuid());
        assertNull(result.getDepartment());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void update_nonExistingUuid_throwException() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        dao.update(Employee.builder()
                .uuid("nonexisting_uuid")
                .lastName("last_name_2")
                .firstName("first_name_2")
                .build());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void delete_nonExistentUuid_throwException() throws PersistenceException {
        String uuid = "test_uuid_nonexistent";
        dao.delete(uuid);
    }

    @Test
    public void delete_existentUuid_success() throws PersistenceException {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = dao.create(employee);
        String result =  dao.delete(created.getUuid());
        assertEquals(created.getUuid(), result);
    }
}