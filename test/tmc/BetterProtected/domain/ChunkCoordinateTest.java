package tmc.BetterProtected.domain;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ChunkCoordinateTest {

    @Test
    public void shouldFindAllInRadiusOf2() {
        List<ChunkCoordinate> coordinates = ChunkCoordinate.findChunkCoordinatesInRadius(2, 1, 1);

        assertThat(coordinates.size(), is(13));
    }

    @Test
    public void shouldFindAllInRadiusOf10() {
        List<ChunkCoordinate> coordinates = ChunkCoordinate.findChunkCoordinatesInRadius(10, 1, 1);

        assertThat(coordinates.size(), is(317));
    }

    @Test
    public void coordShouldBeInsideRadius() {
        assertThat(ChunkCoordinate.coordinateInsideRadius(0, 0, 0, 0, 0), is(true));

        assertThat(ChunkCoordinate.coordinateInsideRadius(0, 0, 0, 0, 1), is(true));
        assertThat(ChunkCoordinate.coordinateInsideRadius(0, 1, 0, 0, 1), is(true));
        assertThat(ChunkCoordinate.coordinateInsideRadius(1, 0, 0, 0, 1), is(true));
        assertThat(ChunkCoordinate.coordinateInsideRadius(1, 1, 0, 0, 1), is(false));

        assertThat(ChunkCoordinate.coordinateInsideRadius(1, 1, 0, 0, 2), is(true));
        assertThat(ChunkCoordinate.coordinateInsideRadius(1, -1, 0, 0, 2), is(true));
        assertThat(ChunkCoordinate.coordinateInsideRadius(-1, 1, 0, 0, 2), is(true));
        assertThat(ChunkCoordinate.coordinateInsideRadius(-1, -1, 0, 0, 2), is(true));

        assertThat(ChunkCoordinate.coordinateInsideRadius(2, 1, 0, 0, 2), is(false));
        assertThat(ChunkCoordinate.coordinateInsideRadius(1, 2, 0, 0, 2), is(false));
        assertThat(ChunkCoordinate.coordinateInsideRadius(1, -2, 0, 0, 3), is(true));
    }
}
