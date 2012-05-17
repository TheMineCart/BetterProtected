package tmc.BetterProtected.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.services.BlockEventRepository;
import tmc.BetterProtected.services.PlayerRepository;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;
import static tmc.BetterProtected.domain.types.BlockEventType.UNPROTECTED;

public class GenericBlockListener {
    protected BlockEventRepository blockEventRepository;
    protected Set<Material> ignoredMaterial;
    protected PlayerRepository playerRepository;

    public GenericBlockListener(BlockEventRepository blockEventRepository, PlayerRepository playerRepository, List<Integer> unprotectedBlockIds) {
        this.blockEventRepository = blockEventRepository;
        this.playerRepository = playerRepository;
        this.ignoredMaterial = newHashSet();
        for (Integer blockTypeId : unprotectedBlockIds) {
            ignoredMaterial.add(Material.getMaterial(blockTypeId));
        }
    }

    BlockEvent getMostRecentBlockEvent(Block block) {
        BlockCoordinate blockCoordinate = BlockCoordinate.newCoordinate(block);
        World world = World.newWorld(block);
        return blockEventRepository.findMostRecent(blockCoordinate, world);
    }

    boolean doesPlayerHavePermissionToBreak(Player player, BlockEvent blockEvent, Block block) {
        if (blockEvent == null ) return true;
        if (blockEvent.getBlockEventType() == REMOVED) return true;
        if (blockEvent.getBlockEventType() == UNPROTECTED) return true;
        if (isBlockEventOwnedByPlayer(player, blockEvent)) return true;
        if (isPlayerFriendOfBlockEventOwner(player, blockEvent)) return true;
        if (isMaterialIgnored(block.getType()) && isMaterialIgnored(blockEvent.getMaterial())) return true;
        return player.isOp();
    }

    boolean doesPlayerHavePermissionToPlace(Player player, Block block, BlockEvent blockEvent) {
        if (blockEvent == null) return true;
        if (blockEvent.getBlockEventType() == REMOVED) return true;
        if (blockEvent.getBlockEventType() == UNPROTECTED) return true;
        if(isBlockEventOwnedByPlayer(player, blockEvent))  return true;
        if(isPlayerFriendOfBlockEventOwner(player, blockEvent)) return true;
        return player.isOp();
    }

    boolean isBlockEventOwnedByPlayer(Player player, BlockEvent blockEvent) {
        return blockEvent != null && blockEvent.getOwner().getUsername().equalsIgnoreCase(player.getName());
    }

    boolean isPlayerFriendOfBlockEventOwner(Player player, BlockEvent blockEvent) {
        if(player == null || blockEvent == null) return false;
        Set<String> friendsByName = playerRepository.findFriendsByName(blockEvent.getOwner().getUsername());
        return friendsByName.contains(player.getName());
    }

    boolean isMaterialIgnored(Material material) {
        return ignoredMaterial.contains(material);
    }

    boolean isMaterialLiquid(Material material) {
        return material == STATIONARY_LAVA || material == STATIONARY_WATER ||
                material == LAVA || material == WATER;
    }
}