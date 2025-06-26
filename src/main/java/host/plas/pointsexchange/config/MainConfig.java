package host.plas.pointsexchange.config;

import gg.drak.thebase.storage.resources.flat.simple.SimpleConfiguration;
import host.plas.pointsexchange.PointsExchange;
import host.plas.pointsexchange.data.Exchange;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

public class MainConfig extends SimpleConfiguration {
    public MainConfig() {
        super("config.yml", PointsExchange.getInstance(), false);
    }

    @Override
    public void init() {
        getExchanges();
    }

    public ConcurrentSkipListSet<Exchange> getExchanges() {
        ConcurrentSkipListSet<Exchange> exchanges = new ConcurrentSkipListSet<>();

        for (String key : singleLayerKeySet("exchanges")) {
            String placeholder = getOrSetDefault("exchanges." + key + ".placeholder", "");
            double cost = getOrSetDefault("exchanges." + key + ".cost", 0.0d);
            List<String> onSuccess = getOrSetDefault("exchanges." + key + ".on-success", new ArrayList<>());
            List<String> onFailure = getOrSetDefault("exchanges." + key + ".on-failure", new ArrayList<>());

            exchanges.add(new Exchange(key, placeholder, cost, onSuccess, onFailure));
        }

        return exchanges;
    }
}
