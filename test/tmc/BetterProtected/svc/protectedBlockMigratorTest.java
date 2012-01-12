package tmc.BetterProtected.svc;

import org.junit.Before;
import org.junit.Test;
import tmc.BetterProtected.domain.ProtectedBlock;

public class ProtectedBlockMigratorTest {

    private ProtectedBlockMigrator protectedBlockMigrator;

    @Before
    public void setUp() throws Exception {
        protectedBlockMigrator = new ProtectedBlockMigrator();
    }

    @Test
    public void shouldTransferDataFromFileToObject() {
        String file = "";
        ProtectedBlock actualBlock = protectedBlockMigrator.getDataFromFile(file);

        assert(false);
    }
}
