package tmc.BetterProtected.domain;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;

@SuppressWarnings("deprecation")
public class TestWorld implements org.bukkit.World {

    List<Block> blocks = newArrayList();
    private String name;

    public TestWorld(String name) {
        this.name = name;
    }

    public void addBlock(Block block) {
        blocks.add(block);
    }

    @Override
    public Block getBlockAt(int x, int y, int z) {
        for (Block block : blocks) {
            if(block.getX() == x && block.getY() == y && block.getZ() == z){
                return block;
            }
        }
        return new TestBlock(x, y, z, Material.DIRT);
    }

    @Override
    public Block getBlockAt(Location location) {
        return null;
    }

    @Override
    public int getBlockTypeIdAt(int x, int y, int z) {
        return getBlockAt(x, y, z).getTypeId();
    }

    @Override
    public int getBlockTypeIdAt(Location location) {
        return 0;  
    }

    @Override
    public int getHighestBlockYAt(int i, int i1) {
        return 0;  
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return 0;  
    }

    @Override
    public Block getHighestBlockAt(int i, int i1) {
        return null;  
    }

    @Override
    public Block getHighestBlockAt(Location location) {
        return null;  
    }

    @Override
    public Chunk getChunkAt(int i, int i1) {
        return null;  
    }

    @Override
    public Chunk getChunkAt(Location location) {
        return null;  
    }

    @Override
    public Chunk getChunkAt(Block block) {
        return null;  
    }

    @Override
    public boolean isChunkLoaded(Chunk chunk) {
        return false;  
    }

    @Override
    public Chunk[] getLoadedChunks() {
        return new Chunk[0];  
    }

    @Override
    public void loadChunk(Chunk chunk) {
        
    }

    @Override
    public boolean isChunkLoaded(int i, int i1) {
        return false;  
    }

    @Override
    public void loadChunk(int i, int i1) {
        
    }

    @Override
    public boolean loadChunk(int i, int i1, boolean b) {
        return false;  
    }

    @Override
    public boolean unloadChunk(Chunk chunk) {
        return false;  
    }

    @Override
    public boolean unloadChunk(int i, int i1) {
        return false;  
    }

    @Override
    public boolean unloadChunk(int i, int i1, boolean b) {
        return false;  
    }

    @Override
    public boolean unloadChunk(int i, int i1, boolean b, boolean b1) {
        return false;  
    }

    @Override
    public boolean unloadChunkRequest(int i, int i1) {
        return false;  
    }

    @Override
    public boolean unloadChunkRequest(int i, int i1, boolean b) {
        return false;  
    }

    @Override
    public boolean regenerateChunk(int i, int i1) {
        return false;  
    }

    @Override
    public boolean refreshChunk(int i, int i1) {
        return false;  
    }

    @Override
    public Item dropItem(Location location, ItemStack itemStack) {
        return null;  
    }

    @Override
    public Item dropItemNaturally(Location location, ItemStack itemStack) {
        return null;  
    }

    @Override
    public Arrow spawnArrow(Location location, Vector vector, float v, float v1) {
        return null;  
    }

    @Override
    public boolean generateTree(Location location, TreeType treeType) {
        return false;  
    }

    @Override
    public boolean generateTree(Location location, TreeType treeType, BlockChangeDelegate blockChangeDelegate) {
        return false;  
    }

    @Override
    public LivingEntity spawnCreature(Location location, EntityType entityType) {
        return null;  
    }

    @Override
    public LivingEntity spawnCreature(Location location, CreatureType creatureType) {
        return null;  
    }

    @Override
    public LightningStrike strikeLightning(Location location) {
        return null;  
    }

    @Override
    public LightningStrike strikeLightningEffect(Location location) {
        return null;  
    }

    @Override
    public List<Entity> getEntities() {
        return null;  
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        return null;  
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes) {
        return null;  
    }

    @Override
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> tClass) {
        return null;  
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        return null;  
    }

    @Override
    public List<Player> getPlayers() {
        return null;  
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUID() {
        return null;  
    }

    @Override
    public Location getSpawnLocation() {
        return null;  
    }

    @Override
    public boolean setSpawnLocation(int i, int i1, int i2) {
        return false;  
    }

    @Override
    public long getTime() {
        return 0;  
    }

    @Override
    public void setTime(long l) {
        
    }

    @Override
    public long getFullTime() {
        return 0;  
    }

    @Override
    public void setFullTime(long l) {
        
    }

    @Override
    public boolean hasStorm() {
        return false;  
    }

    @Override
    public void setStorm(boolean b) {
        
    }

    @Override
    public int getWeatherDuration() {
        return 0;  
    }

    @Override
    public void setWeatherDuration(int i) {
        
    }

    @Override
    public boolean isThundering() {
        return false;  
    }

    @Override
    public void setThundering(boolean b) {
        
    }

    @Override
    public int getThunderDuration() {
        return 0;  
    }

    @Override
    public void setThunderDuration(int i) {
        
    }

    @Override
    public boolean createExplosion(double d, double v1, double v2, float f) {
        return false;  
    }

    @Override
    public boolean createExplosion(double d, double v1, double v2, float f, boolean b) {
        return false;  
    }

    @Override
    public boolean createExplosion(Location location, float v) {
        return false;  
    }

    @Override
    public boolean createExplosion(Location location, float v, boolean b) {
        return false;  
    }

    @Override
    public Environment getEnvironment() {
        return null;  
    }

    @Override
    public long getSeed() {
        return 0;  
    }

    @Override
    public boolean getPVP() {
        return false;  
    }

    @Override
    public void setPVP(boolean b) {
        
    }

    @Override
    public ChunkGenerator getGenerator() {
        return null;  
    }

    @Override
    public void save() {
        
    }

    @Override
    public List<BlockPopulator> getPopulators() {
        return null;  
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> tClass) throws IllegalArgumentException {
        return null;  
    }

    @Override
    public void playEffect(Location location, Effect effect, int i) {
        
    }

    @Override
    public void playEffect(Location location, Effect effect, int i, int i1) {
        
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T t) {
        
    }

    @Override
    public <T> void playEffect(Location location, Effect effect, T t, int i) {
        
    }

    @Override
    public ChunkSnapshot getEmptyChunkSnapshot(int i, int i1, boolean b, boolean b1) {
        return null;  
    }

    @Override
    public void setSpawnFlags(boolean b, boolean b1) {
        
    }

    @Override
    public boolean getAllowAnimals() {
        return false;  
    }

    @Override
    public boolean getAllowMonsters() {
        return false;  
    }

    @Override
    public Biome getBiome(int i, int i1) {
        return null;  
    }

    @Override
    public void setBiome(int i, int i1, Biome biome) {
        
    }

    @Override
    public double getTemperature(int i, int i1) {
        return 0;  
    }

    @Override
    public double getHumidity(int i, int i1) {
        return 0;  
    }

    @Override
    public int getMaxHeight() {
        return 0;  
    }

    @Override
    public int getSeaLevel() {
        return 0;  
    }

    @Override
    public boolean getKeepSpawnInMemory() {
        return false;  
    }

    @Override
    public void setKeepSpawnInMemory(boolean b) {
        
    }

    @Override
    public boolean isAutoSave() {
        return false;  
    }

    @Override
    public void setAutoSave(boolean b) {
        
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        
    }

    @Override
    public Difficulty getDifficulty() {
        return null;  
    }

    @Override
    public File getWorldFolder() {
        return null;  
    }

    @Override
    public WorldType getWorldType() {
        return null;  
    }

    @Override
    public boolean canGenerateStructures() {
        return false;  
    }

    @Override
    public long getTicksPerAnimalSpawns() {
        return 0;  
    }

    @Override
    public void setTicksPerAnimalSpawns(int i) {
        
    }

    @Override
    public long getTicksPerMonsterSpawns() {
        return 0;  
    }

    @Override
    public void setTicksPerMonsterSpawns(int i) {
        
    }

    @Override
    public void setMetadata(String s, MetadataValue metadataValue) {
        
    }

    @Override
    public List<MetadataValue> getMetadata(String s) {
        return null;  
    }

    @Override
    public boolean hasMetadata(String s) {
        return false;  
    }

    @Override
    public void removeMetadata(String s, Plugin plugin) {
        
    }

    @Override
    public void sendPluginMessage(Plugin plugin, String s, byte[] bytes) {
        
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return null;  
    }
}
