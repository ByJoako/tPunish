package net.helydev.com.command;

import net.helydev.com.Punish;
import net.helydev.com.listener.MenuListener;
import net.helydev.com.utils.ColorText;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PunishCommand implements CommandExecutor {
    public static Map<Player, String>Target=new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ColorText.translate("&4&lError! &eUse in-game!"));
            return true;
        }
        Player player= (Player) sender;
        if(player.hasPermission("punish.command.punish")){
            if(args.length==1){
                player.openInventory(MenuListener.getPunishMenu());
                Target.put(player, args[0]);
            }else{
                player.sendMessage(ColorText.translate("&eUse /punish <player>"));
            }
        }else{
            player.sendMessage(ColorText.translate(Punish.getInstance().getConfig().getString("No_permission")));
        }
        return false;
    }
}
