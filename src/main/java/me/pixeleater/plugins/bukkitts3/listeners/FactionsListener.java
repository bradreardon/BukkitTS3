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

package me.pixeleater.plugins.bukkitts3.listeners;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.massivecraft.factions.event.FactionsEventCreate;
import com.massivecraft.factions.event.FactionsEventDescriptionChange;
import com.massivecraft.factions.event.FactionsEventDisband;
import com.massivecraft.factions.event.FactionsEventNameChange;
import java.util.HashMap;
import me.pixeleater.plugins.bukkitts3.BukkitTS3;
import me.pixeleater.plugins.bukkitts3.database.FactionChannelRelation;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

// TODO: Maybe design custom exceptions in case the relationships weren't established right?

// TODO: Send initiating player messages pertaining to what happens here.

/**
 *
 * @author Brad Reardon <brad.jay.reardon@gmail.com>
 */
public class FactionsListener implements Listener {
    
    private final BukkitTS3 plugin;

    public FactionsListener(BukkitTS3 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onFactionCreation(FactionsEventCreate event) {
        HashMap<ChannelProperty, String> options = new HashMap();
        int pid = plugin.getTS3Api().getChannelByName(plugin.getConfig().getString("ts3.parent_channel")).getId();
        options.put(ChannelProperty.PID, Integer.toString(pid)); // FIXME: Find parent channel attribute, this obviously isn't working
        
        FactionChannelRelation fcr = new FactionChannelRelation();
        fcr.setFactionId(event.getFactionId());
        fcr.setChannelId(plugin.getTS3Api().createChannel(event.getFactionName(), options));
        plugin.getFactionChannelRelationTable().save(fcr);
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onFactionDisband(FactionsEventDisband event) {
        int channel = -1;
        try {
            channel = plugin.getFactionChannelRelationTable().getChannel(event.getFactionId());
        } catch (Exception e) {
             // No need to worry, the relationship was never established in the first place.
        } finally {
            plugin.getTS3Api().deleteChannel(channel);
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onFactionRename(FactionsEventNameChange event) {
        HashMap<ChannelProperty, String> options = new HashMap();
        options.put(ChannelProperty.CHANNEL_NAME, event.getNewName());
        int channel = -1;
        try {
            channel = plugin.getFactionChannelRelationTable().getChannel(event.getFaction().getId());
        } catch (Exception e) {
        } finally {
            plugin.getTS3Api().editChannel(channel, options);
        }
    }
    
    public void onFactionDescription(FactionsEventDescriptionChange event) {
        HashMap<ChannelProperty, String> options = new HashMap();
        options.put(ChannelProperty.CHANNEL_DESCRIPTION, event.getNewDescription());
        int channel = -1;
        try {
            channel = plugin.getFactionChannelRelationTable().getChannel(event.getFaction().getId());
        } catch (Exception e) {
        } finally {
            plugin.getTS3Api().editChannel(channel, options);
        }
    }
    
}
