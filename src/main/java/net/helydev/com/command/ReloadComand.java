package net.helydev.com.command;

import net.helydev.com.Punish;
import net.helydev.com.utils.ColorText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadComand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender.hasPermission("punish.command.reload")){
            if(args.length==0){
                Punish.getInstance().getReload();
                sender.sendMessage(ColorText.translate("&aReloading completed successfully"));
            }else{
                sender.sendMessage(ColorText.translate("&euse /"+s));
            }
        }else{
            sender.sendMessage(ColorText.translate("&cNo permission!"));
        }
        return false;
    }
}
