package systems.amit.spigot.amaxplayers;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Cmd_amaxplayers implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission(aMaxPlayers.getInstance().ADMIN_PERM)){
            sender.sendMessage(aMaxPlayers.getInstance().noPerm);
            return false;
        }

        if(args.length == 0){
            sender.sendMessage(ChatColor.GRAY + "Usage: /"+label + " <amount>");
            sender.sendMessage(ChatColor.GRAY + "Current limit: " + ChatColor.YELLOW + aMaxPlayers.getInstance().maxPlayers);
            return false;
        }

        int newMax;
        try {
            newMax = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            sender.sendMessage(ChatColor.RED + "The value must be a number");
            return false;
        }

        if(newMax < 0){
            sender.sendMessage(ChatColor.RED + "The value must be bigger than 0.");
            return false;
        }

        aMaxPlayers.getInstance().maxPlayers = newMax;
        aMaxPlayers.getInstance().getConfig().set("max-players", newMax);
        aMaxPlayers.getInstance().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "The value has been updated to " + newMax);
        return false;
    }
}
