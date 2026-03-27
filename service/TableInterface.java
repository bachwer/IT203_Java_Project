package service;


import model.Table;

import java.util.List;

public interface TableInterface {

    int create(Table table);

    void update(Table table);

    void delete(int tableId);

    List<Table> getAll();

    List<Table> getAvailable();

    void markAvailable(int tableId);

    void markOccupied(int tableId);

    void validate(Table table);
}