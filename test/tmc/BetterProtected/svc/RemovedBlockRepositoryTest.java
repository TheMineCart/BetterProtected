package tmc.BetterProtected.svc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.Player;
import tmc.BetterProtected.domain.RemovedBlock;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static tmc.BetterProtected.domain.builder.PlayerBuilder.aPlayer;
import static tmc.BetterProtected.domain.builder.RemovedBlockBuilder.aRemovedBlock;

public class RemovedBlockRepositoryTest extends RepositoryTest {
    private RemovedBlockRepository removedBlockRepository;

    @Before
    public void setUp() throws Exception {
        removedBlockRepository = new RemovedBlockRepository(getCollection("RemovedBlocks"));
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void shouldSaveRemovedBlock() {
        RemovedBlock removedBlock = aRemovedBlock().build();

        removedBlockRepository.save(removedBlock);

        List<RemovedBlock> blocks = removedBlockRepository.findByBlockCoordinate(removedBlock.getBlockCoordinate());

        assertThat(blocks.get(0), is(removedBlock));
    }

    @Test
    public void shouldFindBlocksByChunkCoordinate() {
        Player jason = aPlayer().build();
        Player charlie = aPlayer().withUsername("Charlie").build();
        Player katie = aPlayer().withUsername("Katie").build();
        RemovedBlock jasonBlock1 = aRemovedBlock().withRemovedBy(jason).withChunkCoordinate(new ChunkCoordinate(1L, 2L)).build();
        RemovedBlock jasonBlock2 = aRemovedBlock().withRemovedBy(jason).withChunkCoordinate(new ChunkCoordinate(1L, 2L)).build();
        RemovedBlock jasonBlock3 = aRemovedBlock().withRemovedBy(jason).withChunkCoordinate(new ChunkCoordinate(1L, 2L)).build();
        RemovedBlock charlieBlock = aRemovedBlock().withRemovedBy(charlie).withChunkCoordinate(new ChunkCoordinate(2L, 3L)).build();
        RemovedBlock katieBlock = aRemovedBlock().withRemovedBy(katie).withChunkCoordinate(new ChunkCoordinate(3L, 4L)).build();

        removedBlockRepository.save(jasonBlock1);
        removedBlockRepository.save(jasonBlock2);
        removedBlockRepository.save(jasonBlock3);
        removedBlockRepository.save(charlieBlock);
        removedBlockRepository.save(katieBlock);

        List<RemovedBlock> blocks = removedBlockRepository.findByChunkCoordinate(jasonBlock1.getChunkCoordinate());

        assertThat(blocks.size(), is(3));
        assertThat(blocks, hasItems(jasonBlock1, jasonBlock2, jasonBlock3));

        blocks = removedBlockRepository.findByChunkCoordinate(charlieBlock.getChunkCoordinate());

        assertThat(blocks.size(), is(1));
        assertThat(blocks, hasItem(charlieBlock));

        blocks = removedBlockRepository.findByChunkCoordinate(katieBlock.getChunkCoordinate());

        assertThat(blocks.size(), is(1));
        assertThat(blocks, hasItem(katieBlock));
    }
}
