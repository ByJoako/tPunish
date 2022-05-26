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
import java.util.List;
import java.util.Map;

public class EditItemListener implements Listener {

    public static Inventory getCategory(){
        Inventory inv=Bukkit.createInventory(null, 9*3, ColorText.translate("&8[&6&lItem&8] &bSelect Category"));
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
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&6&lItem&8] &bSelect Category"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            for(String category:Punish.getInstance().getConfig().getConfigurationSection("items").getKeys(false)){

                    if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate(Punish.getInstance().getConfig().getString("items." + category + ".item.displayname")))) {

                        EditCategoryListener.Category.put(player, category);
                        player.openInventory(getItems(player));
                    }

            }
        }
    }

    public static Inventory getItems(Player player){
        Inventory inv=Bukkit.createInventory(null, 9*5, ColorText.translate("&8[&6&lItem&8] &6Select item"));
        int slot=0;
        for(String items: Punish.getInstance().getDataConfig().getConfiguration().getConfigurationSection(EditCategoryListener.Category.get(player)+".items").getKeys(false)){
            ItemStack item;
            if(Punish.getInstance().getDataConfig().getConfiguration().getBoolean(EditCategoryListener.Category.get(player)+".items."+items+".enchantment")){
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+items+".material")))
                        .setData(Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+items+".data"))
                        .setTitle(Punish.getInstance().getDataConfig().getConfiguration().getString(EditCategoryListener.Category.get(player)+".items."+items+".displayname"))
                        .setLore(ColorText.MENU_BAR,
                                "&4Left-Click to delete item",
                                "&eRight-Click to edit item",
                                ColorText.MENU_BAR)
                        .addEnchantment(Enchantment.DURABILITY, 1).build();
            }else{
                item=new ItemMaker(Material.getMaterial(Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+items+".material")))
                        .setData(Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+items+".data"))
                        .setTitle(Punish.getInstance().getDataConfig().getConfiguration().getString(EditCategoryListener.Category.get(player)+".items."+items+".displayname"))
                    .setLore(ColorText.MENU_BAR,
                            "&4Left-Click to delete item",
                            "&eRight-Click to edit item",
                            ColorText.MENU_BAR).build();

            }
            inv.setItem(slot, item);
            slot++;
        }
        return inv;
    }
    public static Map<Player, String> Items=new HashMap<>();
    @EventHandler
    public void onClicSelectCategory(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&6&lItem&8] &6Select item"))){
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            Player player= (Player) event.getWhoClicked();
            for(String item:Punish.getInstance().getDataConfig().getConfiguration().getConfigurationSection(EditCategoryListener.Category.get(player)+".items").getKeys(false)){

                    if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(
ColorText.translate(Punish.getInstance().getDataConfig().getConfiguration().getString(EditCategoryListener.Category.get(player)+".items." + item + ".displayname")))) {
                        if (event.isLeftClick()) {
                            player.openInventory(getConfirm(item));
                            Items.put(player, item);
                        }
                        if (event.isRightClick()) {
                            Items.put(player, item);
                            player.openInventory(getEditItems(player));
                        }
                    }

            }
        }
    }

    public Inventory getConfirm(String Category){

        Inventory inv= Bukkit.createInventory(null, 9*3, ColorText.translate("&8[&6&lItem&8] &6Delete"));
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
        if(event.getInventory().getName().equalsIgnoreCase(ColorText.translate("&8[&6&lItem&8] &6Delete"))){
            event.setCancelled(true);

            if(event.getSlot()==11){

                player.closeInventory();
                FileConfiguration data=Punish.getInstance().getDataConfig().getConfiguration();
                data.set(EditCategoryListener.Category.get(player)+".items."+Items.get(player), null);
                try {
                    data.save(getDataFile());
                }catch (IOException e){
                    e.printStackTrace();
                }

                player.sendMessage(ColorText.translate("&aItem has been successfully removed"));

                EditCategoryListener.Category.remove(player);
                Items.remove(player);

            }else if(event.getSlot()==15){

                player.closeInventory();
                player.sendMessage(ColorText.translate("&eThe deletion of the &6&l"+Items.get(player)+" &eitem has been canceled"));

                EditCategoryListener.Category.remove(player);
                Items.remove(player);
            }
        }
    }



    public Inventory getEditItems(Player player){
        Inventory inv= Bukkit.createInventory(null,9, ColorText.translate("&8[&6&lItem&8] &b&lEditing"));

        ItemStack displayname=new ItemMaker(Material.BLAZE_POWDER).setTitle("&dSet displayname")
                .setLore(ColorText.MENU_BAR,
                        "&fdisplayname&7: &f"+ Punish.getInstance().getDataConfig().getConfiguration().getString(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".displayname"), ColorText.MENU_BAR).build();
        ItemStack slot=new ItemMaker(Material.ITEM_FRAME).setTitle("&dSet slot")
                .setLore(ColorText.MENU_BAR,
                        "&fslot&7: &f"+ Items.get(player), ColorText.MENU_BAR).build();
        ItemStack item=new ItemMaker(Material.getMaterial(Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".material")))
                .setData(Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".data"))
                .setTitle("&dSet Item")
                .setLore(ColorText.MENU_BAR,
                        "&fClick to change item", ColorText.MENU_BAR).build();
        ItemStack enchant;
        if(Punish.getInstance().getDataConfig().getConfiguration().getBoolean(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".enchantment")){
            enchant=new ItemMaker(Material.ENCHANTMENT_TABLE).setTitle("&bEnchantment&7: &aEnabled")
                    .addEnchantment(Enchantment.DURABILITY, 1)
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &cdisabled", ColorText.MENU_BAR).build();
        }else{
            enchant=new ItemMaker(Material.ENCHANTMENT_TABLE).setTitle("&bEnchantment&7: &cDisabled")
                    .setLore(ColorText.MENU_BAR,
                            "&fClick to &aenabled", ColorText.MENU_BAR).build();
        }
        ItemStack command=new ItemMaker(Material.PAPER).setTitle("&dChange command")
                .setLore(ColorText.MENU_BAR,
                        "&fClick to change command", ColorText.MENU_BAR).build();//Click to open menu edit lore
        inv.setItem(0, displayname);
        inv.setItem(2, item);
        inv.setItem(4, slot);
        inv.setItem(6, enchant);
        inv.setItem(8, command);

        return inv;
    }
    public static Map<Player, String> EditItem=new HashMap<>();

    @EventHandler
    public void onClickMainmenu(InventoryClickEvent event){
        if(event.getInventory().getTitle().equalsIgnoreCase(ColorText.translate("&8[&6&lItem&8] &b&lEditing"))){
            event.setCancelled(true);
            Player player= (Player) event.getWhoClicked();
            FileConfiguration config= Punish.getInstance().getDataConfig().getConfiguration();
            switch (event.getSlot()){
                case 0:
                    EditItem.put(player, "displayname");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
                case 2:
                    player.closeInventory();
                    EditItem.put(player, "item");
                    player.sendMessage(ColorText.translate("&eRight click on an item"));
                    break;
                case 4:
                    player.openInventory(getSetSlot(player));
                    break;
                case 6:
                    if(Punish.getInstance().getConfig().getBoolean(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".enchantment")){
                        config.set(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".enchantment", false);
                    }else{
                        config.set(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".enchantment", true);
                    }try {
                    config.save(getDataFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                    Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> {
                        player.openInventory(getEditItems(player));
                    }, 1L);
                    break;
                case 8:
                    EditItem.put(player, "command");
                    player.closeInventory();
                    player.sendMessage(ColorText.translate("&eType \"exit\" to cancel the edit"));
                    break;
            }
        }
    }

    public Inventory getSetSlot(Player player){
        ItemStack panel=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&aAvailable")).setData(5).build();
        ItemStack noavailable=new ItemMaker(Material.STAINED_GLASS_PANE)
                .setTitle(ColorText.translate("&cNot Available")).setData(14).build();
        Inventory inv=Bukkit.createInventory(null, 9*5, ColorText.translate("&8[&6&lItem&8] &bSet Slot"));

        for (int i = 0; i < inv.getSize(); ++i) {
            inv.setItem(i, panel);
        }
        for(String itemlist:Punish.getInstance().getDataConfig().getConfiguration().getConfigurationSection(EditCategoryListener.Category.get(player)+".items").getKeys(false)){
            inv.setItem(Integer.parseInt(itemlist)-1, noavailable);
        }

        return inv;
    }


    @EventHandler
    public void onClickeditKits(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if(event.getInventory().getName().equalsIgnoreCase(ColorText.translate("&8[&6&lItem&8] &bSet Slot"))){
            if (event.getCurrentItem() == null || event.getSlotType() == null || event.getCurrentItem().getType() == Material.AIR) {
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            if(event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ColorText.translate("&aAvailable"))){
                int slot= event.getSlot();
                FileConfiguration data=Punish.getInstance().getDataConfig().getConfiguration();
                slot=slot+1;
                data.set(EditCategoryListener.Category.get(player)+".items."+slot+".displayname",
                        Punish.getInstance().getDataConfig().getConfiguration().getString(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".displayname"));
                data.set(EditCategoryListener.Category.get(player)+".items."+slot+".material",
                        Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".material"));
                data.set(EditCategoryListener.Category.get(player)+".items."+slot+".data",
                        Punish.getInstance().getDataConfig().getConfiguration().getInt(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".data"));
                data.set(EditCategoryListener.Category.get(player)+".items."+slot+".enchantment",
                        Punish.getInstance().getDataConfig().getConfiguration().getBoolean(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".enchantment"));
                data.set(EditCategoryListener.Category.get(player)+".items."+slot+".command",
                        Punish.getInstance().getDataConfig().getConfiguration().getString(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".command"));
                List<String>lore=Punish.getInstance().getDataConfig().getConfiguration().getStringList(EditCategoryListener.Category.get(player)+".items."+Items.get(player)+".lore");
                data.set(EditCategoryListener.Category.get(player)+".items."+slot+".lore", lore);
                data.set(EditCategoryListener.Category.get(player)+".items."+Items.get(player), null);
                try {
                    data.save(getDataFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                player.closeInventory();
                EditCategoryListener.Category.remove(player);
                Items.remove(player);
            }

        }
    }




    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player player = event.getPlayer();
            if (EditItem.containsKey(player) && EditItem.get(player).equalsIgnoreCase("item")) {
                FileConfiguration dataa=Punish.getInstance().getDataConfig().getConfiguration();
                int material = player.getItemInHand().getTypeId();
                int data = player.getItemInHand().getData().getData();
                dataa.set(EditCategoryListener.Category.get(player)+".items." + Items.get(player) + ".material", null);
                dataa.set(EditCategoryListener.Category.get(player)+".items." + Items.get(player) + ".data", data);
                try {
                    dataa.save(getDataFile());
                }catch (IOException e){
                    e.printStackTrace();
                }
                EditItem.remove(player);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Punish.getInstance(), () -> {
                    player.openInventory(getEditItems(player));
                }, 1L);
            }
        }
    }
public File getDataFile() {
        return new File(Punish.getInstance().getDataFolder(), "data.yml");

    }
}
