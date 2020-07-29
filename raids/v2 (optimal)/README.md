# Raids System
Hello, this is my custom raids for a 718 (unfortunately I won't provide the interfaces for it) but you can have the commands to play it.

# What does it have?
* Must have one team (with max five members);
* Different bosses with its own map and drops
* No time limit to kill the bosses

# How to create a raid encounter?

Create a java file in **impl/** folder and then implement **RaidEncounter**, for example:
```java
package com.rs.game.player.content.raids.impl;

import com.rs.game.item.Item;
import com.rs.game.player.content.raids.data.RaidBoss;
import com.rs.game.player.content.raids.data.RaidDrops;
import com.rs.game.player.content.raids.data.RaidMap;
import com.rs.game.player.content.raids.data.RaidTiles;
import com.rs.game.player.content.raids.RaidsEncounter;
import com.rs.utils.Utils;

public class AnarothCastle implements RaidsEncounter {
    @Override
    public int id() {
        return CHANGE HERE BY THE RAID ORDER ID;
    }

    @Override
    public String raidName() {
        return "CHANGE HERE WITH YOUR RAID NAME";
    }

    @Override
    public String raidDescription() {
        return "CHANGE HERE WITH YOUR RAID DESCRIPTION";
    }

    @Override
    public RaidBoss raidBoss() {
        return new RaidBoss(NPC ID, NPC LEVEL (if -1 will have the level based on group members amount), BOSS BASE HEALTH, "BOSS NAME");
    }

    @Override
    public RaidDrops raidDrops() {
        return new RaidDrops(
                //VERYRARE
                new Item[]{new Item(ITEM ID, QUANTITY)},
                //RARE
                new Item[]{new Item(ITEM ID, QUANTITY)},
                //UNCOMMON
                new Item[]{new Item(ITEM ID, QUANTITY)},
                //NORMAL
                new Item[]{new Item(ITEM ID, QUANTITY)});
    }

    @Override
    public RaidMap raidMap() {
        return new RaidMap(new int[]{3, 3}, new int[]{451, 416});
    }

    @Override
    public RaidTiles raidTiles() {
        return new RaidTiles(new int[]{0, 14},
                new int[]{3, 12}, new int[]{11, 14}, new int[]{3, 19}, new int[]{11, 14}, new int[]{11, 21});
    }
}

```

Commands for the usage:
Open Usage.txt to see

# Credits
My mom to always make me feel better when coding as a hobby
