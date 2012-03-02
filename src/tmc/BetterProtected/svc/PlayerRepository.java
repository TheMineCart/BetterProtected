package tmc.BetterProtected.svc;

import org.hibernate.Criteria;
import org.hibernate.Session;
import tmc.BetterProtected.domain.Player;

public class PlayerRepository {

    private Session session;

    public PlayerRepository(Session session) {
        this.session = session;
    }

    public void save(Player player) {
        session.persist(player);
    }

    public Player findById(Long id) {
        Criteria criteria = session.createCriteria(Player.class);
        System.out.println(criteria.list());
        return null;
    }
}
