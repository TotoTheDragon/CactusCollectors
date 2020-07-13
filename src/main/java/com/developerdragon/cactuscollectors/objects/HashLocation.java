package com.developerdragon.cactuscollectors.objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;

public class HashLocation {

    @Getter private int x;
    @Getter private int y;
    @Getter private int z;
    @Getter private String worldName;

    public HashLocation(int paramX, int paramY, int paramZ, String paramWorldName) {
        this.x = paramX;
        this.y = paramY;
        this.z = paramZ;
        this.worldName = paramWorldName;
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(worldName), x, y, z);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashLocation hashChunk = (HashLocation) o;
        return x == hashChunk.x &&
                z == hashChunk.z &&
                Objects.equals(worldName, hashChunk.worldName);
    }

    @Override public int hashCode() {
        return Objects.hash(x, z, worldName);
    }

    @Override public String toString() {
        return "HashLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", worldName='" + worldName + '\'' +
                '}';
    }
}
