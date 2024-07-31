package host.plas.pointsexchange.events;

import host.plas.pointsexchange.PointsExchange;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class MainListener implements Listener {
    public MainListener() {
        Bukkit.getPluginManager().registerEvents(this, PointsExchange.getInstance());

        PointsExchange.getInstance().logInfo("Registered MainListener!");
    }
}
