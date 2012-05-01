package tmc.BetterProtected.domain.builder;

import com.sun.istack.internal.Builder;
import tmc.BetterProtected.domain.Owner;

public class OwnerBuilder implements Builder<Owner> {

    private String username = "Jason";

    public static OwnerBuilder aPlayer() {
        return new OwnerBuilder();
    }

    @Override
    public Owner build() {
        return new Owner(username);
    }

    public OwnerBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
}
