package com.developerdragon.cactuscollectors.core;

import com.developerdragon.cactuscollectors.objects.CactusCollector;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CInventoryHolder implements InventoryHolder {

    @Getter private Inventory inventory;
    @Getter private String title;
    @Getter private int size;
    @Getter private CInventoryType type;
    @Getter private CactusCollector collector;
    @Getter private InventoryType inventoryType;


    public CInventoryHolder(int paramSize, String paramTitle, CInventoryType paramType, CactusCollector paramCollector) {
        this.size = paramSize;
        this.title = paramTitle;
        this.type = paramType;
        this.collector = paramCollector;
        this.inventory = Bukkit.createInventory(this, paramSize, paramTitle);
    }

    public CInventoryHolder(InventoryType inventoryType, String paramTitle, CInventoryType paramType, CactusCollector paramCollector) {
        this.inventoryType = inventoryType;
        this.title = paramTitle;
        this.type = paramType;
        this.collector = paramCollector;
        this.inventory = Bukkit.createInventory(this, inventoryType, paramTitle);
    }

}
