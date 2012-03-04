package tmc.BetterProtected.domain;

import javax.persistence.*;

@Entity
public class Player {

    @Id
    @SequenceGenerator(name = "player_sequence", sequenceName = "player_sequence")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "player_sequence")
    private Long id;

    @Column
    private String username;

    //Used by hibernate
    public Player() {}

    public Player(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (id != null ? !id.equals(player.id) : player.id != null) return false;
        if (username != null ? !username.equals(player.username) : player.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
