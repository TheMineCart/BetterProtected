package tmc.BetterProtected.utils;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

public class HibernateUtilsTest {
    @Test
    public void somethingHibernaty() {
        SessionFactory sessionFactory = HibernateUtils.configureSessionFactory();

//        Session session = sessionFactory.op

//        session.get
    }

//    Alternatively, you can have the SessionFactory open connections for you. The SessionFactory must be provided with
//    JDBC connection properties in one of the following ways:
//    Pass an instance of java.util.Properties to Configuration.setProperties().
//    Place hibernate.properties in a root directory of the classpath.
//    Set System properties using java -Dproperty=value.
//    Include <property> elements in hibernate.cfg.xml (discussed later).
//    If you take this approach, opening a Session is as simple as:
//    Session session = sessions.openSession(); // open a new Session
//    // do some data access work, a JDBC connection will be used on demand
//    All Hibernate property names and semantics are defined on the class net.sf.hibernate.cfg.Environment.
//    We will now describe the most important settings for JDBC connection configuration.
}
