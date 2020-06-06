package com.bradyvolkmann.mctogether;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;

public final class Listeners implements Listener {

    private ArrayList<Player> players;
    private HashMap locations = new HashMap();
    private Location centroid;

    /** Initialize the players arraylist, locations hashmap, and centroid of players locations*/
    public Listeners(ArrayList<Player> players) {
        this.players= players;
        for (Player player : players) {
            locations.put(player, player.getLocation());
        }

        updateCentroid();
    }


    /** Changes value of centroid of the box which player's cannot move outisde of.
     * Centroid location calculated from the mean of the player's current location*/
    @EventHandler
    public void playerHasMoved(PlayerMoveEvent event) {
        // Update location
        for (Player player : players) {
            locations.put(player, player.getLocation());
        }
        updateCentroid();


    }

    /** Set cetroid */
    private void updateCentroid(){
        double[] centroidCoords = calcMeanLocation(this.players, this.locations);

        centroid.setX(centroidCoords[0]);
        centroid.setY(centroidCoords[1]);
        centroid.setZ(centroidCoords[2]);
    }

    /** Calculate mean of player locations and return it */
    private static double[] calcMeanLocation(ArrayList<Player> players, HashMap<Player, Location> locations) {
        double sumX, sumY, sumZ;
        sumX = sumY = sumZ = 0;

        for (Player player: players) {
            sumX += locations.get(player).getX();
            sumY += locations.get(player).getY();
            sumZ += locations.get(player).getZ();
        }
        double meanX = Math.round( sumX / players.size() );
        double meanY = Math.round( sumY / players.size() );
        double meanZ = Math.round( sumZ / players.size() );
        double[] arr = {meanX, meanY, meanZ};
        return arr;
    }


}