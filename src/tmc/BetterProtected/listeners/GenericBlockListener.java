package tmc.BetterProtected.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import tmc.BetterProtected.domain.BlockCoordinate;
import tmc.BetterProtected.domain.BlockEvent;
import tmc.BetterProtected.domain.World;
import tmc.BetterProtected.services.BlockEventRepository;

import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.bukkit.Material.*;
import static tmc.BetterProtected.domain.types.BlockEventType.REMOVED;

public class GenericBlockListener {
    protected BlockEventRepository blockEventRepository;
    protected Set<Material> ignoredMaterial;

    public GenericBlockListener(BlockEventRepository blockEventRepository, List<Integer> unprotectedBlockIds) {
        this.blockEventRepository = blockEventRepository;
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

    boolean doesPlayerHavePermissionToBreak(Player player, BlockEvent mostRecentBlockEvent, Block block) {
        if (mostRecentBlockEvent == null ) return true;
        if (mostRecentBlockEvent.getBlockEventType() == REMOVED) return true;
        if (isBlockEventOwnedByPlayer(player, mostRecentBlockEvent)) return true;
        if (isMaterialIgnored(block.getType()) && isMaterialIgnored(mostRecentBlockEvent.getMaterial())) {
            return true;
        }
        return player.isOp();
    }

    boolean doesPlayerHavePermissionToPlace(Player player, Block block, BlockEvent mostRecentBlockEvent) {
        if (mostRecentBlockEvent == null)
            return true;

        if (mostRecentBlockEvent.getBlockEventType() == REMOVED)
            return true;

        if (isBlockEventOwnedByPlayer(player, mostRecentBlockEvent) &&
                isAllowedToPlaceBlockIntoLiquid(mostRecentBlockEvent, block))
            return true;

        return player.isOp();
    }

    boolean isBlockEventOwnedByPlayer(Player player, BlockEvent mostRecentBlockEvent) {
        return mostRecentBlockEvent != null && mostRecentBlockEvent.getOwner().getUsername().equalsIgnoreCase(player.getName());
    }

    boolean isAllowedToPlaceBlockIntoLiquid(BlockEvent blockEvent, Block blockInHand) {
        return isMaterialLiquid(blockEvent.getMaterial()) && !isMaterialLiquid(blockInHand.getType());
    }

    boolean isMaterialIgnored(Material material) {
        return ignoredMaterial.contains(material);
    }

    boolean isMaterialLiquid(Material material) {
        return material == STATIONARY_LAVA || material == STATIONARY_WATER ||
                material == LAVA || material == WATER;
    }
}
