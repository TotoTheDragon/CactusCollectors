package com.developerdragon.cactuscollectors.listeners;

import com.developerdragon.cactuscollectors.CollectorMain;
import com.developerdragon.cactuscollectors.core.CInventoryHolder;
import com.developerdragon.cactuscollectors.core.CInventoryType;
import com.developerdragon.cactuscollectors.objects.CactusCollector;
import com.developerdragon.cactuscollectors.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InteractListener implements Listener {

    CollectorMain plugin = CollectorMain.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().equals(Material.BEACON)) return;

        CactusCollector collector = plugin.collectorHandler.getCollector(event.getClickedBlock().getLocation());

        if (collector == null) return;
        event.setCancelled(true);

        int size = 27;
        String title = ChatColor.translateAlternateColorCodes('&', "&6&lCactus Collector");

        Inventory menu = Bukkit.createInventory(new CInventoryHolder(size, title, CInventoryType.CACTUSCOLLECTOR, collector), size, title);
        ItemStack filler = new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5).setName("").toItemStack();
        for (int i = 0; i < menu.getSize(); i++) menu.setItem(i, filler);

        menu.setItem(13, new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY1ODViNDFjYTVhMWI0YWMyNmY1NTY3NjBlZDExMzA3Yzk0ZjhmOGExYWRlNjE1YmQxMmNlMDc0ZjQ3OTMifX19")
                .setName("&a&lStatistics")
                .addLore("",ChatColor.YELLOW + "Click to sell!", "", ChatColor.GRAY + "Total cactus collected: " + ChatColor.GREEN + (int) collector.getCactusStored(), ChatColor.GRAY + "Cactus in chunk: " + ChatColor.GREEN + collector.getCactusInChunk())
                .toItemStack());
        menu.setItem(22, new ItemBuilder(Material.BARRIER, 1)
                .setName("&c&lRemove cactus collector")
                .addLore("", ChatColor.GRAY + "Click this to remove the cactus collector")
                .toItemStack());

        event.getPlayer().openInventory(menu);

        new BukkitRunnable() {
            @Override public void run() {
                if (menu.getViewers().isEmpty()) this.cancel();
                menu.setItem(13, new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY1ODViNDFjYTVhMWI0YWMyNmY1NTY3NjBlZDExMzA3Yzk0ZjhmOGExYWRlNjE1YmQxMmNlMDc0ZjQ3OTMifX19")
                        .setName("&a&lStatistics")
                        .addLore("",ChatColor.YELLOW + "Click to sell!", "", ChatColor.GRAY + "Total cactus collected: " + ChatColor.GREEN + (int) collector.getCactusStored(), ChatColor.GRAY + "Cactus in chunk: " + ChatColor.GREEN + collector.getCactusInChunk())
                        .toItemStack());
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 5L);

    }
}
