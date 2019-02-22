package com.takeaway.test.employee.dao.impl;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.dao.EmployeeDao;
import com.takeaway.test.employee.model.entities.Department;
import com.takeaway.test.employee.model.entities.Employee;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

@Repository
public class EmployeeDaoImpl extends AbstractDao<Employee, String> implements EmployeeDao {
    private static final String TABLE_NAME = "employees";
    private static final String EMPLOYEE_INSERT = "INSERT INTO " + TABLE_NAME + " (" +
            " email, " +
            " first_name, " +
            " last_name, " +
            " birthday, " +
            " department_id " +
            ") VALUES(:email, :firstName, :lastName, :birthday, :departmentId) RETURNING *";
    private static final String EMPLOYEE_UPDATE = "UPDATE " + TABLE_NAME + " SET ";
    private static final String EMPLOYEE_DELETE = "DELETE FROM " + TABLE_NAME;

    private static final RowMapper<Employee> ROW_MAPPER = (rs, rowNum) -> Employee.builder()
            .uuid(rs.getString("_uuid"))
            .email(rs.getString("email"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .birthday(rs.getDate("birthday").toLocalDate())
            .departmentId(rs.getInt("department_id"))
            .build();

    private static final RowMapper<Employee> ROW_MAPPER_EXTENDED = (rs, rowNum) -> Employee.builder()
            .uuid(rs.getString("_uuid"))
            .email(rs.getString("email"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .birthday(rs.getDate("birthday").toLocalDate())
            .department(Department.builder()
                    .id(rs.getInt("department_id"))
                    .name(rs.getString("department_name"))
                    .build())
            .build();

    @Transactional(readOnly = false)
    @Override
    public Employee create(Employee entity) throws PersistenceException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", entity.getEmail())
                .addValue("firstName", entity.getFirstName())
                .addValue("lastName", entity.getLastName())
                .addValue("birthday", entity.getBirthday())
                .addValue("departmentId", entity.getDepartmentId());
        return processQueryReturningEntity(EMPLOYEE_INSERT, params, ROW_MAPPER);
    }

    @Transactional(readOnly = true)
    @Override
    public Employee read(String key, boolean extended) throws PersistenceException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", key);
        Employee employee;
        if (!extended) {
            final String query = "SELECT _uuid, email, first_name, last_name, birthday, department_id " +
                    " FROM " + TABLE_NAME +
                    " WHERE _uuid::text = :uuid";
            employee = processQueryReturningEntity(query, params, ROW_MAPPER);
        } else {
            final String query = "SELECT e._uuid, e.email, e.first_name, e.last_name, e.birthday, " +
                    " d.id as department_id, d.name as department_name " +
                    " FROM employees e JOIN departments d on e.department_id = d.id " +
                    " WHERE e._uuid::text = :uuid";
            employee = processQueryReturningEntity(query, params, ROW_MAPPER_EXTENDED);
        }
        return employee;
    }

    @Transactional(readOnly = false)
    @Override
    public Employee update(Employee entity) throws PersistenceException {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", entity.getUuid());
        final StringBuilder queryBuilder = new StringBuilder("UPDATE " + TABLE_NAME + " SET _uuid = _uuid ");

        if (entity.getEmail() != null) {
            queryBuilder.append(", email = :email ");
            params.addValue("email", entity.getEmail());
        }
        if (entity.getFirstName() != null) {
            queryBuilder.append(", first_name = :firstName ");
            params.addValue("firstName", entity.getFirstName());
        }
        if (entity.getLastName() != null) {
            queryBuilder.append(", last_name = :lastName ");
            params.addValue("lastName", entity.getLastName());
        }
        if (entity.getBirthday() != null) {
            queryBuilder.append(", birthday = :birthday ");
            params.addValue("birthday", entity.getBirthday());
        }
        if (entity.getDepartmentId() != null) {
            queryBuilder.append(", department_id = :departmentId ");
            params.addValue("departmentId", entity.getDepartmentId());
        }
        queryBuilder.append(" WHERE _uuid::text = :uuid");
        queryBuilder.append(" RETURNING *");

        final String query = queryBuilder.toString();

        return processQueryReturningEntity(query, params, ROW_MAPPER);
    }

    @Transactional(readOnly = false)
    @Override
    public String delete(String key) throws PersistenceException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("uuid", key);
        final String query = EMPLOYEE_DELETE + " WHERE _uuid::text = :uuid RETURNING _uuid";

        return processQueryReturningKey(query, params, (rs, rowNum) -> rs.getString("_uuid"));
    }
}
