package grn.database.repository;

import grn.exception.OutdatedApiKeyException;

public interface Repository {
    void init () throws OutdatedApiKeyException;
    void reload() throws OutdatedApiKeyException;
}
