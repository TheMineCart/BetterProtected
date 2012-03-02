package tmc.BetterProtected.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Player {

    @Column
    private Long id;

    @Column
    private String username;

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
}
