package tmc.BetterProtected.domain;

import com.google.gson.annotations.Expose;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class Player {

    @Expose private String username = "";
    @Expose private Set<String> friends = newHashSet();
    @Expose private Boolean protectionEnabled = true;

    public Player(String username) {
        this.username = username;
    }

    public Player(String username, Set<String> friends, Boolean protectionEnabled) {
        this.username = username;
        this.friends = friends;
        this.protectionEnabled = protectionEnabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public boolean addFriend(String username) {
        return friends.add(username);
    }

    public boolean removeFriend(String username) {
        return friends.remove(username);
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public Boolean getProtectionEnabled() {
        return protectionEnabled;
    }

    public void setProtectionEnabled(Boolean protectionEnabled) {
        this.protectionEnabled = protectionEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (friends != null ? !friends.equals(player.friends) : player.friends != null) return false;
        if (protectionEnabled != null ? !protectionEnabled.equals(player.protectionEnabled) : player.protectionEnabled != null)
            return false;
        if (username != null ? !username.equals(player.username) : player.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        result = 31 * result + (protectionEnabled != null ? protectionEnabled.hashCode() : 0);
        return result;
    }
}
