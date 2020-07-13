package com.developerdragon.cactuscollectors.database;

import com.developerdragon.cactuscollectors.objects.CactusCollector;

import java.sql.Connection;
import java.util.List;

public interface IDataHandler {

    void load();

    void initialize();

    void stopConnection();

    Connection getConnection();

    boolean addCollector(CactusCollector collector);

    boolean removeCollector(CactusCollector collector);

    boolean updateCollector(CactusCollector collector);

    boolean bulkUpdate(List<CactusCollector> collectors);

    List<CactusCollector> getCollectors();

}
