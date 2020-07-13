package com.developerdragon.cactuscollectors.listeners;

import com.developerdragon.cactuscollectors.CollectorMain;
import com.developerdragon.cactuscollectors.objects.CactusCollector;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class BlockGrowListener implements Listener {

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (!event.getNewState().getType().equals(Material.CACTUS)) return;
        CactusCollector collector = CollectorMain.getInstance().collectorHandler.getCollector(event.getNewState().getChunk());
        if (collector == null) return;
        event.setCancelled(true);
    }
}
