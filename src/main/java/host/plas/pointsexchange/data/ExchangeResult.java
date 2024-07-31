package host.plas.pointsexchange.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;

@Getter @Setter
public class ExchangeResult {
    public enum Type {
        AFFORDABLE,
        NOT_AFFORDABLE,
        NO_PLAYER,
        NO_EXCHANGE,
        ;
    }

    private final Type type;
    private final Exchange exchange;
    private final OfflinePlayer player;

    public ExchangeResult(Type type, Exchange exchange, OfflinePlayer player) {
        this.type = type;
        this.exchange = exchange;
        this.player = player;
    }

    public static ExchangeResult asAffordable(Exchange exchange, OfflinePlayer player) {
        return new ExchangeResult(Type.AFFORDABLE, exchange, player);
    }

    public static ExchangeResult asNotAffordable(Exchange exchange, OfflinePlayer player) {
        return new ExchangeResult(Type.NOT_AFFORDABLE, exchange, player);
    }

    public static ExchangeResult asNoPlayer(Exchange exchange) {
        return new ExchangeResult(Type.NO_PLAYER, exchange, null);
    }

    public static ExchangeResult asNoExchange(OfflinePlayer player) {
        return new ExchangeResult(Type.NO_EXCHANGE, null, player);
    }
}
