package com.developerdragon.cactuscollectors.database;

import com.developerdragon.cactuscollectors.objects.CactusCollector;

import java.sql.Connection;
import java.util.List;

public interface IDataHandler {

    void load();

    void initialize();

    void stopConnection();

    Connection getConnection();

    void addCollector(CactusCollector collector);

    void removeCollector(CactusCollector collector);

    void updateCollector(CactusCollector collector);

    void bulkUpdate(List<CactusCollector> collectors);

    List<CactusCollector> getCollectors();

}
