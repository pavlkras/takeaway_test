package com.takeaway.test.employee.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.dao.impl.DepartmentDaoImpl;
import com.takeaway.test.employee.dao.impl.EmployeeDaoImpl;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.entities.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
@Transactional
public class EmployeesControllerTest {
    private static final String URI = "/employee";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DepartmentDaoImpl departmentDao;

    @Autowired
    private EmployeeDaoImpl employeeDao;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void createEmployee() {
    }

    @Test
    public void getEmployee() {
    }

    @Test
    public void updateEmployee() {
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void deleteEmployee_existingUuid_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("dept_name").build());
        Employee employee = Employee.builder()
                .departmentId(department.getId())
                .birthday(LocalDate.of(2000, 1, 1))
                .lastName("last_test")
                .firstName("first_test")
                .email("test@email.il")
                .build();
        Employee created = employeeDao.create(employee);
        MvcResult result = mvc.perform(delete(URI + "/" + created.getUuid())).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(created.getUuid(), result.getResponse().getContentAsString());
    }

    @Test
    public void deleteEmployee_authFailed_failed() throws Exception {
        MvcResult result = mvc.perform(delete(URI + "/some-uuid")).andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }

    private <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, clazz);
    }

    private <T> String mapToJson(T entity) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(entity);
    }

}