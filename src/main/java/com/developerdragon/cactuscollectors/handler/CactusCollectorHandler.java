package com.developerdragon.cactuscollectors.handler;

import com.developerdragon.cactuscollectors.CollectorMain;
import com.developerdragon.cactuscollectors.objects.CactusCollector;
import com.developerdragon.cactuscollectors.objects.HashChunk;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class CactusCollectorHandler {

    CollectorMain plugin;
    HashMap<HashChunk, CactusCollector> collectorHashMap;
    BukkitTask recalculateTask;
    BukkitTask giveCactusTask;
    BukkitTask updateTask;


    public CactusCollectorHandler() {
        plugin = CollectorMain.getInstance();
        collectorHashMap = new HashMap<>();
        loadCollectors();

        recalculateTask = new BukkitRunnable() {
            @Override
            public void run() {
                recalculateAllChunks();
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 1200L);

        giveCactusTask = new BukkitRunnable() {
            @Override
            public void run() {
                collectorHashMap.forEach((hashChunk, collector) -> collector.setCactusStored((collector.getCactusStored() + (collector.getCactusInChunk() / 720.0))));
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 5L);

        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                plugin.databaseHandler.bulkUpdate(new ArrayList<>(collectorHashMap.values()));
            }
        }.runTaskTimerAsynchronously(plugin, 0, 1200L);

    }

    public void addCollector(CactusCollector collector) {
        collectorHashMap.putIfAbsent(collector.getHashChunk(), collector);
        plugin.databaseHandler.addCollector(collector);
    }

    public void removeCollector(CactusCollector collector) {
        collectorHashMap.remove(collector.getHashChunk());
        plugin.databaseHandler.removeCollector(collector);
    }


    public CactusCollector getCollector(Location location) {
        return getCollector(location.getChunk());
    }

    public CactusCollector getCollector(Chunk chunk) {
        return collectorHashMap.getOrDefault(new HashChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getName()), null);
    }


    public void stop() {
        recalculateTask.cancel();
    }

    public void loadCollectors() {
        plugin.databaseHandler.getCollectors().forEach(collector -> collectorHashMap.put(collector.getHashChunk(), collector));
    }

    public void recalculateCactusInChunk(CactusCollector collector) {
        Chunk chunk = collector.getHashChunk().getChunk();
        int count = 0;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    if (chunk.getBlock(x, y, z).getType() != Material.CACTUS) continue;   // Check if block is cactus
                    if (chunk.getBlock(x, y + 1, z).getType() != Material.AIR)
                        continue; // Check if block above cactus is free

                    if (chunk.getBlock(x + 1, y + 1, z).getType() == Material.AIR &&
                            chunk.getBlock(x - 1, y + 1, z).getType() == Material.AIR &&
                            chunk.getBlock(x, y + 1, z + 1).getType() == Material.AIR &&
                            chunk.getBlock(x, y + 1, z - 1).getType() == Material.AIR)
                        continue; // Check blocks one up, then to the side to see if it would break normally

                    count++;
                }
            }
        }
        if (collector.getCactusInChunk() == count) return;
        collector.setCactusInChunk(count);
    }

    public void recalculateAllChunks() {
        collectorHashMap.forEach((hashChunk, collector) -> Bukkit.getScheduler().runTask(plugin, () -> recalculateCactusInChunk(collector)));
    }

}
