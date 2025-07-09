package net.goldtreeservers.worldguardextraflags.we.handlers;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BaseBlock;
import com.sk89q.worldguard.LocalPlayer;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldguard.bukkit.BukkitPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import com.sk89q.worldguard.protection.managers.RegionManager;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class WorldEditFlagHandler extends AbstractDelegateExtent
{
    private final LocalPlayer player;

    private final RegionManager regionManager;
    private static final Logger logger = LoggerFactory.getLogger(WorldEditFlagHandler.class);
    private static final String DEBUG_PERMISSION = "wgef.worldedit.debug";
    private static final String DISABLE_NOTIFY_PERMISSION = "wgef.worldedit.disable_notify";
    private static final String BYPASS_PERMISSION = "wgef.worldedit.bypass";

    public WorldEditFlagHandler(Extent extent, LocalPlayer player, RegionManager regionManager)
    {
        super(extent);

        this.player = player;

        this.regionManager = regionManager;
    }

    @Override
    public boolean setBlock(BlockVector3 location, BlockStateHolder block) throws WorldEditException
    {
        ApplicableRegionSet regions = this.regionManager.getApplicableRegions(location);
        if (player.hasPermission(BYPASS_PERMISSION) || regions.queryState(this.player, Flags.WORLDEDIT) == State.ALLOW)
        {
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("1. Setting block at {} to {}", location, block);
            return super.setBlock(location, block);
        }
        sendNoWorldeditAllowedMessage();
        return false;
    }

    @Override
    public boolean setBlock(int x, int y, int z, BlockStateHolder block) throws WorldEditException
    {
        return setBlock(BlockVector3.at(x, y, z), block);
    }

    @Override
    public int setBlocks(@NotNull Region region, Pattern pattern) throws MaxChangedBlocksException
    {
        if (hasPermissionInRegion(region))
        {
            int blocks = super.setBlocks(region, pattern);
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("2. Set {} blocks in region {} with pattern {}", blocks, region, pattern);
        }
        sendNoWorldeditAllowedMessage();
        return 0;
    }

    @Override
    public <B extends BlockStateHolder<B>> int setBlocks(final Region region, final B block) throws MaxChangedBlocksException {
        if (hasPermissionInRegion(region))
        {
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("3. Setting blocks in region {} to {}", region, block);
            return super.setBlocks(region, block);
        }
        sendNoWorldeditAllowedMessage();
        return 0;
    }

    @Override
    public int setBlocks(final @NotNull Set<BlockVector3> vset, final Pattern pattern) {
        boolean hasPermission = true;
        for (BlockVector3 position : vset) {
            ApplicableRegionSet regions = this.regionManager.getApplicableRegions(position);
            if (regions.queryState(this.player, Flags.WORLDEDIT) == State.DENY)
            {
                hasPermission = false;
                break; // No need to check further if one position is allowed
            }
        }
        if (player.hasPermission(BYPASS_PERMISSION) || hasPermission) {
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("4. Setting blocks at {} with pattern {}", vset, pattern);
            return super.setBlocks(vset, pattern);
        }
        sendNoWorldeditAllowedMessage();
        return 0;
    }

    private boolean hasPermissionInRegion(@NotNull Region region) {
        if (player.hasPermission(BYPASS_PERMISSION)) {
            return true; // Bypass permission overrides all checks
        }
        boolean hasPermission = true;
        Region clonedRegion = region.clone();
        for (BlockVector3 position : clonedRegion)
        {
            ApplicableRegionSet regions = this.regionManager.getApplicableRegions(position);
            if (regions.queryState(this.player, Flags.WORLDEDIT) == State.DENY)
            {
                hasPermission = false;
                break; // No need to check further if one position is allowed
            }
        }
        return hasPermission;
    }

    @Override
    public int replaceBlocks(@NotNull Region region, Mask mask, Pattern pattern) throws MaxChangedBlocksException
    {
        if (hasPermissionInRegion(region))
        {
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("5. Replacing blocks in region {} with mask {} and pattern {}", region, mask, pattern);
            return super.replaceBlocks(region, mask, pattern);
        }
        sendNoWorldeditAllowedMessage();
        return 0;
    }

    @Override
    public <B extends BlockStateHolder<B>> int replaceBlocks(final @NotNull Region region, final Set<BaseBlock> filter, final B replacement) throws MaxChangedBlocksException
    {
        if (hasPermissionInRegion(region))
        {
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("6. Replacing blocks in region {} with filter {} and replacement {}", region, filter, replacement);
            return super.replaceBlocks(region, filter, replacement);
        }
        sendNoWorldeditAllowedMessage();
        return 0;
    }

    @Override
    public int replaceBlocks(final @NotNull Region region, final Set<BaseBlock> filter, final Pattern pattern) throws MaxChangedBlocksException
    {
        boolean hasPermission = true;
        Region clonedRegion = region.clone();
        for (BlockVector3 position : clonedRegion)
        {
            ApplicableRegionSet regions = this.regionManager.getApplicableRegions(position);
            if (regions.queryState(this.player, Flags.WORLDEDIT) == State.DENY)
            {
                hasPermission = false;
                break; // No need to check further if one position is allowed
            }
        }
        if (player.hasPermission(BYPASS_PERMISSION) || hasPermission)
        {
            if (player.hasPermission(DEBUG_PERMISSION)) logger.info("6. Replacing blocks in region {} with filter {} and pattern {}", region, filter, pattern);
            return super.replaceBlocks(region, filter, pattern);
        }
        sendNoWorldeditAllowedMessage();
        return 0;
    }

    private void sendNoWorldeditAllowedMessage() {
        if (player.hasPermission(DEBUG_PERMISSION)) {
            Thread.dumpStack();
            logger.info("WorldEdit operation denied due to WorldGuard restrictions (worldedit flag).");
        }

        if (!player.hasPermission(DISABLE_NOTIFY_PERMISSION) && player instanceof BukkitPlayer bp) {
            bp.getPlayer().sendMessage(Component.text("[WGEF (Zoriot)] ", NamedTextColor.GOLD)
                    .append(Component.text("You are not allowed to perform WorldEdit operations in this region (worldedit flag).", NamedTextColor.DARK_RED)));
        }
    }
}
