package com.developerdragon.cactuscollectors.objects;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.Objects;

public class HashChunk {

    @Getter private int x;
    @Getter private int z;
    @Getter private String worldName;

    public HashChunk(int paramX, int paramZ, String paramWorldName) {
        this.x = paramX;
        this.z = paramZ;
        this.worldName = paramWorldName;
    }

    public Chunk getChunk() {
        return Bukkit.getWorld(worldName).getChunkAt(x, z);
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashChunk hashChunk = (HashChunk) o;
        return x == hashChunk.x &&
                z == hashChunk.z &&
                Objects.equals(worldName, hashChunk.worldName);
    }

    @Override public int hashCode() {
        return Objects.hash(x, z, worldName);
    }
}
