package grn.database.repository;

import grn.exception.EndpointException;

public interface Repository {
    void init () throws EndpointException;
    void reload() throws EndpointException;
}
