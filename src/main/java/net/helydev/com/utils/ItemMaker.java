package net.helydev.com.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemMaker implements Cloneable
{
    private Material type;
    private int data;
    private int amount;
    private String title;
    private List<String> lore;
    private Color color;
    private HashMap<Enchantment, Integer> enchantments;
    private boolean unbreakable;
    
    public ItemMaker(final Material type) {
        this(type, 1);
    }
    
    public ItemMaker(final Material type, final int amount) {
        this(type, amount, 0);
    }
    
    public ItemMaker(Material type, int amount, int data) {
        this.lore = new ArrayList<>();
        this.type = type;
        this.amount = amount;
        this.data = data;
        this.enchantments = new HashMap<>();
    }
    
    public ItemMaker(final ItemStack item) {
        this.lore = new ArrayList();
        Validate.notNull(item);
        this.enchantments = new HashMap<>();
        this.type = item.getType();
        this.data = item.getDurability();
        this.amount = item.getAmount();
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                this.title = item.getItemMeta().getDisplayName();
            }
            if (item.getItemMeta().hasLore()) {
                this.lore = (List<String>)item.getItemMeta().getLore();
            }
        }
        if (item.getEnchantments() != null) {
            this.enchantments.putAll(item.getEnchantments());
        }
        if (item.getType().toString().toLowerCase().contains("leather") && item.getItemMeta() instanceof LeatherArmorMeta) {
            final LeatherArmorMeta lam = (LeatherArmorMeta)item.getItemMeta();
            this.color = lam.getColor();
        }
    }
    
    public ItemMaker(final ItemMaker itemMaker) {
        this(itemMaker.build());
    }
    
    public ItemMaker setUnbreakable(final boolean flag) {
        this.unbreakable = flag;
        return this;
    }
    
    public ItemMaker addLore(final String... lore) {
        for (final String s : lore) {
            this.lore.add(ColorText.translate(s));
        }
        return this;
    }
    
    public ItemMaker setBase64(final String base) {
        return this;
    }
    
    public ItemMaker setTexture(final String str) {
        return this;
    }
    
    public ItemMaker setData(final int data) {
        this.data = data;
        return this;
    }
    
    public ItemMaker setAmount(final int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemMaker setTitle(final String title) {
        this.title = ColorText.translate(title);
        return this;
    }
    
    public ItemMaker setLore(final String... lore) {
        this.lore = ColorText.translate(Arrays.asList(lore));
        return this;
    }
    
    public ItemMaker setSkullType(final SkullType type) {
        Validate.notNull((Object)type);
        this.setData(type.data);
        return this;
    }
    
    public List<String> getLore() {
        return this.lore;
    }
    
    public ItemMaker setLore(final List<String> list) {
        this.lore = ColorText.translate(list);
        return this;
    }
    
    public Material getType() {
        return this.type;
    }
    
    public ItemMaker setType(final Material type) {
        this.type = type;
        return this;
    }
    
    public ItemMaker addEnchantment(final Enchantment e, final int level) {
        this.enchantments.put(e, level);
        return this;
    }
    
    public ItemMaker setColor(final Color c) {
        if (!this.type.toString().toLowerCase().contains("leather")) {
            throw new RuntimeException("Cannot set color of non-leather items.");
        }
        this.color = c;
        return this;
    }
    
    public ItemStack build() {
        Validate.noNullElements(new Object[] { this.type, this.data, this.amount });
        final ItemStack stack = new ItemStack(this.type, this.amount, (short)this.data);
        final ItemMeta im = stack.getItemMeta();
        if (this.title != null && this.title != "") {
            im.setDisplayName(this.title);
        }
        if (this.lore != null && !this.lore.isEmpty()) {
            im.setLore(this.lore);
        }
        if (this.color != null && this.type.toString().toLowerCase().contains("leather")) {
            ((LeatherArmorMeta)im).setColor(this.color);
        }
        stack.setItemMeta(im);
        if (this.enchantments != null && !this.enchantments.isEmpty()) {
            stack.addUnsafeEnchantments(this.enchantments);
        }
        if (this.unbreakable) {
            final ItemMeta meta = stack.getItemMeta();
            meta.spigot().setUnbreakable(true);
            stack.setItemMeta(meta);
        }
        return stack;
    }
    
    public ItemMaker clone() {
        return new ItemMaker(this);
    }
    
    public enum SkullType
    {
        SKELETON("SKELETON", 0, 0), 
        WITHER_SKELETON("WITHER_SKELETON", 1, 1), 
        ZOMBIE("ZOMBIE", 2, 2), 
        PLAYER("PLAYER", 3, 3), 
        CREEPER("CREEPER", 4, 4);
        
        private int data;
        
        private SkullType(final String s, final int n, final int data) {
            this.data = data;
        }
        
        public int getData() {
            return this.data;
        }
    }
}
