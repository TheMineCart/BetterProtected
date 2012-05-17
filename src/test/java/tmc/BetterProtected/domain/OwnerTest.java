package tmc.BetterProtected.domain;

import com.google.gson.Gson;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OwnerTest {
    @Test
    public void canDeserializeOwner() {
        String playerString = "{\"username\":\"Jason\"}";

        Gson gson = new Gson();
        Owner owner = gson.fromJson(playerString, Owner.class);

        assertThat(owner.getUsername(), is("Jason"));
    }

    @Test
    public void canSerializeOwner() {
        String expectedJson = "{\"username\":\"Jason\"}";
        Owner jason = new Owner("Jason");

        assertThat(new Gson().toJson(jason, Owner.class), is(expectedJson));
    }
}
