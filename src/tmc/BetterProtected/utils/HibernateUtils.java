package tmc.BetterProtected.utils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import tmc.BetterProtected.domain.Player;

import java.io.File;

public class HibernateUtils {

    public static SessionFactory configureSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        File file = new File("conf/hibernate.cfg.xml");
        configuration.configure(file);
        configuration.addClass(Player.class);
        configuration.configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
