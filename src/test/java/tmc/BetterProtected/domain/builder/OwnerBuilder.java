package tmc.BetterProtected.domain.builder;

import tmc.BetterProtected.domain.Owner;

public class OwnerBuilder {

    private String username = "Jason";

    public static OwnerBuilder aPlayer() {
        return new OwnerBuilder();
    }

    public Owner build() {
        return new Owner(username);
    }

    public OwnerBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
}
