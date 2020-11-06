package systems.amit.spigot.amaxplayers;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class aMaxPlayers extends JavaPlugin implements Listener {

    public final String ADMIN_PERM = "amaxplayers.admin";
    public final String BYPASS_PERM = "amaxplayers.bypass";
    public int maxPlayers;
    public String noPerm;
    public String kickMsg;

    private static aMaxPlayers instance;
    public static aMaxPlayers getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!loadConfig()) {
            getLogger().severe("Could not load config, disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("amaxplayers").setExecutor(new Cmd_amaxplayers());
        Bukkit.getPluginManager().registerEvents(this, this);
        new Metrics(this, 7717);
        getLogger().info("The plugin has been enabled v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("The plugin has been disabled");
    }

    private boolean loadConfig() {
        saveDefaultConfig();

        if (!getConfig().contains("max-players")) {
            getLogger().severe("The config is missing the 'max-players' field");
            return false;
        }
        if (!getConfig().contains("no-perm")) {
            getLogger().severe("The config is missing the 'no-perm' field");
            return false;
        }
        if (!getConfig().contains("kick-msg")) {
            getLogger().severe("The config is missing the 'kick-msg' field");
            return false;
        }

        maxPlayers = getConfig().getInt("max-players");
        noPerm = ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-perm"));
        kickMsg = ChatColor.translateAlternateColorCodes('&', getConfig().getString("kick-msg"));
        return true;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e){
        if(Bukkit.getOnlinePlayers().size() > maxPlayers && !e.getPlayer().hasPermission(BYPASS_PERM)){
            e.disallow(PlayerLoginEvent.Result.KICK_FULL, kickMsg);
        }
    }

    @EventHandler
    public void onPing(ServerListPingEvent e){
        e.setMaxPlayers(maxPlayers);
    }
}
