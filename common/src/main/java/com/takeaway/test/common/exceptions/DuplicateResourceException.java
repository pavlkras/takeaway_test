package com.takeaway.test.common.exceptions;

/**
 * takeaway_test
 * <p>
 * ???
 *
 * @author Pavel
 * @since 22/02/2019
 */

public class DuplicateResourceException extends PersistenceException {
    public DuplicateResourceException() {
    }

    public DuplicateResourceException(Throwable cause) {
        super(cause);
    }
}
