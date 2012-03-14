package com.gmail.klezst.reserves;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Enables the reservation of player slots, in case the server is at max players.
 * 
 * @author Klezst
 */
public class Reserves extends JavaPlugin {

    private static Logger logger = Logger.getLogger("Minecraft");
    protected static Permission permission = null;

    private PlayerListener listener = new PlayerListener();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
	    String commandLabel, String[] args) {
	return false;
    }

    @Override
    public void onDisable() {
	log(Level.INFO, "Disabled");
    }

    @Override
    public void onEnable() {
	setupPermissions();
	this.getServer().getPluginManager().registerEvents(listener, this);
	log(Level.INFO, "Enabled.");
    }

    /**
     * Logs a message.
     * 
     * @param level
     *            The importance of the message.
     * @param message
     *            The message to log.
     * 
     * @author Klezst
     */
    private void log(Level level, String message) {
	logger.log(level, "[Reserves]" + message);
    }

    /**
     * Loads the Vault permission management system.
     * 
     * @author Vault
     */
    private Boolean setupPermissions() {
	RegisteredServiceProvider<Permission> permissionProvider = getServer()
		.getServicesManager().getRegistration(
			net.milkbowl.vault.permission.Permission.class);
	if (permissionProvider != null) {
	    permission = permissionProvider.getProvider();
	}
	return (permission != null);
    }
}
