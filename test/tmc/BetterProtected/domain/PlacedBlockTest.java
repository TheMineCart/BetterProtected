package tmc.BetterProtected.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import org.junit.Test;
import tmc.BetterProtected.adaptors.DateTimeAdaptor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PlacedBlockTest {
    @Test
    public void canSerializeAndDeserialize() {
        DateTime placedOn = new DateTime();
        PlacedBlock placedBlock = new PlacedBlock(placedOn, new Player("Jason"), new BlockCoordinate(1L, 2L, 3L), new ChunkCoordinate(1L, 2L), "Narnia", Material.DIRT);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeAdaptor());
        Gson gson = gsonBuilder.create();

        String blockJson = gson.toJson(placedBlock, PlacedBlock.class);
        PlacedBlock deserializedBlock = gson.fromJson(blockJson, PlacedBlock.class);

        assertThat(deserializedBlock.getPlacedOn(), is(placedOn));
        assertThat(deserializedBlock.getPlacedBy(), is(new Player("Jason")));
        assertThat(deserializedBlock.getBlockCoordinate(), is(new BlockCoordinate(1L, 2L, 3L)));
        assertThat(deserializedBlock.getChunkCoordinate(), is(new ChunkCoordinate(1L, 2L)));
        assertThat(deserializedBlock.getMaterial(), is(Material.DIRT));
    }
}
