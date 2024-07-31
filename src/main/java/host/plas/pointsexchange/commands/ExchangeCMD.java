package host.plas.pointsexchange.commands;

import host.plas.bou.commands.CommandContext;
import host.plas.bou.commands.SimplifiedCommand;
import host.plas.pointsexchange.PointsExchange;
import host.plas.pointsexchange.managers.ExchangeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

public class ExchangeCMD extends SimplifiedCommand {
    public ExchangeCMD() {
        super("exchange", PointsExchange.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        Player target;

        if (! commandContext.isArgUsable(1)) {
            if (commandContext.isPlayer()) {
                Optional<Player> playerOptional = commandContext.getSender().getPlayer();
                if (playerOptional.isPresent()) {
                    target = playerOptional.get();
                } else {
                    commandContext.sendMessage("&cYou must specify a player to exchange points with.");
                    return false;
                }
            } else {
                commandContext.sendMessage("&cYou must specify a player to exchange points with.");
                return false;
            }
        } else {
            if (! commandContext.isArgUsable(0)) {
                commandContext.sendMessage("&cYou must specify an exchange identifier!");
                return false;
            }

            String playerName = commandContext.getStringArg(1);

            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                commandContext.sendMessage("&cThe player " + playerName + " is not online.");
                return false;
            }

            target = player;
        }

        String exchangeIdentifier = commandContext.getStringArg(0);

        Optional<CommandSender> senderOptional = commandContext.getSender().getCommandSender();
        if (senderOptional.isEmpty()) {
            commandContext.sendMessage("&cCould not find your sender...");
            return false;
        }
        CommandSender sender = senderOptional.get();

        ExchangeManager.fireExchange(sender, target, exchangeIdentifier);
        return true;
    }

    @Override
    public ConcurrentSkipListSet<String> tabComplete(CommandContext commandContext) {
        ConcurrentSkipListSet<String> completions = new ConcurrentSkipListSet<>();

        if (commandContext.getArgs().size() <= 1) {
            completions.addAll(ExchangeManager.getExchangeIdentifiers());
        }
        if (commandContext.getArgs().size() == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
        }

        return completions;
    }
}
