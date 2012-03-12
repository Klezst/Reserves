package com.gmail.klezst.reserves;

import java.util.HashMap;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Reserves extends JavaPlugin {
    
    private static Permission permission = null;
    
    private Listener listener = new Listener()
    {
	private final String permissionNode = "reserves";
	
	private HashMap<Player, Long> joinTimes = new HashMap<Player, Long>();
	private Server server = Bukkit.getServer();
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerPreLogin(PlayerLoginEvent event) {
	    System.out.println("RAWRWARWASDFSADFAS");
	    // Record join time, to determine who should be kicked.
	    Player player = event.getPlayer();
	    joinTimes.put(player, player.getPlayerTime());
	    
	    Player[] players = server.getOnlinePlayers();
	    if (players.length == server.getMaxPlayers()){
		if (permission.has(player, permissionNode)){
		    
		    // Calculate the time each player has been connected to the server.
		    HashMap<Player, Long> timeConnected = new HashMap<Player, Long>();
		    for (Player connected : players)
		    {
			timeConnected.put(connected, connected.getPlayerTime() - joinTimes.get(connected));
		    }
		    
		    // Sort players based on timeConnected in descending order.
		    for (int i = 1; i < players.length; i++)
		    {
			int j = i;
			while (j > 0 && timeConnected.get(players[j]) > timeConnected.get(players[j - 1])) {
			    Player temp = players[j - 1];
			    players[j - 1] = players[j];
			    players[j] = temp;
			    
			    j--;
			}
		    }
		    
		    // Kick player who has been connected longest and does not have a slot reserved.
		    for (Player connected : players)
		    {
			if (!permission.has(connected, permissionNode)) {
			    connected.kickPlayer("A player with a reserved slot has connected. Sorry, the server is at maximum capacity.");
			    break;
			}
		    }
		}
	    }
	}
    };
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
	return false;
    }
    
    @Override
    public void onDisable()
    {
	
    }
    
    @Override
    public void onEnable()
    {
	setupPermissions();
	this.getServer().getPluginManager().registerEvents(listener, this);
    }
    
    /**
     * Loads Vault permission management system.
     * 
     * @author Vault
     */
    private Boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
