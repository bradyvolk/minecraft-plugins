package com.landonvolkmann.deathswap;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Timer;

public final class DeathSwap extends JavaPlugin {

    private ArrayList<Player> players = new ArrayList<>();
    private Integer ROUND_LENGTH = 300;
    private Integer seconds_left = ROUND_LENGTH;
    private int gameID;

    public void setPlayer(Player player) { this.players.add(player);}


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("setSwapPlayer")) {
            Player target= (Bukkit.getServer().getPlayer(args[0]));
            if (target == null) {
                sender.sendMessage(args[0] + " is not online!");
                return false;
            } else {
                setPlayer(target);
            }
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("startSwapGame")) {
//            // Creating a Listener
//            getServer().getPluginManager().registerEvents(new SwapListeners(players), this);
            BukkitScheduler scheduler = getServer().getScheduler();

            this.gameID = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {

                        if (seconds_left < 5 && seconds_left > 0) {
                            Bukkit.broadcastMessage(seconds_left.toString() + " seconds to SWAP!");
                            System.out.println(seconds_left);
                        }
                        if (seconds_left == 0) {

                            Bukkit.broadcastMessage("DEATH SWAP!");
                            System.out.println("DEATH SWAP!");
//                            players.get(0).teleport(new Location(players.get(0).getWorld(), 0, 150, 0));

                            Location p1_loc = players.get(0).getLocation();
                            Location p2_loc = players.get(1).getLocation();
                            players.get(0).teleport(p2_loc);
                            players.get(1).teleport(p1_loc);


                            seconds_left = ROUND_LENGTH;

                        } else {
                            --seconds_left;
                        }
                    }


            }, 20, 20);

            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("endSwapGame")) {
            Bukkit.getScheduler().cancelTask(this.gameID);
        }

        return false;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
