package com.developerdragon.cactuscollectors.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class CactusCollector {

    @Getter
    @Setter
    private double cactusStored;
    @Getter
    @Setter
    private int cactusInChunk;
    @Getter
    private final HashChunk hashChunk;
    @Getter
    private final HashLocation hashLocation;
    @Getter
    private final String ownerUniqueId;

    public CactusCollector(int paramX, int paramY, int paramZ, String paramWorldName, double paramCactusStored, int paramCactusInChunk, String paramOwner) {
        this.hashLocation = new HashLocation(paramX, paramY, paramZ, paramWorldName);
        this.hashChunk = new HashChunk(paramX >> 4, paramZ >> 4, paramWorldName);
        this.cactusStored = paramCactusStored;
        this.cactusInChunk = paramCactusInChunk;
        this.ownerUniqueId = paramOwner;
    }

    public CactusCollector(Location location, String paramOwner) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName(), 0, 0,paramOwner);
    }

    @Override
    public String toString() {
        return "CactusCollector{" +
                "cactusStored=" + cactusStored +
                ", cactusInChunk=" + cactusInChunk +
                ", hashLocation=" + hashLocation +
                '}';
    }
}
