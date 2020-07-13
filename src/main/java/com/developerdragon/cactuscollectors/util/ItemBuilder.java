package com.developerdragon.cactuscollectors.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {


    private Inventory inventory;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private SkullMeta skullMeta;
    private PotionMeta potionMeta;
    private BlockStateMeta blockStateMeta;
    private CreatureSpawner creatureSpawner;


    private double position = -1;


    public ItemBuilder() {
        this(Material.AIR);
    }


    public ItemBuilder(Material material) {
        this(material, 1);
    }


    public ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }


    public ItemBuilder(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount, (byte) data);
        this.itemMeta = itemStack.getItemMeta();
    }


    public ItemBuilder(JSONObject jsonObject) {
        String string = jsonObject.get("serialized").toString();
        this.itemStack = fromString(string).toItemStack();
        this.itemMeta = itemStack.getItemMeta();
        this.position = fromString(string).getPosition();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setItem(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public ItemBuilder inventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(coloredString(name));
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder addLore(List<String> lores) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lores);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder addLore(String... lores) {
        addLore(Arrays.asList(lores));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int value, boolean ignoreLevelRestriction) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addEnchant(enchantment, value, ignoreLevelRestriction);
        this.itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder setEnchants(Map<Enchantment, Integer> enchantment) {
        for (Map.Entry<Enchantment, Integer> entry : enchantment.entrySet()) {
            Enchantment enchant = entry.getKey();
            addEnchantment(enchant, entry.getValue(), entry.getValue() > enchant.getMaxLevel());
        }
        return this;
    }

    public ItemBuilder removeEnchant(Enchantment enchantment) {
        if (!this.getEnchantments().containsKey(enchantment))
            return this;
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeEnchant(enchantment);
        this.itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder removeEnchants(List<Enchantment> enchantments) {
        for (Enchantment enchantment : enchantments) {
            if (!this.getEnchantments().containsKey(enchantment))
                continue;
            this.removeEnchant(enchantment);
        }
        return this;
    }

    public ItemBuilder clearEnchants() {
        if (this.getEnchantments() == null)
            return this;
        for (Enchantment enchantment : this.getEnchantments().keySet())
            this.removeEnchant(enchantment);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag... itemFlag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder addItemFlag(List<ItemFlag> itemFlag) {
        itemFlag.forEach(this::addItemFlag);
        return this;
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlag);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder hideEnchant() {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder showEnchant() {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(itemMeta);
        this.itemMeta = itemMeta;
        return this;
    }

    public ItemBuilder setNewDurability(int durability) {
        itemStack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setSkullTextureFromePlayerName(String playerName) {
        this.skullMeta = (SkullMeta) itemStack.getItemMeta();
        this.skullMeta.setOwner(playerName);
        itemStack.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder setSkullTexture(Player player) {
        setSkullTextureFromePlayerName(player.getName());
        return this;
    }

    public ItemBuilder setSkullTexture(String value) {
        this.skullMeta = (SkullMeta) itemStack.getItemMeta();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));

        try {
            Field gameProfileField = skullMeta.getClass().getDeclaredField("profile");
            gameProfileField.setAccessible(true);
            gameProfileField.set(skullMeta, gameProfile);
        } catch (IllegalAccessException | NoSuchFieldException error) {
            error.printStackTrace();
        }

        itemStack.setItemMeta(skullMeta);
        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType) {
        addPotionEffect(potionEffectType, 10);
        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration) {
        addPotionEffect(potionEffectType, duration, 1);
        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier) {
        addPotionEffect(potionEffectType, duration, amplifier, true);
        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient) {
        addPotionEffect(potionEffectType, duration, amplifier, ambient, false);
        return this;
    }

    public ItemBuilder addPotionEffect(PotionEffectType potionEffectType, int duration, int amplifier, boolean ambient, boolean overwrite) {
        this.potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        this.potionMeta.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier, ambient), overwrite);
        this.itemStack.setItemMeta(this.potionMeta);
        return this;
    }

    public ItemBuilder removePotionEffect(PotionEffectType potionEffectType) {
        this.potionMeta = (PotionMeta) this.itemStack.getItemMeta();
        if (this.potionMeta == null || !this.potionMeta.hasCustomEffect(potionEffectType))
            return this;
        this.potionMeta.removeCustomEffect(potionEffectType);
        this.itemStack.setItemMeta(potionMeta);
        return this;
    }

    public ItemBuilder removePotionEffect(List<PotionEffectType> potionEffectTypes) {
        for (PotionEffectType potionEffectType : potionEffectTypes) {
            if (this.potionMeta == null || !this.potionMeta.hasCustomEffect(potionEffectType))
                continue;
            removePotionEffect(potionEffectType);
        }
        return this;
    }

    public ItemBuilder clearPotionEffect() {
        if (this.getPotionEffects() == null)
            return this;
        for (PotionEffect potionEffect : this.getPotionEffects()) {
            removePotionEffect(potionEffect.getType());
        }
        return this;
    }

    public ItemBuilder setPotion(PotionType potionType) {
        setPotion(potionType, true);
        return this;
    }

    public ItemBuilder setPotion(PotionType potionType, boolean splash) {
        Potion potion = new Potion(1);
        potion.setSplash(splash);
        potion.setType(potionType);
        potion.apply(this.itemStack);
        return this;
    }

    public ItemBuilder setType(EntityType type) {
        blockStateMeta = (BlockStateMeta) this.getItemMeta();
        creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
        creatureSpawner.setSpawnedType(type);
        blockStateMeta.setBlockState(creatureSpawner);
        this.itemStack.setItemMeta(blockStateMeta);
        return this;
    }

    public ItemBuilder inject(Inventory inventory, int position) {
        inventory.setItem(position, toItemStack());
        return this;
    }

    public ItemBuilder inject(Inventory inventory) {
        inventory.addItem(toItemStack());
        return this;
    }

    public ItemBuilder inject(int position) {
        inventory.setItem(position, toItemStack());
        return this;
    }

    public ItemBuilder inject() {
        this.inventory.addItem(toItemStack());
        return this;
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public long getPosition() {
        return (long) this.position;
    }

    public ItemBuilder setPosition(int position) {
        this.position = position;
        return this;
    }

    public ItemStack toItemStack() {
        return itemStack;
    }

    public boolean isSimilar(ItemBuilder itemBuilder) {
        return hasSameMaterial(itemBuilder) && hasSameData(itemBuilder) && hasSameDisplayName(itemBuilder);
    }

    public boolean isExactlySame(ItemBuilder itemBuilder) {
        return hasSameMaterial(itemBuilder) && hasSameData(itemBuilder) && hasSameDisplayName(itemBuilder)
                && hasSameAmount(itemBuilder) && hasSameDurability(itemBuilder) && hasSameEnchantment(itemBuilder)
                && hasSameItemFlag(itemBuilder) && hasSameLore(itemBuilder) && hasSameBreakableStat(itemBuilder);
    }

    public boolean hasSameMaterial(ItemBuilder itemBuilder) {
        return getMaterial() == itemBuilder.getMaterial();
    }

    public boolean hasSameDisplayName(ItemBuilder itemBuilder) {
        return getDisplayName().equalsIgnoreCase(itemBuilder.getDisplayName());
    }

    public boolean hasSameEnchantment(ItemBuilder itemBuilder) {
        return getEnchantments().equals(itemBuilder.getEnchantments());
    }

    public boolean hasSameItemFlag(ItemBuilder itemBuilder) {
        return getItemFlag().equals(itemBuilder.getItemFlag());
    }

    public boolean hasSameLore(ItemBuilder itemBuilder) {
        if (getLore() == null)
            return false;
        return getLore().equals(itemBuilder.getLore());
    }

    public boolean hasSameData(ItemBuilder itemBuilder) {
        return getData() == itemBuilder.getData();
    }

    public boolean hasSameAmount(ItemBuilder itemBuilder) {
        return getAmount() == itemBuilder.getAmount();
    }

    public boolean hasSameDurability(ItemBuilder itemBuilder) {
        return getDurability() == itemBuilder.getDurability();
    }

    public boolean hasSameBreakableStat(ItemBuilder itemBuilder) {
        return isUnbreakable() == itemBuilder.isUnbreakable();
    }

    public Material getMaterial() {
        return itemStack.getType();
    }

    public int getAmount() {
        return itemStack.getAmount();
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public int getData() {
        return itemStack.getData().getData();
    }

    public ItemBuilder setData(int data) {
        this.itemStack = new ItemStack(itemStack.getType(), itemStack.getAmount(), (byte) data);
        return this;
    }

    public int getDurability() {
        return itemStack.getDurability();
    }

    public ItemBuilder setDurability(float percent) {
        if (percent > 100.0) {
            return this;
        } else if (percent < 0.0) {
            return this;
        }
        itemStack.setDurability((short) (itemStack.getDurability() * (percent / 100)));
        return this;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public String getDisplayName() {
        return itemStack.hasItemMeta() && itemMeta.hasDisplayName() ? itemMeta.getDisplayName() : "";
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.itemStack.hasItemMeta() && this.itemMeta.hasEnchants() ? this.itemMeta.getEnchants() : null;
    }

    public List<String> getLore() {
        return itemStack.hasItemMeta() && itemMeta.hasLore() ? itemMeta.getLore() : null;
    }

    public Set<ItemFlag> getItemFlag() {
        return itemStack.hasItemMeta() && itemMeta.getItemFlags().size() > 0 ? itemMeta.getItemFlags() : null;
    }

    public List<PotionEffect> getPotionEffects() {
        return this.potionMeta != null && this.potionMeta.getCustomEffects().size() > 0 ? this.potionMeta.getCustomEffects() : null;
    }

    public boolean isUnbreakable() {
        return itemStack.hasItemMeta() && itemMeta.spigot().isUnbreakable();
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        if (this.itemMeta == null) {
            if (this.itemStack == null)
                return null;
            this.itemMeta = this.itemStack.getItemMeta();
        }
        this.itemMeta.spigot().setUnbreakable(unbreakable);
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    @SuppressWarnings("unchecked")
    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serialized", toString());
        return jsonObject;
    }

    public String toString() {
        String[] splitValues = new String[]{"{^}", "[^]", "`SECTION_TYPE`", "|", ","};
        StringBuilder itemBuilderString = new StringBuilder();
        itemBuilderString.append("item: ").append(splitValues[2]).append(splitValues[1])
                .append("type: ").append(getMaterial()).append(splitValues[1])
                .append("data: ").append(getData()).append(splitValues[1])
                .append("amount: ").append(getAmount()).append(splitValues[1])
                .append("durability: ").append(getDurability()).append(splitValues[1])
                .append("unbreakable: ").append(isUnbreakable()).append(splitValues[1])
                .append(splitValues[0]);
        itemBuilderString.append("other: ").append(splitValues[2]).append(splitValues[1]);
        itemBuilderString.append("position: ").append(getPosition()).append(splitValues[1]);
        itemBuilderString.append(splitValues[0]);
        if (this.itemStack.hasItemMeta()) {
            itemBuilderString.append("meta: ").append(splitValues[2]).append(splitValues[1]);
            if (getDisplayName() != null)
                itemBuilderString.append("name: ").append(getDisplayName()).append(splitValues[1]);
            if (getEnchantments() != null) {
                itemBuilderString.append("enchants: ");
                getEnchantments().forEach((enchantment, integer) -> itemBuilderString.append("enchantType: ")
                        .append(enchantment.getName()).append(splitValues[4])
                        .append("enchantLevel: ").append(integer)
                        .append(splitValues[4]).append(splitValues[3]));
                itemBuilderString.append(splitValues[1]);
            }
            if (getLore() != null) {
                itemBuilderString.append("lores: ");
                getLore().forEach(s -> itemBuilderString.append("lore: ").append(uncoloredString(s)).append(splitValues[3]));
                itemBuilderString.append(splitValues[1]);
            }
            if (getItemFlag() != null) {
                itemBuilderString.append("itemFlags: ");
                getItemFlag().forEach(s -> itemBuilderString.append("itemflag: ").append(s).append(splitValues[3]));
                itemBuilderString.append(splitValues[1]);
            }
            itemBuilderString.append(splitValues[0]);
        }

        return itemBuilderString.toString();
    }

    public ItemBuilder fromString(String string) {
        String[] splitValues = new String[]{"\\{\\^}", "\\[\\^]", "`SECTION_TYPE`", "\\|", ","};
        ItemBuilder itemBuilder = new ItemBuilder();
        String[] sections = string.split(splitValues[0]);
        if (sections.length == 0 || Arrays.stream(sections).filter(s -> s.split(splitValues[2])[0]
                .equalsIgnoreCase("item: ")).count() != 1)
            return itemBuilder;
        String[] sectionType = sections[0].split(splitValues[2]);
        String[] object = sections[0].split(splitValues[1]);
        if (object.length < 6)
            return itemBuilder;
        itemSection(itemBuilder, sectionType, object);
        if (sections.length == 1 || !sections[1].startsWith("other: "))
            return itemBuilder;
        sectionType = sections[1].split(splitValues[2]);
        object = sections[1].split(splitValues[1]);
        otherPropertySection(itemBuilder, sectionType, object);
        if (sections.length == 2)
            return itemBuilder;
        sectionType = sections[2].split(splitValues[2]);
        object = sections[2].split(splitValues[1]);
        if (sectionType[0].equalsIgnoreCase("meta: "))
            metaSection(itemBuilder, sectionType, object);
        return itemBuilder;
    }

    private void itemSection(ItemBuilder itemBuilder, String[] sectionType, String[] object) {
        Arrays.asList(object).forEach(s -> {
            if (!s.equalsIgnoreCase(sectionType[0])) {
                if (s.startsWith("type: "))
                    itemBuilder.setItem(Material.valueOf(s.replace("type: ", "")));
                if (s.startsWith("data: "))
                    itemBuilder.setData(Integer.parseInt(s.replace("data: ", "")));
                if (s.startsWith("amount: "))
                    itemBuilder.setAmount(Integer.parseInt(s.replace("amount: ", "")));
                if (s.startsWith("durability: "))
                    itemBuilder.setNewDurability(Integer.parseInt(s.replace("durability: ", "")));
                if (s.startsWith("unbreakable: "))
                    itemBuilder.setUnbreakable(Boolean.parseBoolean(s.replace("unbreakable: ", "")));
            }
        });
    }

    private void otherPropertySection(ItemBuilder itemBuilder, String[] sectionType, String[] object) {
        Arrays.asList(object).forEach(s -> {
            if (!s.equalsIgnoreCase(sectionType[0])) {
                if (s.startsWith("position: ")) {
                    itemBuilder.setPosition(Integer.parseInt(s.replace("position: ", "")));
                }
            }
        });
    }

    private void metaSection(ItemBuilder itemBuilder, String[] sectionType, String[] object) {
        String[] splitValues = new String[]{"\\{\\^}", "\\[\\^]", "`SECTION_TYPE`", "\\|", ","};
        Arrays.asList(object).forEach(s -> {
            if (!s.equalsIgnoreCase(sectionType[0])) {
                if (s.startsWith("name: "))
                    itemBuilder.setName(coloredString(s.replace("name: ", "")));
                if (s.startsWith("enchants: ")) {
                    Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
                    String[] enchant = s.split(splitValues[3]);
                    Arrays.asList(enchant).forEach(s1 -> {
                        String[] enchantObject = s1.split(splitValues[4]);
                        enchantmentMap.put(Enchantment.getByName(enchantObject[0].replace("enchants: ", "")
                                        .replace("enchantType: ", "")),
                                Integer.valueOf(enchantObject[1].replace("enchantLevel: ", "")));
                    });
                    itemBuilder.setEnchants(enchantmentMap);
                }
                if (s.startsWith("lores: ")) {
                    String[] lores = s.split(splitValues[3]);
                    List<String> loreList = new ArrayList<>();
                    Arrays.asList(lores).forEach(s1 -> loreList.add(coloredString(s1)
                            .replace("lores: ", "").replace("lore: ", "")));
                    itemBuilder.addLore(loreList);
                }
                if (s.startsWith("itemFlags: ")) {
                    String[] itemFlags = s.split(splitValues[3]);
                    List<ItemFlag> itemFlagList = new ArrayList<>();
                    Arrays.asList(itemFlags).forEach(s1 -> itemFlagList.add(ItemFlag.valueOf(s1.replace("itemFlags: ", "")
                            .replace("itemflag: ", ""))));
                    itemBuilder.addItemFlag(itemFlagList);
                }
            }
        });
    }

    private String uncoloredString(String string) {
        return string.replace("§", "&");
    }

    private String coloredString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
