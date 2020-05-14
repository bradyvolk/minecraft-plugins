package com.bradyvolkmann.playervshunter;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerVsHunter extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup login
        class Listeners implements Listener {
            @EventHandler
            public void creatureSpawn(CreatureSpawnEvent event) {
                if (event.getEntityType() == EntityType.CREEPER) {
                    Creeper creeper = (Creeper) event.getEntity();
                    creeper.setPowered(true);
                }
            }
        }

        // Creating a Listener
        getServer().getPluginManager().registerEvents(new Listeners(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
