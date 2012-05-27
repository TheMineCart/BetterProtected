package tmc.BetterProtected.services;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BukkitTestUtilities.Services.RepositoryTest;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static tmc.BetterProtected.domain.builder.BlockEventBuilder.aPlacedBlock;

public class BlockEventRepositoryTest extends RepositoryTest {
    
    @Before
    public void setUp() throws Exception {
        BlockEventRepository.initialize(getCollection("BlockEvents")) ;
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void shouldSavePlacedBlock() {
        BlockEvent blockEvent = aPlacedBlock().build();

        BlockEventRepository.save(blockEvent);

        List<BlockEvent> blockEvents = BlockEventRepository.findByBlockCoordinate(blockEvent.getBlockCoordinate(), blockEvent.getWorld());

        assertThat(blockEvents.get(0), is(blockEvent));
    }

    @Test
    public void shouldFindBlocksByChunkCoordinate() {
        DateTime now = new DateTime();
        BlockEvent jasonBlockEvent1 = aPlacedBlock().withInstant(now.plusDays(1)).withOwner("Jason").withChunkCoordinate(new ChunkCoordinate(1L, 2L)).build();
        BlockEvent jasonBlockEvent2 = aPlacedBlock().withInstant(now.plusDays(3)).withOwner("Jason").withChunkCoordinate(new ChunkCoordinate(1L, 2L)).build();
        BlockEvent jasonBlockEvent3 = aPlacedBlock().withInstant(now.plusDays(2)).withOwner("Jason").withChunkCoordinate(new ChunkCoordinate(1L, 2L)).build();
        BlockEvent jasonBlockEvent4 = aPlacedBlock().withOwner("Jason").withChunkCoordinate(new ChunkCoordinate(1L, 2L)).withWorld("DifferentWorld").build();
        BlockEvent charlieBlockEvent = aPlacedBlock().withOwner("Charlie").withChunkCoordinate(new ChunkCoordinate(2L, 3L)).build();
        BlockEvent katieBlockEvent = aPlacedBlock().withOwner("Katie").withChunkCoordinate(new ChunkCoordinate(3L, 4L)).build();

        BlockEventRepository.save(jasonBlockEvent1);
        BlockEventRepository.save(jasonBlockEvent2);
        BlockEventRepository.save(jasonBlockEvent3);
        BlockEventRepository.save(jasonBlockEvent4);
        BlockEventRepository.save(charlieBlockEvent);
        BlockEventRepository.save(katieBlockEvent);

        BlockEvent mostRecentBlockEvent = BlockEventRepository.findMostRecent(jasonBlockEvent1.getBlockCoordinate(), jasonBlockEvent1.getWorld());
        
        assertThat(mostRecentBlockEvent, is(jasonBlockEvent2));

        List<BlockEvent> blockEvents = BlockEventRepository.findByChunkCoordinate(jasonBlockEvent1.getChunkCoordinate(), jasonBlockEvent1.getWorld());

        assertThat(blockEvents.size(), is(3));
        assertThat(blockEvents, hasItems(jasonBlockEvent1, jasonBlockEvent2, jasonBlockEvent3));

        blockEvents = BlockEventRepository.findByChunkCoordinate(jasonBlockEvent4.getChunkCoordinate(), jasonBlockEvent4.getWorld());
        assertThat(blockEvents.size(), is(1));
        assertThat(blockEvents, hasItem(jasonBlockEvent4));

        blockEvents = BlockEventRepository.findByChunkCoordinate(charlieBlockEvent.getChunkCoordinate(), charlieBlockEvent.getWorld());
        
        assertThat(blockEvents.size(), is(1));
        assertThat(blockEvents, hasItem(charlieBlockEvent));

        blockEvents = BlockEventRepository.findByChunkCoordinate(katieBlockEvent.getChunkCoordinate(), katieBlockEvent.getWorld());

        assertThat(blockEvents.size(), is(1));
        assertThat(blockEvents, hasItem(katieBlockEvent));
    }
}
