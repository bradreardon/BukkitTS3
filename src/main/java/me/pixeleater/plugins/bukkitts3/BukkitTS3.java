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
import me.pixeleater.plugins.bukkitts3.listeners.ChatListener;
import me.pixeleater.plugins.bukkitts3.listeners.PlayerListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTS3 extends JavaPlugin implements Listener {
    
    private static BukkitTS3 instance;
    private static TS3Api connTS3;
    
    @Override
    public void onDisable() {
        getLogger().info("Closing TS3 connection.");
        connTS3.quit();
        connTS3 = null;
        getLogger().info(this + " is now disabled.");
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        
        new ChatListener(this);
        new PlayerListener(this);
        
        getLogger().info("Establishing connection to TS3 Server.");
        
        try {
            if (instance.getConfig().getBoolean("debug", false))
                connTS3 = new TS3Query(instance.getConfig().getString("ts3.host", "localhost"), TS3Query.DEFAULT_PORT, FloodRate.DEFAULT).debug(Level.ALL).connect().getApi();
            else
                connTS3 = new TS3Query(instance.getConfig().getString("ts3.host", "localhost"), TS3Query.DEFAULT_PORT, FloodRate.DEFAULT).connect().getApi();
            
            getTS3Api().login(instance.getConfig().getString("ts3.sq_user", "serveradmin"), instance.getConfig().getString("ts3.sq_pass"));
            getTS3Api().selectVirtualServerByPort(instance.getConfig().getInt("ts3.vs_port", 9987));
            getTS3Api().setNickname(instance.getConfig().getString("ts3.nick", "BukkitTS3"));
        } catch(Exception e) {
            getLogger().severe(e.toString());
        } finally {
            getLogger().info("Connection established to TeamSpeak.");
        }
        
        // FIXME: What happens if the server disconnects? Handle it.
    }
    
    public static TS3Api getTS3Api() {
        return instance.connTS3;
    }
}

