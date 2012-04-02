package tmc.BetterProtected.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Material;
import org.joda.time.DateTime;
import org.junit.Test;
import tmc.BetterProtected.adaptors.DateTimeAdaptor;
import tmc.BetterProtected.domain.types.BlockEventType;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BlockEventTest {
    @Test
    public void canSerializeAndDeserialize() {
        DateTime placedOn = new DateTime();
        BlockEvent blockEvent = new BlockEvent(placedOn, new Owner("Jason"), BlockEventType.PLACED, new BlockCoordinate(1L, 2L, 3L), new ChunkCoordinate(1L, 2L), new World("narnia"), Material.DIRT);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class, new DateTimeAdaptor());
        Gson gson = gsonBuilder.create();

        String blockJson = gson.toJson(blockEvent, BlockEvent.class);
        BlockEvent deserializedBlockEvent = gson.fromJson(blockJson, BlockEvent.class);

        assertThat(deserializedBlockEvent.getInstant(), is(placedOn));
        assertThat(deserializedBlockEvent.getOwner(), is(new Owner("Jason")));
        assertThat(deserializedBlockEvent.getBlockCoordinate(), is(new BlockCoordinate(1L, 2L, 3L)));
        assertThat(deserializedBlockEvent.getChunkCoordinate(), is(new ChunkCoordinate(1L, 2L)));
        assertThat(deserializedBlockEvent.getMaterial(), is(Material.DIRT));
    }
}
