package net.helydev.com.command;

import net.helydev.com.listener.edit.EditListener;
import net.helydev.com.utils.ColorText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EditCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ColorText.translate("&4&lError! &eUse in-game!"));
            return true;
        }
        if(sender.hasPermission("punish.command.edit")){
            if(args.length==0){
                ((Player) sender).openInventory(EditListener.getEditMenu());
            }else{
                sender.sendMessage(ColorText.translate("&eUse /"+s));
            }
        }else{
            sender.sendMessage(ColorText.translate("&cNo permission!"));
        }
        return false;
    }
}
