package com.landonvolkmann.deathswap;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collections;

public final class DeathSwap extends JavaPlugin {

    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Location> swap_locations = new ArrayList<>();
    private Integer ROUND_LENGTH = 300;
    private Integer seconds_left = ROUND_LENGTH;
    private int gameID;

    public void removeDeadPlayers(ArrayList<Player> players) {
        players.removeIf(Entity::isDead);
    }

    public void setSwapLocations (ArrayList<Player> players, ArrayList<Location> swap_locations) {

        removeDeadPlayers(players);

        for (int i = 0; i < players.size(); i++) {
            Location player_location = players.get(i).getLocation();
            swap_locations.add(i, player_location);
        }

        ArrayList<Location> original_locations = new ArrayList<>(swap_locations);
        for(int i=0; i < swap_locations.size(); i++) {
            if (swap_locations.get(i).equals(original_locations.get(i))){
                Collections.shuffle(swap_locations);
                i = 0;
            }
        }
    }

    public void teleportPlayers (ArrayList<Player> players, ArrayList<Location> swap_locations){
        for (int i = 0; i < players.size(); i++) {
            players.get(i).teleport(swap_locations.get(i));
        }
    }

    public void addAllPlayersToGame (ArrayList<Player> players) {
        players.addAll(Bukkit.getServer().getOnlinePlayers());
        System.out.println("Number of players added to game: " + players.size());
    }

    public void endSwapGame() {
        Bukkit.getScheduler().cancelTask(this.gameID);
        seconds_left = ROUND_LENGTH;
        players = new ArrayList<>();
        swap_locations = new ArrayList<>();
    }


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("startSwapGame")) {

            addAllPlayersToGame(players);

            if (players.size() < 2) {
                sender.sendMessage("Too few active players to play Death Swap. Get some more friends.");
                players = new ArrayList<>();
                return true;
            } else {
                Bukkit.broadcastMessage("Death Swap has started!");
            }

            BukkitScheduler scheduler = getServer().getScheduler();

            this.gameID = scheduler.scheduleSyncRepeatingTask(this, () -> {

                removeDeadPlayers(players);
                switch (players.size()){
                    case 1:
                        Player winner = players.get(0);
                        Bukkit.broadcastMessage(winner.getDisplayName() + " is our champion!");
                        endSwapGame();
                        break;
                    case 0:
                        Bukkit.broadcastMessage("It's a tie!");
                        endSwapGame();
                        break;
                    default:
                        break;
                }

                if (seconds_left <= 5 && seconds_left > 0) {
                    Bukkit.broadcastMessage(seconds_left.toString() + " seconds to SWAP!");
                }
                if (seconds_left == 0) {

                    setSwapLocations(players, swap_locations);
                    Bukkit.broadcastMessage("DEATH SWAP!");
                    teleportPlayers(players, swap_locations);
                    swap_locations.clear();
                    seconds_left = ROUND_LENGTH;

                } else {
                    --seconds_left;
                }
            }, 20, 20);

            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("endSwapGame")) {
            endSwapGame();
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
