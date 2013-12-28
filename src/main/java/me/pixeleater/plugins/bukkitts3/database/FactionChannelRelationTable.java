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

package me.pixeleater.plugins.bukkitts3.database;

import com.avaje.ebean.Query;
import me.pixeleater.plugins.bukkitts3.BukkitTS3;

/**
 *
 * @author Brad Reardon <brad.jay.reardon@gmail.com>
 */
public class FactionChannelRelationTable {
    
    private final BukkitTS3 plugin;
    
    public FactionChannelRelationTable(BukkitTS3 plugin) {
        this.plugin = plugin;
    }
    
    public String getFaction(int channelId) {
        String f = null;
        Query<FactionChannelRelation> query = plugin.getDatabase().find(FactionChannelRelation.class).where().eq("channelId", channelId).query();
        if (query != null)
            f = query.findUnique().getFactionId();
        return f;
    }
    
    public int getChannel(String factionId) {
        int c;
        Query<FactionChannelRelation> query = plugin.getDatabase().find(FactionChannelRelation.class).where().eq("factionId", factionId).query();
        if (query != null)
            c = query.findUnique().getChannelId();
        else
            c = -1;
        return c;
    }
    
    public void save(FactionChannelRelation factionChannelRelation) {
        plugin.getDatabase().save(factionChannelRelation);
    }
    
}
