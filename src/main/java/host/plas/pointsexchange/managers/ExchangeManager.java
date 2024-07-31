package host.plas.pointsexchange.managers;

import host.plas.bou.commands.Sender;
import host.plas.pointsexchange.PointsExchange;
import host.plas.pointsexchange.data.Exchange;
import host.plas.pointsexchange.data.ExchangeResult;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListSet;

public class ExchangeManager {
    @Getter @Setter
    private static ConcurrentSkipListSet<Exchange> exchanges = new ConcurrentSkipListSet<>();

    public static void registerExchange(Exchange exchange) {
        exchanges.add(exchange);
    }

    public static void unregisterExchange(String identifier) {
        exchanges.removeIf(exchange -> exchange.getIdentifier().equalsIgnoreCase(identifier));
    }

    public static Optional<Exchange> getExchange(String identifier) {
        return exchanges.stream().filter(exchange -> exchange.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
    }

    public static void addAll(Collection<Exchange> collection) {
        for (Exchange exchange : collection) {
            registerExchange(exchange);
        }
    }

    public static void init() {
        reloadExchanges();
    }

    public static void reloadExchanges() {
        clearExchanges();
        addAll(PointsExchange.getMainConfig().getExchanges());
    }

    public static Optional<Double> getPoints(OfflinePlayer player, String placeholder) {
        String converted = PlaceholderAPI.setPlaceholders(player, placeholder);
        try {
            return Optional.of(Double.parseDouble(converted));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Double> getPoints(OfflinePlayer player, Exchange exchange) {
        return getPoints(player, exchange.getPlaceholder());
    }

    public static ExchangeResult fireExchange(OfflinePlayer player, String identifier) {
        ExchangeResult result = getResult(player, identifier);
        Exchange exchange = result.getExchange();
        if (exchange == null) return result;

        if (result.getType() == ExchangeResult.Type.NOT_AFFORDABLE) {
            exchange.runFailure(player);
        }
        if (result.getType() == ExchangeResult.Type.AFFORDABLE) {
            exchange.runSuccess(player);
        }

        return result;
    }

    public static ExchangeResult getResult(OfflinePlayer player, String identifier) {
        Optional<Exchange> optionalExchange = getExchange(identifier);
        if (optionalExchange.isEmpty()) return ExchangeResult.asNoExchange(player);
        Exchange exchange = optionalExchange.get();

        if (player == null) return ExchangeResult.asNoPlayer(exchange);

        Optional<Double> optionalDouble = getPoints(player, exchange);
        if (optionalDouble.isEmpty()) return ExchangeResult.asNotAffordable(exchange, player);
        double currentPoints = optionalDouble.get();

        double toSubtract = currentPoints - exchange.getCost();
        if (toSubtract < 0) return ExchangeResult.asNotAffordable(exchange, player);

        return ExchangeResult.asAffordable(exchange, player);
    }

    public static void fireExchange(CommandSender sender, OfflinePlayer player, String identifier) {
        ExchangeResult result = fireExchange(player, identifier);
        Sender s = new Sender(sender);

        if (result.getType() == ExchangeResult.Type.AFFORDABLE || result.getType() == ExchangeResult.Type.NOT_AFFORDABLE) {
            s.sendMessage("&eFired exchange &7'&c" + identifier + "&7' &efor player &7'&d" + player.getName() + "&7'&8.");
        } else {
            s.sendMessage("&cFiring exchange failed... Returned: &f" + result.getType().name());
        }
    }

    public static ConcurrentSkipListSet<String> getExchangeIdentifiers() {
        return getExchanges().stream().map(Exchange::getIdentifier).collect(ConcurrentSkipListSet::new, ConcurrentSkipListSet::add, ConcurrentSkipListSet::addAll);
    }

    public static void clearExchanges() {
        getExchanges().clear();
    }
}
