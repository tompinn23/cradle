package org.int13h.cradle.services;

public class ServiceException extends RuntimeException {
    public ServiceException(Class clazz, Throwable cause) {
        super(String.format("Failed to locate service implementation of %s", clazz.getSimpleName()), cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Class clazz) {
        super(String.format("Failed to locate service implementation of %s", clazz.getSimpleName()));
    }

    public ServiceException(String message) {
        super(message);
    }
}