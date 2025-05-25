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
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import com.sk89q.worldguard.protection.managers.RegionManager;
import net.goldtreeservers.worldguardextraflags.flags.Flags;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class WorldEditFlagHandler extends AbstractDelegateExtent
{
    private final LocalPlayer player;

    private final RegionManager regionManager;

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
        if (regions.queryState(this.player, Flags.WORLDEDIT) != State.DENY)
        {
            return super.setBlock(location, block);
        }

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
            return super.setBlocks(region, pattern);
        }
        return 0;
    }

    @Override
    public <B extends BlockStateHolder<B>> int setBlocks(final Region region, final B block) throws MaxChangedBlocksException {
        if (hasPermissionInRegion(region))
        {
            return super.setBlocks(region, block);
        }
        return 0;
    }

    @Override
    public int setBlocks(final @NotNull Set<BlockVector3> vset, final Pattern pattern) {
        boolean hasPermission = true;
        for (BlockVector3 position : vset) {
            ApplicableRegionSet regions = this.regionManager.getApplicableRegions(position);
            if (regions.queryState(this.player, Flags.WORLDEDIT) != State.DENY)
            {
                hasPermission = false;
                break; // No need to check further if one position is allowed
            }
        }
        if (hasPermission) {
            return super.setBlocks(vset, pattern);
        }
        return 0;
    }

    private boolean hasPermissionInRegion(@NotNull Region region) {
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
            return super.replaceBlocks(region, mask, pattern);
        }
        return 0;
    }

    @Override
    public <B extends BlockStateHolder<B>> int replaceBlocks(final @NotNull Region region, final Set<BaseBlock> filter, final B replacement) throws MaxChangedBlocksException
    {
        if (hasPermissionInRegion(region))
        {
            return super.replaceBlocks(region, filter, replacement);
        }
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
            if (regions.queryState(this.player, Flags.WORLDEDIT) != State.DENY)
            {
                hasPermission = false;
            }
        }
        if (hasPermission)
        {
            return super.replaceBlocks(region, filter, pattern);
        }
        return 0;
    }
}
