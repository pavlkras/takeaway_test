package com.takeaway.test.employee.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.takeaway.test.employee.Application;
import com.takeaway.test.employee.dao.impl.DepartmentDaoImpl;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.web.CreateDepartmentRequest;
import com.takeaway.test.employee.model.web.DepartmentExtendedResponse;
import com.takeaway.test.employee.model.web.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@WebAppConfiguration
@Transactional
public class DepartmentsControllerTest {
    private static final String URI = "/department";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DepartmentDaoImpl departmentDao;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createDepartment_authDone_success() throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("department");
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createDepartment_authDoneExtended_success() throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("department");
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)).param("extended", "true")).andReturn();
        assertEquals(201, result.getResponse().getStatus());
        DepartmentExtendedResponse response = mapFromJson(result.getResponse().getContentAsString(), DepartmentExtendedResponse.class);
        assertEquals("department", response.getName());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void createDepartment_veryLongName_failed() throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("departmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartmentdepartment");
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(400, result.getResponse().getStatus());
        assertEquals(HttpStatus.BAD_REQUEST, mapFromJson(result.getResponse().getContentAsString(), ErrorResponse.class).getStatus());
        assertEquals("Input arguments validation failed", mapFromJson(result.getResponse().getContentAsString(), ErrorResponse.class).getMessage());
    }

    @Test
    public void createDepartment_authFailed_failed() throws Exception {
        CreateDepartmentRequest request = new CreateDepartmentRequest();
        request.setName("department");
        MvcResult result = mvc.perform(post(URI + "/create").contentType(MediaType.APPLICATION_JSON).content(mapToJson(request))).andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }

    @Test
    public void getDepartment_existingId_success() throws Exception {
        Department department = departmentDao.create(Department.builder().name("name").build());
        MvcResult result = mvc.perform(get(URI + "/" + department.getId()).contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(department.getId().intValue(), mapFromJson(result.getResponse().getContentAsString(), DepartmentExtendedResponse.class).getId());
        assertEquals("name", mapFromJson(result.getResponse().getContentAsString(), DepartmentExtendedResponse.class).getName());
    }

    @Test
    public void getDepartment_nonExistingId_failed() throws Exception {
        MvcResult result = mvc.perform(get(URI + "/" + Integer.MAX_VALUE).contentType(MediaType.APPLICATION_JSON)).andReturn();
        assertEquals(404, result.getResponse().getStatus());
        assertEquals(HttpStatus.NOT_FOUND, mapFromJson(result.getResponse().getContentAsString(), ErrorResponse.class).getStatus());
        assertEquals("Resource not found", mapFromJson(result.getResponse().getContentAsString(), ErrorResponse.class).getMessage());
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