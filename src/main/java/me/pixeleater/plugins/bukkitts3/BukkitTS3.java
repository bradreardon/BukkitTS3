/*
 * Copyright (C) 2013 Brad Reardon <brad.jay.reardon@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.pixeleater.plugins.bukkitts3;

/**
 *
 * @author Brad Reardon <brad.jay.reardon@gmail.com>
 */
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.TS3Query.FloodRate;
import java.util.logging.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitTS3 extends JavaPlugin implements Listener {
    
    private static BukkitTS3 instance;
    private static TS3Api connTS3;
    
    public void onDisable() {
        getLogger().info("Closing TS3 connection.");
        connTS3.quit();
        connTS3 = null;
        getLogger().info(this + " is now disabled.");
    }

    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("Establishing connection to TS3 Server.");
        // TODO: Establish central connection to TS3 server here, and handle disconnects accordingly.
        try {
            if (instance.getConfig().getBoolean("debug", false))
                connTS3 = new TS3Query(instance.getConfig().getString("ts3.host", "localhost"), TS3Query.DEFAULT_PORT, FloodRate.DEFAULT).debug(Level.ALL).connect().getApi();
            else
                connTS3 = new TS3Query(instance.getConfig().getString("ts3.host", "localhost"), TS3Query.DEFAULT_PORT, FloodRate.DEFAULT).connect().getApi();
            connTS3.login(instance.getConfig().getString("ts3.sq_user", "serveradmin"), instance.getConfig().getString("ts3.sq_pass"));
            connTS3.selectVirtualServerByPort(instance.getConfig().getInt("ts3.vs_port", 9987));
            connTS3.setNickname(instance.getConfig().getString("ts3.nick", "BukkitTS3"));
        } catch(Exception e) {
            getLogger().severe(e.toString());
        } finally {
            getLogger().info("Connection established to TeamSpeak.");
        }
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(instance.getConfig().getBoolean("messages.player_join"))
            connTS3.sendChannelMessage(event.getPlayer().getName() + " logged in.");
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(instance.getConfig().getBoolean("messages.player_quit"))
            connTS3.sendChannelMessage(event.getPlayer().getName() + " logged out.");
    }
    
    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if(instance.getConfig().getBoolean("messages.chat"))
            connTS3.sendChannelMessage("<" + event.getPlayer().getName() + "> " + event.getMessage());
    }
}

