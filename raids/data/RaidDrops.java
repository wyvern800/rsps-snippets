package com.rs.game.minigames.raids.data;

import com.rs.game.item.Item;

public class RaidDrops {


    private Item[] veryRareDrops;
    private Item[] rareDrops;
    private Item[] uncommonDrops;

    public Item[] getVeryRareDrops() {
        return veryRareDrops;
    }

    public void setVeryRareDrops(Item[] veryRareDrops) {
        this.veryRareDrops = veryRareDrops;
    }

    public Item[] getRareDrops() {
        return rareDrops;
    }

    public void setRareDrops(Item[] rareDrops) {
        this.rareDrops = rareDrops;
    }

    public Item[] getUncommonDrops() {
        return uncommonDrops;
    }

    public void setUncommonDrops(Item[] uncommonDrops) {
        this.uncommonDrops = uncommonDrops;
    }

    public Item[] getCommonDrops() {
        return commonDrops;
    }

    public void setCommonDrops(Item[] commonDrops) {
        this.commonDrops = commonDrops;
    }

    public RaidDrops(Item[] veryRareDrops, Item[] rareDrops, Item[] uncommonDrops, Item[] commonDrops) {
        this.veryRareDrops = veryRareDrops;
        this.rareDrops = rareDrops;
        this.uncommonDrops = uncommonDrops;
        this.commonDrops = commonDrops;
    }

    private Item[] commonDrops;

}
