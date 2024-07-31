package host.plas.pointsexchange;

import host.plas.bou.BetterPlugin;
import host.plas.pointsexchange.commands.ExchangeCMD;
import host.plas.pointsexchange.commands.ReloadExchangeCMD;
import host.plas.pointsexchange.config.MainConfig;
import host.plas.pointsexchange.managers.ExchangeManager;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class PointsExchange extends BetterPlugin {
    @Getter @Setter
    private static PointsExchange instance;
    @Getter @Setter
    private static MainConfig mainConfig;

    @Getter @Setter
    private static ExchangeCMD exchangeCMD;
    @Getter @Setter
    private static ReloadExchangeCMD reloadExchangeCMD;

    public PointsExchange() {
        super();
    }

    @Override
    public void onBaseEnabled() {
        // Plugin startup logic
        setInstance(this);

        setMainConfig(new MainConfig());

        setExchangeCMD(new ExchangeCMD());
        setReloadExchangeCMD(new ReloadExchangeCMD());

        ExchangeManager.init();
    }

    @Override
    public void onBaseDisable() {
        // Plugin shutdown logic
        ExchangeManager.clearExchanges();
    }
}
