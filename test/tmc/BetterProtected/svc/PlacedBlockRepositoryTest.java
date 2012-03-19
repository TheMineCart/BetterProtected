package tmc.BetterProtected.svc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.ChunkCoordinate;
import tmc.BetterProtected.domain.PlacedBlock;
import tmc.BetterProtected.domain.Player;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;
import static org.junit.matchers.JUnitMatchers.hasItems;
import static tmc.BetterProtected.domain.builder.PlacedBlockBuilder.aPlacedBlock;
import static tmc.BetterProtected.domain.builder.PlayerBuilder.aPlayer;

public class PlacedBlockRepositoryTest extends RepositoryTest{
    
    private PlacedBlockRepository placedBlockRepository;

    @Before
    public void setUp() throws Exception {
        placedBlockRepository = new PlacedBlockRepository(getCollection("PlacedBlocks"));
    }

    @After
    public void tearDown() throws Exception {
        clearTestData();
    }

    @Test
    public void shouldSavePlacedBlock() {
        PlacedBlock placedBlock = aPlacedBlock().build();

        placedBlockRepository.save(placedBlock);

        List<PlacedBlock> blocks = placedBlockRepository.findByBlockCoordinate(placedBlock.getBlockCoordinate());

        assertThat(blocks.get(0), is(placedBlock));
    }

    @Test
    public void shouldFindBlocksByChunkCoordinate() {
        Player jason = aPlayer().build();
        Player charlie = aPlayer().withUsername("Charlie").build();
        Player katie = aPlayer().withUsername("Katie").build();
        PlacedBlock jasonBlock1 = aPlacedBlock().withPlacedBy(jason).withChunkCoordinate(new ChunkCoordinate(1, 2)).build();
        PlacedBlock jasonBlock2 = aPlacedBlock().withPlacedBy(jason).withChunkCoordinate(new ChunkCoordinate(1, 2)).build();
        PlacedBlock jasonBlock3 = aPlacedBlock().withPlacedBy(jason).withChunkCoordinate(new ChunkCoordinate(1, 2)).build();
        PlacedBlock charlieBlock = aPlacedBlock().withPlacedBy(charlie).withChunkCoordinate(new ChunkCoordinate(2, 3)).build();
        PlacedBlock katieBlock = aPlacedBlock().withPlacedBy(katie).withChunkCoordinate(new ChunkCoordinate(3, 4)).build();

        placedBlockRepository.save(jasonBlock1);
        placedBlockRepository.save(jasonBlock2);
        placedBlockRepository.save(jasonBlock3);
        placedBlockRepository.save(charlieBlock);
        placedBlockRepository.save(katieBlock);

        List<PlacedBlock> blocks = placedBlockRepository.findByChunkCoordinate(jasonBlock1.getChunkCoordinate());

        assertThat(blocks.size(), is(3));
        assertThat(blocks, hasItems(jasonBlock1, jasonBlock2, jasonBlock3));

        blocks = placedBlockRepository.findByChunkCoordinate(charlieBlock.getChunkCoordinate());
        
        assertThat(blocks.size(), is(1));
        assertThat(blocks, hasItem(charlieBlock));

        blocks = placedBlockRepository.findByChunkCoordinate(katieBlock.getChunkCoordinate());

        assertThat(blocks.size(), is(1));
        assertThat(blocks, hasItem(katieBlock));
    }
}
