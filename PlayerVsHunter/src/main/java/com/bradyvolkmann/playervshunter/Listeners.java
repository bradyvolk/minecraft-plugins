package com.bradyvolkmann.playervshunter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public final class Listeners implements Listener {

    private ArrayList<Player> hunters;
    private Player runner;
    private Location prevLoc;

    public Listeners(ArrayList<Player> hunters, Player runner) {
        this.hunters= hunters;
        this.runner= runner;
    }

    /** Sets the hunters's compasses to point towards runner and prints runners Y location
     * to chat. Also gives hunter a compass if they don't have one. */
    @EventHandler
    public void trackPlayer(PlayerInteractEvent event) {
        if (hunters.contains(event.getPlayer())) {
            Player hunter = event.getPlayer();
            Inventory hunterInv = hunter.getInventory();
            if (!hunterInv.contains(Material.COMPASS)) {
                hunterInv.addItem(new ItemStack(Material.COMPASS));
            } else if (event.getItem() != null && event.getItem().getType() == Material.COMPASS) {
                if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (runner.getWorld().getEnvironment() == (World.Environment.NETHER) || runner.getWorld().getEnvironment() == World.Environment.THE_END) {
                        Location runnerLoc = prevLoc;
                        runnerLoc.getY();
                        hunter.sendMessage("Runner's Y coordinate: " + runnerLoc.getY());
                        hunter.setCompassTarget(runnerLoc);
                    } else {
                        Location runnerLoc = runner.getLocation();
                        prevLoc = runnerLoc;
                        runnerLoc.getY();
                        hunter.sendMessage("Runner's Y coordinate: " + runnerLoc.getY());
                        hunter.setCompassTarget(runnerLoc);
                    }
                }
            }
        }

    }
}
