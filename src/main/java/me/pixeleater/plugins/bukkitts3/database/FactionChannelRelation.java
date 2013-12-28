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

import com.avaje.ebean.validation.NotNull;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents the relationship between a faction and a TeamSpeak channel.
 * Used for synchronization between factions and channels on a TeamSpeak server.
 * 
 * @author Brad Reardon <brad.jay.reardon@gmail.com>
 */
@Entity
@Table(name="faction_channel_relationships")
public class FactionChannelRelation {
    
    @Id
    private int id;
    
    @NotNull
    private int factionId;
    
    @NotNull
    private int channelId;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setFactionId(int id) {
        this.factionId = id;
    }
    
    public int getFactionId() {
        return this.factionId;
    }
    
    public void setChannelId(int id) {
        this.channelId = id;
    }
    
    public int getChannelId() {
        return this.channelId;
    }
}
