package com.bradyvolkmann.playervshunter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public final class PlayerVsHunter extends JavaPlugin {

    private ArrayList<Player> hunters;
    private Player runner;


    /** Add hunter to the array of hunters */
    public void setHunter(Player hunter) {
        hunters.add(hunter);
    }

    /** Set runner player to runner*/
    public void setRunner(Player runner) {
        this.runner= runner;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if (cmd.getName().equalsIgnoreCase("sethunter")) {
            Player target= (Bukkit.getServer().getPlayer(args[0]));
            if (target == null) {
                sender.sendMessage(args[0] + " is not online!");
                return false;
            } else {
                setHunter(target);
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("setrunner")) {
            Player target= (Bukkit.getServer().getPlayer(args[0]));
            if (target == null) {
                sender.sendMessage(args[0] + " is not online!");
                return false;
            } else {
                setRunner(target);
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("startgame")) {
            // Creating a Listener
            getServer().getPluginManager().registerEvents(new Listeners(hunters, runner), this);
            return true;
        }

        return false;
    }

    @Override
    public void onEnable() {
        // Plugin startup login








    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
