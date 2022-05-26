package net.helydev.com.listener.edit;

import net.helydev.com.Punish;
import net.helydev.com.utils.ColorText;
import net.helydev.com.utils.ItemMaker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditListener implements Listener {


    public static Map<Player, String>EditMainmenu=new HashMap<>();

    public static Inventory getEditMenu(){
        Inventory inv= Bukkit.createInventory(null, 9*3, ColorText.translate("&6&ltPunish Manager"));

            ItemStack menu=new ItemMaker(Material.BOOK)
                    .setTitle("&b&lEdit Principal menu")
                    .setLore(ColorText.MENU_BAR,
                            "&eClick to edit main menu",
                            ColorText.MENU_BAR).build();
            ItemStack item=new ItemMaker(Material.BOOK)
                    .setTitle("&b&lEdit Items")
                    .setLore(ColorText.MENU_BAR,
                            "&eClick to edit category items",
                            "&aShift-Click to create item",
                            ColorText.MENU_BAR).build();
            ItemStack category=new ItemMaker(Material.BOOK)
                    .setTitle("&b&lEdit Category")
                    .setLore(ColorText.MENU_BAR,
                            "&eClick to edit the categories",
                            "&aShift-Click to create category",
                            ColorText.MENU_BAR).build();
            ItemStack Pane=new ItemMaker(Material.STAINED_GLASS_PANE).setTitle("").setData(7).build();
            for(int j=0; j<inv.getSize();j++){
                inv.setItem(j, Pane);
            }
            inv.setItem(11, menu);
            inv.setItem(13, category);
            inv.setItem(15, item);

        return inv;
    }

    public static List<Player>create=new ArrayList<>();
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&6&ltPunish Manager"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            if(event.getSlot()==13){
                if(event.isShiftClick()){
                    player.closeInventory();
                    create.add(player);
                    player.sendMessage(ColorText.translate("&eWrite the name of the category"));
                }else {
                    player.openInventory(EditCategoryListener.getCategory());
                }
                return;
            }
            if(event.getSlot()==11){
                player.openInventory(getEditMainMenu());
                return;
            }
            if(event.getSlot()==15){
                if(event.isShiftClick()){
                    player.openInventory(CreateListener.getCategory());
                }else{
                player.openInventory(EditItemListener.getCategory());
                }
            }
        }
    }
    /*

        Edit main menu
            - Menu edit
     */
    public Inventory getEditMainMenu(){
        Inventory inv=Bukkit.createInventory(null,9, ColorText.translate("&8[&6&lMain&8] &bEditing"));
        ItemStack title=new ItemMaker(Material.SIGN).setTitle("&dSet title")
                .setLore(ColorText.MENU_BAR,
                "&fTitle&7: &f"+Punish.getInstance().getConfig().getString("menu.principal.title"), ColorText.MENU_BAR).build();
        ItemStack size=new ItemMaker(Material.PAINTING).setTitle("&dSet size")
                .setLore(ColorText.MENU_BAR,
                        "&fSize&7: &f"+Punish.getInstance().getConfig().getInt("menu.principal.size"), ColorText.MENU_BAR).build();
        ItemStack fill;
        if(Punish.getInstance().getConfig().getBoolean("menu.principal.fill.enabled")){
            fill=new ItemMaker(Material.REDSTONE_TORCH_ON).setTitle("&dFill&7: &aEnabled")
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &cdisabled", ColorText.MENU_BAR).build();
        }else{
            fill=new ItemMaker(Material.REDSTONE_TORCH_ON).setTitle("&dFill&7: &cDisabled")
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &aenabled", ColorText.MENU_BAR).build();
        }
        ItemStack fillcolor=new ItemMaker(Material.STAINED_GLASS_PANE).setTitle("&dfill color")
                .setData(Punish.getInstance().getConfig().getInt("menu.principal.fill.data"))
                .setLore(ColorText.MENU_BAR,
                        "&fClick to change color", ColorText.MENU_BAR).build();

        inv.setItem(1, title);
        inv.setItem(3, size);
        inv.setItem(5, fill);
        inv.setItem(7, fillcolor);

        return inv;
    }

    @EventHandler
    public void onClickMainmenu(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&6&lMain&8] &bEditing"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            switch (event.getSlot()){
                case 1:
                    EditMainmenu.put(player, "title");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
                case 3:
                    EditMainmenu.put(player, "size");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
                case 5:
                    FileConfiguration config= Punish.getInstance().getConfig();
                    if(Punish.getInstance().getConfig().getBoolean("menu.principal.fill.enabled")){
                        config.set("menu.principal.fill.enabled", false);
                    }else{
                        config.set("menu.principal.fill.enabled", true);
                    }
                    try {
                        config.save(getConfigFile());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> {
                        player.openInventory(getEditMainMenu());
                    }, 1L);
                    break;
                case 7:
                    player.openInventory(getSetfill());
                    break;
            }
        }
    }

    /*
        - Menu Fill Edit
     */

    public Inventory getSetfill(){
        Inventory inv=Bukkit.createInventory(null, 9*2,ColorText.translate("&8[&b&lMenu&8] &d&lSet color"));
        for(int j=0;j<=15;j++){
            ItemStack fill=new ItemMaker(Material.STAINED_GLASS_PANE).setData(j).setTitle("").build();
            inv.setItem(j, fill);
        }
        return inv;
    }

    @EventHandler
    public void onClicksetfill(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&b&lMenu&8] &d&lSet color"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            if(event.getCurrentItem()!=null && event.getCurrentItem().getType()== Material.STAINED_GLASS_PANE){
                FileConfiguration config= Punish.getInstance().getConfig();
                config.set("menu.principal.fill.data", event.getSlot());
                try {
                    config.save(getConfigFile());
                }catch (IOException e){
                    e.printStackTrace();
                }

                Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> {
                    player.openInventory(getEditMainMenu());
                }, 1L);
            }
        }
    }


    public File getConfigFile() {
        return new File(Punish.getInstance().getDataFolder(), "config.yml");
    }
}
