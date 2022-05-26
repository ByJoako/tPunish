package net.helydev.com.listener.edit;

import net.helydev.com.Punish;
import net.helydev.com.utils.ColorText;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;

public class OnChat implements Listener {

    @EventHandler
    public void onChatItems(AsyncPlayerChatEvent event){
        if(EditItemListener.EditItem.containsKey(event.getPlayer())){
            event.setCancelled(true);
            FileConfiguration data=Punish.getInstance().getDataConfig().getConfiguration();
            if (event.getMessage() == null || event.getMessage().isEmpty()) {
                event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eTry back"));
                return;
            }
            if(event.getMessage().equalsIgnoreCase("exit")){
                event.getPlayer().sendMessage(ColorText.translate("&eEdit mode has been canceled"));
                EditCategoryListener.EditCategory.remove(event.getPlayer());
                return;
            }if(EditItemListener.EditItem.get(event.getPlayer()).equalsIgnoreCase("displayname")){
                data.set(EditCategoryListener.Category.get(event.getPlayer())+".items."+EditItemListener.Items.get(event.getPlayer())+".displayname", event.getMessage());
                event.getPlayer().sendMessage(ColorText.translate("&aThe displayname has been changed to &6"+event.getMessage()));
            }else if(EditItemListener.EditItem.get(event.getPlayer()).equalsIgnoreCase("command")){
                data.set(EditCategoryListener.Category.get(event.getPlayer())+".items."+EditItemListener.Items.get(event.getPlayer())+".command", "/"+event.getMessage());
                event.getPlayer().sendMessage(ColorText.translate("&aThe command has been changed to &f/"+event.getMessage()));
            }
            try {
                data.save(getDataFile());
            }catch (IOException e){
                e.printStackTrace();
            }
            EditItemListener.EditItem.remove(event.getPlayer());
        }

    }
    @EventHandler
    public void onChatCategory(AsyncPlayerChatEvent event){
        if(EditCategoryListener.EditCategory.containsKey(event.getPlayer())){
            event.setCancelled(true);
            FileConfiguration config= Punish.getInstance().getConfig();
            if (event.getMessage() == null || event.getMessage().isEmpty()) {
                event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eTry back"));
                return;
            }
            if(event.getMessage().equalsIgnoreCase("exit")){
                event.getPlayer().sendMessage(ColorText.translate("&eEdit mode has been canceled"));
                EditCategoryListener.EditCategory.remove(event.getPlayer());
                return;
            }
            if(EditCategoryListener.EditCategory.get(event.getPlayer()).equalsIgnoreCase("title")){
                config.set("items", null);
                event.getPlayer().sendMessage(ColorText.translate("&aThe title of the main menu has been changed to &f"+event.getMessage()));
            }else if(EditCategoryListener.EditCategory.get(event.getPlayer()).equalsIgnoreCase("size")){
                String size=event.getMessage();
                for(int i=0; i<size.length();i++){
                    char c=size.charAt(i);
                    if (!Character.isDigit(c)) {
                        event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eTry back"));
                        return;
                    }
                }
                if(Integer.parseInt(size)>6){
                    event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eThe maximum size is 6"));
                    return;
                }
                config.set("items."+EditCategoryListener.Category.get(event.getPlayer())+".menu.size", Integer.parseInt(size));
                event.getPlayer().sendMessage(ColorText.translate("&aThe size has been changed to &6"+size));
            }else if(EditCategoryListener.EditCategory.get(event.getPlayer()).equalsIgnoreCase("displayname")){
                config.set("items."+EditCategoryListener.Category.get(event.getPlayer())+".item.displayname", event.getMessage());
                event.getPlayer().sendMessage(ColorText.translate("&aThe displayname has been changed to &6"+event.getMessage()));
            }
            try {
                config.save(getConfigFile());
            }catch (IOException e){
                e.printStackTrace();
            }
            EditCategoryListener.EditCategory.remove(event.getPlayer());
        }

    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(EditListener.EditMainmenu.containsKey(event.getPlayer())){
            event.setCancelled(true);
            FileConfiguration config= Punish.getInstance().getConfig();
            if (event.getMessage() == null || event.getMessage().isEmpty()) {
                event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eTry back"));
                return;
            }
            if(event.getMessage().equalsIgnoreCase("exit")){
                event.getPlayer().sendMessage(ColorText.translate("&eEdit mode has been canceled"));
                EditListener.EditMainmenu.remove(event.getPlayer());
                EditCategoryListener.EditCategory.remove(event.getPlayer());
                return;
            }
            if(EditListener.EditMainmenu.get(event.getPlayer()).equalsIgnoreCase("title")){
                config.set("menu.principal.title", event.getMessage());
                event.getPlayer().sendMessage(ColorText.translate("&aThe title of the main menu has been changed to &f"+event.getMessage()));
            }else{
                String size=event.getMessage();
                for(int i=0; i<size.length();i++){
                    char c=size.charAt(i);
                    if (!Character.isDigit(c)) {
                        event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eTry back"));
                        return;
                    }
                }
                if(Integer.parseInt(size)>6){
                    event.getPlayer().sendMessage(ColorText.translate("&4&lError! &eThe maximum size is 6"));
                    return;
                }
                config.set("menu.principal.size", Integer.parseInt(size));
                event.getPlayer().sendMessage(ColorText.translate("&aThe size has been changed to &6"+size));
            }
            try {
                config.save(getConfigFile());
            }catch (IOException e){
                e.printStackTrace();
            }
            EditListener.EditMainmenu.remove(event.getPlayer());
        }
    }
    public File getConfigFile() {
        return new File(Punish.getInstance().getDataFolder(), "config.yml");
    }
    public File getDataFile() {
        return new File(Punish.getInstance().getDataFolder(), "data.yml");
    }
}

