package net.helydev.com.listener.edit;

import net.helydev.com.Punish;
import net.helydev.com.utils.ColorText;
import net.helydev.com.utils.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateListener implements Listener {
    Map<Player, String>category=new HashMap<>();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(EditListener.create.contains(event.getPlayer())){
            event.setCancelled(true);
            if(!Punish.getInstance().getConfig().contains("items."+event.getMessage())) {
                category.put(event.getPlayer(), event.getMessage());
                this.getSetSlotCategory(event.getPlayer());
            }
            else{
                EditListener.create.remove(event.getPlayer());
            }
        }
    }



    public void getSetSlotCategory(Player player){
        ItemStack panel=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&aAvailable")).setData(5).build();
        ItemStack noavailable=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&cNot Available")).setData(14).build();
        Inventory inv= Bukkit.createInventory(null, 9*5, ColorText.translate("&6Set slot"));

        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, panel);
        }
        for(String itemlist:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){
            inv.setItem(Punish.getInstance().getConfig().getInt("items." + itemlist + ".item.slot") -1, noavailable);
        }

        player.openInventory(inv);
    }


    @EventHandler
    public void onClicked(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getName().equalsIgnoreCase(ColorText.translate("&6Set slot"))){
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&aAvailable"))){
                int slot= event.getSlot();
                FileConfiguration config= Punish.getInstance().getConfig();
                List<String>lore=new ArrayList<>();
                lore.add("&7-----------------------------");
                lore.add("&7Click to open menu "+category.get(player));
                lore.add("&7-----------------------------");
                config.set("items." +category.get(player) + ".item.material", 35);
                config.set("items." +category.get(player)+ ".item.data", 14);
                config.set("items." + category.get(player) + ".item.displayname", "&e"+category.get(player));
                config.set("items."+category.get(player)+".item.slot", slot+1);
                config.set("items." + category.get(player) + ".item.enchantment", false);
                config.set("items." +category.get(player)+ ".item.lore", lore);
                config.set("items." +category.get(player)+ ".menu.title", category.get(player)+" &7menu");
                config.set("items." + category.get(player)+ ".menu.size", 5);
                config.set("items." +category.get(player)+ ".menu.fill.enabled", true);
                config.set("items." + category.get(player)+ ".menu.fill.data", 7);
                try {
                    config.save(getConfigFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                player.closeInventory();
                player.sendMessage(ColorText.translate("&aThe category was created successfully"));
                category.remove(player);
                EditListener.create.remove(player);
            }

        }
    }


    public static Inventory getCategory(){
        Inventory inv=Bukkit.createInventory(null, 9*3, ColorText.translate("&bSelect Category"));
        int slot=0;
        for(String category: Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){
            ItemStack item;
            if(Punish.getInstance().getConfig().getBoolean("items."+category+".item.enchantment")){
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+category+".item.material")))
                        .setData(Punish.getInstance().getConfig().getInt("items."+category+".item.data"))
                        .setTitle(Punish.getInstance().getConfig().getString("items."+category+".item.displayname"))
                        .setLore(ColorText.MENU_BAR,
                                "&7Click to select category",
                                ColorText.MENU_BAR)
                        .addEnchantment(Enchantment.DURABILITY, 1).build();
            }else{
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+category+".item.material")))
                        .setData(Punish.getInstance().getConfig().getInt("items."+category+".item.data"))
                        .setTitle(Punish.getInstance().getConfig().getString("items."+category+".item.displayname"))
                        .setLore(ColorText.MENU_BAR,"&7Click to select category",
                                ColorText.MENU_BAR).build();

            }
            inv.setItem(slot, item);
            slot++;
        }
        return inv;
    }

    @EventHandler
    public void onClicSelect(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&bSelect Category"))){
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }

            Player player= (Player) event.getWhoClicked();
            for(String categorys:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){

                    if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate(Punish.getInstance().getConfig().getString("items." + categorys + ".item.displayname")))) {

                        player.openInventory(getSetSlotItem(player, categorys));
                        category.put(player, categorys);
                    }

            }
        }
    }



    public Inventory getSetSlotItem(Player player, String c){
        ItemStack panel=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&aAvailable")).setData(5).build();
        ItemStack noavailable=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&cNot Available")).setData(14).build();
        Inventory inv=Bukkit.createInventory(null, 9*5, ColorText.translate("&bSet Slot &7"));

        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, panel);
        }
        if(Punish.getInstance().getDataConfig().getConfiguration().contains(c+".items")){
            for(String itemlist:Punish.getInstance().getDataConfig().getConfiguration().getConfigurationSection(c+".items").getKeys(false)){
                inv.setItem(Integer.parseInt(itemlist)-1, noavailable);
            }
        }

        return inv;
    }


    @EventHandler
    public void onClickeditKits(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getName().equalsIgnoreCase(ColorText.translate("&bSet Slot &7"))){
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&aAvailable"))){
                int slot= event.getSlot();
                FileConfiguration data=Punish.getInstance().getDataConfig().getConfiguration();
                slot=slot+1;
                data.set(category.get(player)+".items."+slot+".displayname", "&eDefault");
                data.set(category.get(player)+".items."+slot+".material", 340);
                data.set(category.get(player)+".items."+slot+".data", 0);
                data.set(category.get(player)+".items."+slot+".enchantment", false);
                data.set(category.get(player)+".items."+slot+".command", "/test %target%");
                List<String>lore=new ArrayList<>();
                lore.add("");
                data.set(category.get(player)+".items."+slot+".lore", lore);
                try {
                    data.save(getDATAfILE());
                }catch (IOException e){
                    e.printStackTrace();
                }
                player.closeInventory();
                player.sendMessage(ColorText.translate("&aItem created successfully"));
            }

        }
    }




    public File getDATAfILE() {
        return new File(Punish.getInstance().getDataFolder(), "data.yml");
    }

    public File getConfigFile() {
        return new File(Punish.getInstance().getDataFolder(), "config.yml");
    }
}
