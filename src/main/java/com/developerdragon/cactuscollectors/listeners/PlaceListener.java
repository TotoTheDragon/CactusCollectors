package com.developerdragon.cactuscollectors.listeners;

import com.developerdragon.cactuscollectors.CollectorMain;
import com.developerdragon.cactuscollectors.objects.CactusCollector;
import com.developerdragon.cactuscollectors.util.ChatUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceListener implements Listener {

    CollectorMain plugin = CollectorMain.getInstance();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().getItemInHand().getType().equals(Material.BEACON)) return;
        if (event.getPlayer().getItemInHand() == null) return;
        if (!event.getPlayer().getItemInHand().getItemMeta().spigot().isUnbreakable()) return;

        if (plugin.collectorHandler.getCollector(event.getBlockPlaced().getLocation()) != null) {
            ChatUtil.sendPluginMessage(event.getPlayer(), "&cYou can only place 1 collector per chunk");
            event.setCancelled(true);
            return;
        }

        CactusCollector collector = new CactusCollector(event.getBlockPlaced().getLocation());
        plugin.collectorHandler.addCollector(collector);
        plugin.collectorHandler.recalculateCactusInChunk(collector);
    }

}
