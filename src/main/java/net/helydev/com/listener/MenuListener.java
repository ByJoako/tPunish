package net.helydev.com.listener;

import net.helydev.com.Punish;
import net.helydev.com.command.PunishCommand;
import net.helydev.com.utils.ColorText;
import net.helydev.com.utils.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MenuListener implements Listener {

    public Map<Player, String>SetSubMenu=new HashMap<>();
    @Deprecated
    public static Inventory getPunishMenu(){
        Inventory inv= Bukkit.createInventory(null,
                Punish.getInstance().getConfig().getInt("menu.principal.size")*9,
                ColorText.translate(Punish.getInstance().getConfig().getString("menu.principal.title")));
        if(Punish.getInstance().getConfig().getBoolean("menu.principal.fill.enabled")){
            ItemStack fill=new ItemMaker(Material.STAINED_GLASS_PANE).setTitle("").setData(Punish.getInstance().getConfig().getInt("menu.principal.fill.data")).build();
            for(int j=0; j<inv.getSize();j++){
                inv.setItem(j, fill);
            }
        }
        for(String items:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){
            ItemStack item;
            if(Punish.getInstance().getConfig().getBoolean("items."+items+".item.enchantment")){
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+items+".item.material")))
                        .setData(Punish.getInstance().getConfig().getInt("items."+items+".item.data"))
                        .setTitle(ColorText.translate(Punish.getInstance().getConfig().getString("items."+items+".item.displayname")))
                        .setLore(Punish.getInstance().getConfig().getStringList("items."+items+".item.lore"))
                        .addEnchantment(Enchantment.DURABILITY, 1).build();
            }else{
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+items+".item.material")))
                        .setData(Punish.getInstance().getConfig().getInt("items."+items+".item.data"))
                        .setTitle(ColorText.translate(Punish.getInstance().getConfig().getString("items."+items+".item.displayname")))
                        .setLore(Punish.getInstance().getConfig().getStringList("items."+items+".item.lore")).build();
            }
            inv.setItem(Punish.getInstance().getConfig().getInt("items."+items+".item.slot")-1, item);
        }
        return inv;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate(Punish.getInstance().getConfig().getString("menu.principal.title")))){
            event.setCancelled(true);
            for(String items:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){
                if(event.getSlot()==Punish.getInstance().getConfig().getInt("items."+items+".item.slot")-1){
                    Player player= (Player) event.getWhoClicked();
                    player.openInventory(getSubMenu(items));
                    SetSubMenu.put(player, items);
                }
            }
        }
    }
    @Deprecated
    public Inventory getSubMenu(String items){
        Inventory inv=Bukkit.createInventory(null,
                Punish.getInstance().getConfig().getInt("items."+items+".menu.size")*9,
                ColorText.translate(Punish.getInstance().getConfig().getString("items."+items+".menu.title")));
        if(Punish.getInstance().getConfig().getBoolean("items."+items+".menu.fill.enabled")){
            ItemStack fill=new ItemMaker(Material.STAINED_GLASS_PANE).setTitle("").setData(Punish.getInstance().getConfig().getInt("items."+items+".menu.fill.data")).build();
            for(int j=0; j<inv.getSize();j++){
                inv.setItem(j, fill);
            }
        }
        for(String itemlist:Punish.getInstance().getDataConfig().getConfiguration().getConfigurationSection(items+".items").getKeys(false)){
            ItemStack item;
            if(Punish.getInstance().getDataConfig().getConfiguration().getBoolean(items+".items."+itemlist+".enchantment")){
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getDataConfig().getConfiguration().getInt(items+".items."+itemlist+".material")))
                        .setData(Punish.getInstance().getDataConfig().getConfiguration().getInt(items+".items."+itemlist+".data"))
                        .setTitle(Punish.getInstance().getDataConfig().getConfiguration().getString(items+".items."+itemlist+".displayname"))
                        .setLore(Punish.getInstance().getDataConfig().getConfiguration().getStringList(items+".items."+itemlist+".lore"))
                        .addEnchantment(Enchantment.DURABILITY, 1)
                        .build();
            }else{
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getDataConfig().getConfiguration().getInt(items+".items."+itemlist+".material")))
                        .setData(Punish.getInstance().getDataConfig().getConfiguration().getInt(items+".items."+itemlist+".data"))
                        .setTitle(Punish.getInstance().getDataConfig().getConfiguration().getString(items+".items."+itemlist+".displayname"))
                        .setLore(Punish.getInstance().getDataConfig().getConfiguration().getStringList(items+".items."+itemlist+".lore"))
                        .build();
            }
            inv.setItem(Integer.parseInt(itemlist)-1, item);
        }
        return inv;
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event){
        Player player= (Player) event.getWhoClicked();
        if(SetSubMenu.containsKey(player)){
            if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate(Punish.getInstance().getConfig().getString("items."+SetSubMenu.get(player)+".menu.title")))){
                event.setCancelled(true);
                for(String itemlist:Punish.getInstance().getDataConfig().getConfiguration().getConfigurationSection(SetSubMenu.get(player)+".items").getKeys(false)){
                    if(event.getSlot()==Integer.parseInt(itemlist)-1){
                        player.closeInventory();
                        player.chat(Punish.getInstance().getDataConfig().getConfiguration().getString(SetSubMenu.get(player)+".items."+itemlist+".command").replace("%target%", PunishCommand.Target.get(player)));
                        SetSubMenu.remove(player);
                        PunishCommand.Target.remove(player);
                    }
                }
            }
        }
    }
}
