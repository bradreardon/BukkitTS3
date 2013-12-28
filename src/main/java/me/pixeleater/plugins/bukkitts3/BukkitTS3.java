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
import java.util.ArrayList;
import java.util.logging.Level;
import javax.persistence.PersistenceException;
import me.pixeleater.plugins.bukkitts3.database.FactionChannelRelation;
import me.pixeleater.plugins.bukkitts3.database.FactionChannelRelationTable;
import me.pixeleater.plugins.bukkitts3.listeners.ChatListener;
import me.pixeleater.plugins.bukkitts3.listeners.FactionsListener;
import me.pixeleater.plugins.bukkitts3.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitTS3 extends JavaPlugin {
    
    private static BukkitTS3 instance;
    private static TS3Api connTS3;
    
    private static ChatListener chatListener;
    private static PlayerListener playerListener;
    private static FactionsListener factionsListener;
    
    FactionChannelRelationTable fcrTable;
    
    
    @Override
    public void onDisable() {
        getLogger().info("Closing TS3 connection.");
        connTS3.quit();
        connTS3 = null;
        getLogger().log(Level.INFO, "{0} is now disabled.", this);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        initDB();
        
        chatListener = new ChatListener(this);
        playerListener = new PlayerListener(this);
        
        fcrTable = new FactionChannelRelationTable(this);
        
        if (getServer().getPluginManager().isPluginEnabled("Factions") && instance.getConfig().getBoolean("plugins.factions")) {
            factionsListener = new FactionsListener(this);
            getLogger().info("Factions detected, integration enabled.");
        }
        
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
    
    private void initDB() {
        try {
            getDatabase().find(FactionChannelRelation.class).findRowCount();
        } catch(PersistenceException ex) {
            getLogger().log(Level.INFO, "Initializing database tables.");
            installDDL();
        }
    }
    
    public ArrayList<Class<?>> getDatabaseClasses() {
        ArrayList<Class<?>> dbClasses = new ArrayList();
        dbClasses.add(FactionChannelRelation.class);
        return dbClasses;
    }
    
    public FactionChannelRelationTable getFactionChannelRelationTable() {
        return fcrTable;
    }
    
    public TS3Api getTS3Api() {
        return connTS3;
    }
}

