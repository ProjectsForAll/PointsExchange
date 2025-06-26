package host.plas.pointsexchange.data;

import gg.drak.thebase.objects.Identifiable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Getter @Setter
public class Exchange implements Identifiable {
    private String identifier;
    private String placeholder;
    private double cost;
    private List<String> onSuccess;
    private List<String> onFailure;

    public Exchange(String identifier, String placeholder, double cost, List<String> onSuccess, List<String> onFailure) {
        this.identifier = identifier;
        this.placeholder = placeholder;
        this.cost = cost;
        this.onSuccess = onSuccess;
        this.onFailure = onFailure;
    }

    public void runSuccess(OfflinePlayer player) {
        for (String command : getOnSuccess()) {
            runCommand(command, player);
        }
    }

    public void runFailure(OfflinePlayer player) {
        for (String command : getOnFailure()) {
            runCommand(command, player);
        }
    }

    public void runCommand(String rawCommand, OfflinePlayer player) {
        String trueCommand = rawCommand;
        boolean asConsole = false;

        if (trueCommand.startsWith("!c")) {
            asConsole = true;
            trueCommand = trueCommand.substring(2);
        }

        while (trueCommand.startsWith(" ")) {
            trueCommand = trueCommand.substring(1);
        }

        if (trueCommand.startsWith("/")) {
            trueCommand = trueCommand.substring(1);
        }

        trueCommand = understandCommand(trueCommand, player, getCost());

        if (asConsole) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), trueCommand);
        } else {
            if (player.isOnline()) {
                Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
                if (onlinePlayer == null) return;
                Bukkit.dispatchCommand(onlinePlayer, trueCommand);
            }
        }
    }

    public static String understandCommand(String command, OfflinePlayer player, double cost) {
        if (player == null) return command;

        String name = player.getName();
        if (name == null) name = "NULL";

        return command
                .replace("%player_name%", name)
                .replace("%player_uuid%", player.getUniqueId().toString())
                .replace("%price%", String.valueOf(cost))
                ;
    }
}
