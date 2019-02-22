package com.takeaway.test.common.exceptions;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public class ConstraintException extends PersistenceException {
    public ConstraintException() {
    }

    public ConstraintException(Throwable cause) {
        super(cause);
    }

}
