package tmc.BetterProtected;

import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;
import tmc.BetterProtected.utils.HibernateUtils;

public class BetterProtectedPlugin extends JavaPlugin {

    private SessionFactory sessionFactory;

    @Override
    public void onEnable() {
        sessionFactory = HibernateUtils.configureSessionFactory();
    }

    @Override
    public void onDisable() {
        sessionFactory.close();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
