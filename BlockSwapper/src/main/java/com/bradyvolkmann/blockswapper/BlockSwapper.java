package com.bradyvolkmann.blockswapper;

import com.ibm.dtfj.corereaders.Register;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Handler;

public final class BlockSwapper extends JavaPlugin {

    private ArrayList<Player> players= new ArrayList<>();
    private final Integer ROUND_LENGTH= 30;
    private Integer seconds_left= ROUND_LENGTH;
    private int gameID;
    private HashMap<Player, Boolean> blockFound = new HashMap<>();
    private ArrayList<RegisteredListener> registeredListeners;



    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("startBlockSwapper")) {

            addAllPlayersToGame(players);


            if (players.size() < 2) {
                sender.sendMessage("Too few active players to play Block Swapper. Get some more friends.");
                players = new ArrayList<>();
                return true;
            } else {
                Bukkit.broadcastMessage("Block Swapper has started!");
            }


            // Register Listeners and generate randomBlock for each player
            for (Player player : players) {
                getServer().getPluginManager().registerEvents(new MyListener(player, generateRandomBlock()), this);
                blockFound.put(player, false);
            }

            registeredListeners=  HandlerList.getRegisteredListeners(this);


            BukkitScheduler scheduler = getServer().getScheduler();

            this.gameID = scheduler.scheduleSyncRepeatingTask(this, () -> {
                for (RegisteredListener listener: registeredListeners) {
                    MyListener myListener = (MyListener) listener.getListener();
                    if( myListener.getTargetBlockFound() ) {
                        if (!blockFound.get(myListener.getPlayer())) {
                            Bukkit.broadcastMessage(myListener.getPlayer().getDisplayName() + " has found their block!");
                            blockFound.put(myListener.getPlayer(), true);
                        }
                    }

                }

                boolean allFound = true;
                for (boolean val : blockFound.values()) {
                    allFound = allFound && val;
                }

                if (allFound) {
                    newRound();
                }

                if (seconds_left <= 10 && seconds_left > 0) {
                    Bukkit.broadcastMessage(seconds_left.toString() + " seconds left!");
                }
                if (seconds_left == 0) {
                    eliminatePlayers();

                    if (blockFound.keySet().size() == 1) {
                        for (Player player : blockFound.keySet()) {
                            Bukkit.broadcastMessage(player.getDisplayName() + " is our champion!");
                        }
                        endBlockSwapper();
                    } else if (blockFound.keySet().size() == 0) {
                        Bukkit.broadcastMessage("Nobody won?! You're all not good.");
                        endBlockSwapper();
                    }

                    newRound();
                } else {
                    --seconds_left;
                }
            }, 20, 20);

            return true;

        }
        else if (cmd.getName().equalsIgnoreCase("endBlockSwapper")) {
            endBlockSwapper();
        }

        return false;
    }


    public void addAllPlayersToGame (ArrayList<Player> players) {
        players.addAll(Bukkit.getServer().getOnlinePlayers());
        System.out.println("Number of players added to game: " + players.size());
    }

    public void endBlockSwapper() {
        Bukkit.getScheduler().cancelTask(this.gameID);
        Bukkit.broadcastMessage("Block Swapper has ended!");
        seconds_left = ROUND_LENGTH;
        HandlerList.unregisterAll();
        players = new ArrayList<>();
        blockFound = new HashMap<>();
    }

    /** Generate random block and return as a material */
    private Material generateRandomBlock() {
        Material material = null;
        Random random = new Random();
        while(material == null)
        {
            material = Material.values()[random.nextInt(Material.values().length)];
            if(!(material.isBlock()))
            {
                material = null;
            }
        }
        return material;
    }

    private void newRound() {
        seconds_left = ROUND_LENGTH;
        for (RegisteredListener listener : registeredListeners) {
            MyListener myListener = (MyListener) listener.getListener();
            myListener.setTargetBlock(generateRandomBlock());
        }
        for (Player player : players) {
            blockFound.put(player, false);
        }

    }

    private void eliminatePlayers() {
        for (RegisteredListener listener : registeredListeners) {
            MyListener myListener = (MyListener) listener.getListener();
            Player player = myListener.getPlayer();
            if (!myListener.getTargetBlockFound()) {
                HandlerList.unregisterAll(myListener);
                blockFound.remove(player);
                players.remove(player);
                Bukkit.broadcastMessage("Player " + player.getDisplayName() + " has been eliminated!");
            }

        }
        registeredListeners= HandlerList.getRegisteredListeners(this);
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
