package tmc.BetterProtected.svc;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import tmc.BetterProtected.domain.Player;

import java.util.List;

import static org.hibernate.criterion.Restrictions.eq;

public class PlayerRepository {

    private SessionFactory sessionFactory;
    private Session session;

    public PlayerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Player player) {
        // Non-managed environment idiom
        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.persist(player);
            tx.commit();
        }
        catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e; // or display error message
        }
        finally {
            session.close();
        }
    }

    public Player findById(Long id) {
        // Non-managed environment idiom
        Player player;
        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria criteria = session.createCriteria(Player.class);
            player = (Player)criteria.add(eq("id", id)).uniqueResult();
            tx.commit();
        }
        catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e; // or display error message
        }
        finally {
            session.close();
        }
        return player;
    }

    public List<Player> all() {
        // Non-managed environment idiom
        List<Player> players;
        session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            players = session.createCriteria(Player.class).list();
            tx.commit();
        }
        catch (RuntimeException e) {
            if (tx != null) tx.rollback();
            throw e; // or display error message
        }
        finally {
            session.close();
        }
        return players;
    }
}
