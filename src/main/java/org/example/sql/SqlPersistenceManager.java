package org.example.sql;

import java.util.List;

public class SqlPersistenceManager  {

    public void insert(Persistable persistable) {
        SqlService.insert(persistable);
    }


    public void update(Persistable persistable)  {
        SqlService.update(persistable);
    }

    public void delete(Persistable persistable) {
        SqlService.delete(persistable);
    }

}