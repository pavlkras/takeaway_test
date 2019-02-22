package com.takeaway.test.employee.dao;

import com.takeaway.test.common.exceptions.PersistenceException;
import com.takeaway.test.employee.model.entities.Entity;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public interface Dao<E extends Entity, K> {
    E create(E entity) throws PersistenceException;
    E read(K key) throws PersistenceException;
    E update(E entity) throws PersistenceException;
    E delete(K key) throws PersistenceException;
}
