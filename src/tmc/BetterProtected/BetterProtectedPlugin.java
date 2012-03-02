package tmc.BetterProtected;

import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.Session;
import tmc.BetterProtected.utils.HibernateUtils;

public class BetterProtectedPlugin extends JavaPlugin {

    private Session session;

    @Override
    public void onEnable() {
        session = HibernateUtils.configureSessionFactory().openSession();
    }

    @Override
    public void onDisable() {
        session.close();
    }

    public Session getHibernateSession() {
        return session;
    }
}
