package tmc.BetterProtected.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import org.junit.Test;
import tmc.BetterProtected.adaptors.DateTimeAdaptor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RemovedBlockTest {
    @Test
    public void canSerializeAndDeserialize() {
        DateTime removedOn = new DateTime();
        RemovedBlock removedBlock = new RemovedBlock(removedOn, new Player("Jason"), new BlockCoordinate(1L, 2L, 3L), new ChunkCoordinate(1L, 2L), "Narnia", Material.DIRT);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeAdaptor());
        Gson gson = gsonBuilder.create();

        String blockJson = gson.toJson(removedBlock, RemovedBlock.class);
        RemovedBlock deserializedBlock = gson.fromJson(blockJson, RemovedBlock.class);

        assertThat(deserializedBlock.getRemovedOn(), is(removedOn));
        assertThat(deserializedBlock.getRemovedBy(), is(new Player("Jason")));
        assertThat(deserializedBlock.getBlockCoordinate(), is(new BlockCoordinate(1L, 2L, 3L)));
        assertThat(deserializedBlock.getChunkCoordinate(), is(new ChunkCoordinate(1L, 2L)));
        assertThat(deserializedBlock.getMaterial(), is(Material.DIRT));
    }
}
