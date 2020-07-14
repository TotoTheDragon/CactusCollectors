package com.developerdragon.cactuscollectors.listeners;

import com.developerdragon.cactuscollectors.CollectorMain;
import com.developerdragon.cactuscollectors.commands.GiveCollectorCommand;
import com.developerdragon.cactuscollectors.core.CInventoryHolder;
import com.developerdragon.cactuscollectors.core.CInventoryType;
import com.developerdragon.cactuscollectors.util.ChatUtil;
import com.developerdragon.cactuscollectors.util.ItemBuilder;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.exception.player.PlayerDataNotLoadedException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    CollectorMain plugin = CollectorMain.getInstance();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof CInventoryHolder)) return;

        CInventoryHolder holder = (CInventoryHolder) event.getInventory().getHolder();
        Player player = (Player) event.getWhoClicked();

        if (holder.getType().equals(CInventoryType.CACTUSCOLLECTOR)) {
            event.setCancelled(true);

            // TODO Switch to different windowbuilder that gets from config

            if (event.getSlot() == 22) {
                InventoryType inventoryType = InventoryType.HOPPER;
                String title = ChatColor.translateAlternateColorCodes('&', "&c&lRemove cactus collector");
                Inventory removeMenu = Bukkit.createInventory(new CInventoryHolder(inventoryType, title, CInventoryType.CACTUSCOLLECTORREMOVE, holder.getCollector()), inventoryType, title);
                removeMenu.setItem(1, new ItemBuilder(Material.STAINED_CLAY, 1, 5).setName("&a&lConfirm").toItemStack());
                removeMenu.setItem(3, new ItemBuilder(Material.STAINED_CLAY, 1, 14).setName("&c&lCancel").toItemStack());

                player.closeInventory();
                player.openInventory(removeMenu);
            } else if (event.getSlot() == 13) {
                try {
                    double sellAmount = ShopGuiPlusApi.getItemStackPriceSell(player, new ItemStack(Material.CACTUS, (int) holder.getCollector().getCactusStored()));
                    ChatUtil.sendPluginMessage(player, "Sold &e" + (int) holder.getCollector().getCactusStored() + " &fcactus for a total of: &e" + sellAmount + "&f.");
                    holder.getCollector().setCactusStored(holder.getCollector().getCactusStored() - (int) holder.getCollector().getCactusStored());
                    plugin.getEcon().depositPlayer(Bukkit.getPlayer(player.getUniqueId()), sellAmount);
                } catch (PlayerDataNotLoadedException exception) {
                    exception.printStackTrace();
                }
            }

            return;
        } else if (holder.getType().equals(CInventoryType.CACTUSCOLLECTORREMOVE)) {
            event.setCancelled(true);

            if (event.getSlot() == 3) player.closeInventory();
            else if (event.getSlot() == 1) {
                holder.getCollector().getHashLocation().getLocation().getBlock().setType(Material.AIR);
                plugin.collectorHandler.removeCollector(holder.getCollector());
                GiveCollectorCommand.giveCollector(player, 1);
                player.closeInventory();
            }
            return;
        }
    }

}
