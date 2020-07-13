package com.developerdragon.cactuscollectors.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class CactusCollector {

    @Getter @Setter private double cactusStored;
    @Getter @Setter private int cactusInChunk;
    @Getter private HashChunk hashChunk;
    @Getter private HashLocation hashLocation;

    public CactusCollector(int paramX, int paramY, int paramZ, String paramWorldName, double paramCactusStored, int paramCactusInChunk) {
        this.hashLocation = new HashLocation(paramX, paramY, paramZ, paramWorldName);
        this.hashChunk = new HashChunk(paramX >> 4, paramZ >> 4, paramWorldName);
        this.cactusStored = paramCactusStored;
        this.cactusInChunk = paramCactusInChunk;
    }

    public CactusCollector(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName(), 0, 0);
    }

    @Override public String toString() {
        return "CactusCollector{" +
                "cactusStored=" + cactusStored +
                ", cactusInChunk=" + cactusInChunk +
                ", hashLocation=" + hashLocation +
                '}';
    }
}
