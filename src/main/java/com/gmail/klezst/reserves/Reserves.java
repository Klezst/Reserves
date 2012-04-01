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

import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import bukkitutil.BukkitUtilJavaPlugin;
import bukkitutil.util.IO;

/**
 * Enables the reservation of player slots, in case the server is at max players.
 * 
 * @author Klezst
 * @since 1.0.0
 */
public class Reserves extends BukkitUtilJavaPlugin {
	private final PlayerListener listener = new PlayerListener();
	
	public Reserves() {
		super("[Reserves]");
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
		
		try {
			IO.extract(this, "LICENSE.txt");
		} catch (IOException e) {
			log(Level.SEVERE, "Error extracting resources.");
			e.printStackTrace();
		}
		
		this.getServer().getPluginManager().registerEvents(listener, this);
		
		// Add any already logged in players to avoid kick invulnerability.
		listener.playerJoinOrder.clear();
		Player[] players = this.getServer().getOnlinePlayers();
		for(Player player : players)
			listener.playerJoinOrder.add(player);
		
		log(Level.INFO, "Enabled.");
	}
}
