package com.takeaway.test.employee.dao;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Department;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Repository
public class DepartmentDao extends AbstractDao<Department, Integer> {
    private static final String TABLE_NAME = "departments";
    private static final String DEPARTMENT_ALL_FIELDS =
            " id, " +
            " name ";
    private static final String DEPARTMENT_SELECT = "SELECT " + DEPARTMENT_ALL_FIELDS +
            " FROM " + TABLE_NAME + " ";
    private static final String DEPARTMENT_INSERT = "INSERT INTO " + TABLE_NAME + " (name) " +
            " VALUES(:name) RETURNING *";

    public static final RowMapper<Department> ROW_MAPPER = (rs, rowNum) -> Department.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .build();

    @Transactional(readOnly = false)
    @Override
    public Department create(Department entity) throws PersistenceException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", entity.getName());
        return processQuery(DEPARTMENT_INSERT, params, ROW_MAPPER);

    }

    @Transactional(readOnly = true)
    @Override
    public Department read(Integer key) throws PersistenceException {
        final String query = DEPARTMENT_SELECT + "WHERE id = :id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", key);
        return processQuery(query, params, ROW_MAPPER);
    }

    @Override
    public Department update(Department entity) throws PersistenceException {
        throw new UnsupportedOperationException("DepartmentDao doesn't support update");
    }

    @Override
    public Department delete(Integer key) throws PersistenceException {
        throw new UnsupportedOperationException("DepartmentDao doesn't support delete");
    }
}
