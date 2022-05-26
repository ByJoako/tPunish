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
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditCategoryListener implements Listener {

    public static Map<Player, String>Category=new HashMap<>();
    /*

        Edit Category
            - Menu select category edit
     */
    @Deprecated
    public static Inventory getCategory(){
        Inventory inv=Bukkit.createInventory(null, 9*3, ColorText.translate("&8[&6&lCategory&8] &bSelect"));
        int slot=0;
        for(String category: Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){
            ItemStack item;
            if(Punish.getInstance().getConfig().getBoolean("items."+category+".item.enchantment")){
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+category+".item.material")))
                        .setData(Punish.getInstance().getConfig().getInt("items."+category+".item.data"))
                        .setTitle(Punish.getInstance().getConfig().getString("items."+category+".item.displayname"))
                        .setLore(ColorText.MENU_BAR,
                                "&4Left-Click to delete category",
                                "&eRight-Click to edit category",
                                ColorText.MENU_BAR)
                        .addEnchantment(Enchantment.DURABILITY, 1).build();
            }else{
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+category+".item.material")))
                        .setData(Punish.getInstance().getConfig().getInt("items."+category+".item.data"))
                        .setTitle(Punish.getInstance().getConfig().getString("items."+category+".item.displayname"))
                        .setLore(ColorText.MENU_BAR,
                                "&4Left-Click to delete category",
                                "&eRight-Click to edit category",
                                ColorText.MENU_BAR).build();

            }
            inv.setItem(slot, item);
            slot++;
        }
        return inv;
    }

    @EventHandler
    public void onClicSelectCategory(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&6&lCategory&8] &bSelect"))){
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            Player player= (Player) event.getWhoClicked();
            for(String category:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){

                    if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate(Punish.getInstance().getConfig().getString("items." + category + ".item.displayname")))) {
                        if (event.isLeftClick()) {
                            player.openInventory(getConfirm());
                            Category.put(player, category);
                        }
                        if (event.isRightClick()) {
                            Category.put(player, category);
                            player.openInventory(EditCategoryListener.getEditCategory(player));
                        }
                    }

            }
        }
    }

    /*

    Delete Category

     */
    public Inventory getConfirm(){

        Inventory inv= Bukkit.createInventory(null, 9*3, ColorText.translate("&8[&6&lCategory&8] &bDelete"));
        ItemStack confirm=new ItemMaker(Material.WOOL).setData(5)
                .setTitle("&aConfirm").build();
        ItemStack cancel=new ItemMaker(Material.WOOL).setData(14)
                .setTitle("&cCancel").build();
        inv.setItem(11, confirm);
        inv.setItem(15, cancel);
        return inv;
    }

    @EventHandler
    public void onClickDelete(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getName().equalsIgnoreCase(ColorText.translate("&8[&6&lCategory&8] &bDelete"))){
            event.setCancelled(true);

            if(event.getSlot()==11){

                player.closeInventory();
                YamlConfiguration data=new YamlConfiguration();
                data.set(Category.get(player)+".items", null);
                Punish.getInstance().getDataConfig().setConfiguration(data);
                Punish.getInstance().getDataConfig().save();

                FileConfiguration config= Punish.getInstance().getConfig();
                config.set("items."+Category.get(player), null);
                try {
                    config.save(getConfigFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                player.sendMessage(ColorText.translate("&aCategory has been successfully removed &r"+Category.get(player)));

                Category.remove(player);

            }else if(event.getSlot()==15){

                player.closeInventory();
                player.sendMessage(ColorText.translate("&eThe deletion of the &6&l"+Category.get(player)+" &ecategory has been canceled"));
                Category.remove(player);
            }
        }
    }

    /*
        - Menu edit category
     */

    public static Inventory getEditCategory(Player player){
        Inventory inv= Bukkit.createInventory(null,9*2, ColorText.translate("&8[&6&lCategory&8] &bEditing"));
        ItemStack title=new ItemMaker(Material.SIGN).setTitle("&dSet title")
                .setLore(ColorText.MENU_BAR,
                        "&fTitle&7: &f"+ Punish.getInstance().getConfig().getString("items."+Category.get(player)+".menu.title"), ColorText.MENU_BAR).build();
        ItemStack size=new ItemMaker(Material.PAINTING).setTitle("&dSet size")
                .setLore(ColorText.MENU_BAR,
                        "&fSize&7: &f"+Punish.getInstance().getConfig().getInt("items."+Category.get(player)+".menu.size"), ColorText.MENU_BAR).build();
        ItemStack fill;
        if(Punish.getInstance().getConfig().getBoolean("items."+Category.get(player)+".menu.fill.enabled")){
            fill=new ItemMaker(Material.REDSTONE_TORCH_ON).setTitle("&dFill&7: &aEnabled")
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &cdisabled", ColorText.MENU_BAR).build();
        }else{
            fill=new ItemMaker(Material.REDSTONE_TORCH_ON).setTitle("&dFill&7: &cDisabled")
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &aenabled", ColorText.MENU_BAR).build();
        }
        ItemStack fillcolor=new ItemMaker(Material.STAINED_GLASS_PANE).setTitle("&dfill color")
                .setData(Punish.getInstance().getConfig().getInt("items."+Category.get(player)+".menu.fill.data"))
                .setLore(ColorText.MENU_BAR,
                        "&fClick to change color", ColorText.MENU_BAR).build();

        inv.setItem(1, title);
        inv.setItem(3, size);
        inv.setItem(5, fill);
        inv.setItem(7, fillcolor);

        ItemStack displayname=new ItemMaker(Material.BLAZE_POWDER).setTitle("&dSet displayname")
                .setLore(ColorText.MENU_BAR,
                        "&fdisplayname&7: &f"+ Punish.getInstance().getConfig().getString("items."+Category.get(player)+".item.displayname"), ColorText.MENU_BAR).build();
        ItemStack slot=new ItemMaker(Material.ITEM_FRAME).setTitle("&dSet slot")
                .setLore(ColorText.MENU_BAR,
                        "&fslot&7: &f"+ Punish.getInstance().getConfig().getInt("items."+Category.get(player)+".item.slot"), ColorText.MENU_BAR).build();
        @Deprecated
        ItemStack item=new ItemMaker(Material.getMaterial(Punish.getInstance().getConfig().getInt("items."+Category.get(player)+".item.material")))
                .setData(Punish.getInstance().getConfig().getInt("items."+Category.get(player)+".item.data"))
                .setTitle("&dSet Item")
                .setLore(ColorText.MENU_BAR,
                        "&fClick to change item", ColorText.MENU_BAR).build();
        ItemStack enchant;
        if(Punish.getInstance().getConfig().getBoolean("items."+Category.get(player)+".item.enchantment")){
            enchant=new ItemMaker(Material.ENCHANTMENT_TABLE).setTitle("&bEnchantment&7: &aEnabled")
                    .addEnchantment(Enchantment.DURABILITY, 1)
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &cdisabled", ColorText.MENU_BAR).build();
        }else{
            enchant=new ItemMaker(Material.ENCHANTMENT_TABLE).setTitle("&bEnchantment&7: &cDisabled")
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &aenabled", ColorText.MENU_BAR).build();
        }
        ItemStack lore=new ItemMaker(Material.PAPER).setTitle("&dChange lore")
                .setLore(ColorText.MENU_BAR,
                        "&6Coming soon", ColorText.MENU_BAR).build();//Click to open menu edit lore
        inv.setItem(9, displayname);
        inv.setItem(11, item);
        inv.setItem(13, slot);
        inv.setItem(15, enchant);
        inv.setItem(17, lore);

        return inv;
    }
    public static Map<Player, String> EditCategory=new HashMap<>();

    @EventHandler
    public void onClickMainmenu(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&6&lCategory&8] &bEditing"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            FileConfiguration config= Punish.getInstance().getConfig();
            switch (event.getSlot()){
                case 1:
                    EditCategory.put(player, "title");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
                case 3:
                    EditCategory.put(player, "size");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
                case 5:
                    if(Punish.getInstance().getConfig().getBoolean("items."+Category.get(player)+".menu.fill.enabled")){
                        config.set("items."+Category.get(player)+".menu.fill.enabled", false);
                    }else{
                        config.set("items."+Category.get(player)+".menu.fill.enabled", true);
                    }
                    try {
                        config.save(getConfigFile());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> player.openInventory(getEditCategory(player)), 1L);
                    break;
                case 7:
                    player.openInventory(getSetfill());
                    break;
                case 9:
                    EditCategory.put(player, "displayname");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
                case 11:
                    player.closeInventory();
                    EditCategory.put(player, "item");
                    player.sendMessage(ColorText.translate("&eRight click on an item"));
                    break;
                case 13:
                    player.openInventory(getSetSlot());
                    break;
                case 15:
                    if(Punish.getInstance().getConfig().getBoolean("items."+Category.get(player)+".item.enchantment")){
                        config.set("items."+Category.get(player)+".item.enchantment", false);
                    }else{
                        config.set("items."+Category.get(player)+".item.enchantment", true);
                    }
                    try {
                        config.save(getConfigFile());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> player.openInventory(getEditCategory(player)), 1L);
                    break;
            }
        }
    }

    /*
        - Set slot menu
     */
    
    public Inventory getSetSlot(){
        ItemStack panel=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&aAvailable")).setData(5).build();
        ItemStack noavailable=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&cNot Available")).setData(14).build();
        Inventory inv=Bukkit.createInventory(null, 9*5, ColorText.translate("&8[&6&lCategory&8] &bSet slot"));

        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, panel);
        }
        for(String itemlist:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){
            inv.setItem(Punish.getInstance().getConfig().getInt("items." + itemlist + ".item.slot") -1, noavailable);
        }

        return inv;
    }


    @EventHandler
    public void onClickeditKits(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getName().equalsIgnoreCase(ColorText.translate("&8[&6&lCategory&8] &bSet slot"))){
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&aAvailable"))){
                int slot= event.getSlot();
                FileConfiguration config= Punish.getInstance().getConfig();
                config.set("items."+Category.get(player)+".item.slot", slot+1);
                try {
                    config.save(getConfigFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> player.openInventory(getEditCategory(player)), 1L);
            }

        }
    }

    /*
        - Select item
     */

    @EventHandler @Deprecated
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            if (EditCategory.containsKey(player) && EditCategory.get(player).equalsIgnoreCase("item")) {
                FileConfiguration config= Punish.getInstance().getConfig();

                int material=player.getItemInHand().getTypeId();
                int data=player.getItemInHand().getData().getData();
                config.set("items."+Category.get(player)+".item.material", material);
                config.set("items."+Category.get(player)+".item.data", data);
                try {
                    config.save(getConfigFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                EditCategory.remove(player);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> player.openInventory(getEditCategory(player)), 1L);
            }
        }
    }

    /*
        - Edit Fill menu
     */

    public Inventory getSetfill(){
        Inventory inv=Bukkit.createInventory(null, 9*2,ColorText.translate("&b&lSet color fill"));
        for(int j=0;j<=15;j++){
            ItemStack fill=new ItemMaker(Material.STAINED_GLASS_PANE).setData(j).setTitle("").build();
            inv.setItem(j, fill);
        }
        return inv;
    }

    @EventHandler
    public void onClicksetfill(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&b&lSet color fill"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            if(event.getCurrentItem()!=null && event.getCurrentItem().getType()== Material.STAINED_GLASS_PANE){
                FileConfiguration config= Punish.getInstance().getConfig();
                config.set("items."+Category.get(player)+".menu.fill.data", event.getSlot());
                try {
                    config.save(getConfigFile());
                }catch (IOException e){
                    e.printStackTrace();
                }

                Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> player.openInventory(getEditCategory(player)), 1L);
            }
        }
    }

    public File getConfigFile() {
        return new File(Punish.getInstance().getDataFolder(), "config.yml");
    }
}
