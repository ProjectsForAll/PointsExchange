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

public class ReloadExchangeCMD extends SimplifiedCommand {
    public ReloadExchangeCMD() {
        super("reloadexchange", PointsExchange.getInstance());
    }

    @Override
    public boolean command(CommandContext commandContext) {
        Optional<CommandSender> senderOptional = commandContext.getSender().getCommandSender();
        if (senderOptional.isEmpty()) {
            commandContext.sendMessage("&cCould not find your sender...");
            return false;
        }
        CommandSender sender = senderOptional.get();

        commandContext.sendMessage("&cClearing &f" + ExchangeManager.getExchanges().size() + " &eexchanges&8...");
        ExchangeManager.reloadExchanges();

        commandContext.sendMessage("&aLoaded &f" + ExchangeManager.getExchanges().size() + " &eexchanges&8!");
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
