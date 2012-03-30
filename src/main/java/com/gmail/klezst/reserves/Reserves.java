/*
	Reserves
	Copyright (C) 2011 Klezst

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.gmail.klezst.reserves;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Enables the reservation of player slots, in case the server is at max players.
 * 
 * @author Klezst
 * @since 1.0.0
 */
public class Reserves extends JavaPlugin {

    public static Permission permissionHandler = null;
    
    public final Logger log = Logger.getLogger("Minecraft.Reserves");
    private final PlayerListener listener = new PlayerListener();
    private String tag = "";
    
    public Reserves() {
    	this("[Reserves]");
    }
    
    private Reserves(String tag) {
    	this.tag = new StringBuilder(tag).append(" ").toString();
    }
    
    private void log(Level level, String message) {
    	log.log(level, new StringBuilder(tag).append(message).toString());
    }
   
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
    	
    	extractLicense();
    	
    	Plugin vaultCandidate = this.getServer().getPluginManager().getPlugin("Vault");
    	if(vaultCandidate == null || !(vaultCandidate instanceof Vault)) {
		    log(Level.SEVERE, "Vault is not installed.");
		    this.getServer().getPluginManager().disablePlugin(this);
    	}
    	
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissionHandler = rsp.getProvider();
    	if(permissionHandler == null) {
		    log(Level.SEVERE, "Could not find a permissions system.");
		    this.getServer().getPluginManager().disablePlugin(this);
    	}
		
		this.getServer().getPluginManager().registerEvents(listener, this);
		
		// Add any already logged in players to avoid kick invulnerability.
		listener.playerJoinOrder.clear();
		Player[] players = this.getServer().getOnlinePlayers();
		for(Player player : players)
		    listener.playerJoinOrder.add(player);
		
		log(Level.INFO, "Enabled.");
    }

	private void extractLicense() {
		File destF = new File(this.getDataFolder(), "LICENSE.txt");
		if(!destF.exists() || !destF.isFile()) {
			try {
				URL srcURL = Reserves.class.getResource("LICENSE.txt");
				InputStream src = srcURL.openStream();
				OutputStream dest = new FileOutputStream(destF);
				
				byte[] buffer = new byte[1024];
				int read = 0;
				while((read = src.read(buffer)) > 0)
					dest.write(buffer, 0, read);
				
				dest.flush();
				src.close();
				dest.close();
				
			} catch (IOException e) {
			    log(Level.SEVERE, "Error extracting resources.");
			    e.printStackTrace();
			}
		}
	}
}
