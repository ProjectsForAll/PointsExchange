package host.plas.pointsexchange.config;

import host.plas.pointsexchange.PointsExchange;
import host.plas.pointsexchange.data.Exchange;
import tv.quaint.storage.resources.flat.simple.SimpleConfiguration;

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
            String placeholder = getResource().getString("exchanges." + key + ".placeholder");
            double cost = getResource().getDouble("exchanges." + key + ".cost");
            List<String> onSuccess = getResource().getStringList("exchanges." + key + ".on-success");
            List<String> onFailure = getResource().getStringList("exchanges." + key + ".on-failure");

            exchanges.add(new Exchange(key, placeholder, cost, onSuccess, onFailure));
        }

        return exchanges;
    }
}
