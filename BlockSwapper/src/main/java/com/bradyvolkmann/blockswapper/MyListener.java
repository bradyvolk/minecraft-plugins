package com.bradyvolkmann.blockswapper;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public final class MyListener implements Listener {

    private final Player player;
    private Material targetBlock;
    private boolean targetBlockFound;

    public MyListener(Player player, Material targetBlock) {
        this.player= player;
        setTargetBlock(targetBlock);
    }

    /** When player right clicks, checks if block standing on is the same type as target block. */
    @EventHandler
    public void checkBlock(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            targetBlockFound = targetBlock == (player.getLocation().clone().subtract(0,1,0).getBlock().getType());
            System.out.println(targetBlockFound);
            System.out.println((player.getLocation().clone().subtract(0,1,0).getBlock().getType()));
        }

    }


    public void setTargetBlock(Material targetBlock) {
        this.targetBlock= targetBlock;
        this.targetBlockFound= false;
        player.sendMessage("Your Next Block: " + targetBlock.toString());
    }

    public boolean getTargetBlockFound() {
        return targetBlockFound;
    }

    public Player getPlayer() {
        return player;
    }
}
