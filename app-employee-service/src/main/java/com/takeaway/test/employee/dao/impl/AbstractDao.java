package com.takeaway.test.employee.dao.impl;

import com.takeaway.test.common.exceptions.ConstraintException;
import com.takeaway.test.common.exceptions.DuplicateResourceException;
import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.common.exceptions.ResourceNotFoundException;
import com.takeaway.test.employee.dao.Dao;
import com.takeaway.test.employee.model.entities.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public abstract class AbstractDao<E extends Entity, K> implements Dao<E, K> {
    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;

    private final Class<E> type;

    AbstractDao() {
        final String className = getClass().getName();

        // Fetch AbstractDao as a parametrized class type.
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        if (parameterizedType == null) {
            throw new IllegalStateException("Could not detect dao type for DAO implementation " + className);
        }

        // Extract type arguments.
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length != 2) {
            throw new IllegalStateException("Could not detect dao type for DAO implementation " + className);
        }

        //noinspection unchecked
        this.type = (Class<E>) types[0];
    }

    E processQueryReturningEntity(String query, SqlParameterSource params, RowMapper<E> rowMapper) throws PersistenceException {
        return (E)processQuery(query, params, rowMapper);    }

    K processQueryReturningKey(String query, SqlParameterSource params, RowMapper<K> rowMapper) throws PersistenceException {
        return (K)processQuery(query, params, rowMapper);
    }

    private Object processQuery(String query, SqlParameterSource params, RowMapper rowMapper) throws PersistenceException {
        Object record;
        try {
            record = jdbcTemplate.queryForObject(query, params, rowMapper);
        } catch (EmptyResultDataAccessException erdae) {
            throw new ResourceNotFoundException(erdae);
        } catch (DuplicateKeyException dke) {
            throw new DuplicateResourceException(dke);
        } catch (DataIntegrityViolationException dive) {
            throw new ConstraintException(dive);
        } catch (DataAccessException dae) {
            throw new PersistenceException(dae);
        }
        return record;
    }

}
