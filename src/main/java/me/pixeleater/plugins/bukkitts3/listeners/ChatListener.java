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

import me.pixeleater.plugins.bukkitts3.BukkitTS3;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

/**
 *
 * @author Brad Reardon <brad.jay.reardon@gmail.com>
 */
public class ChatListener implements Listener {
    
    private final BukkitTS3 plugin;
    
    public ChatListener(BukkitTS3 plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(PlayerChatEvent event) {
        if(plugin.getConfig().getBoolean("messages.chat"))
            plugin.getTS3Api().sendChannelMessage("<" + event.getPlayer().getName() + "> " + event.getMessage());
    }
}
