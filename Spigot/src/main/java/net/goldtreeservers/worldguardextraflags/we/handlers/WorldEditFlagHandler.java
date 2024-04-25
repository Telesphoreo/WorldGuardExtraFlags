package net.goldtreeservers.worldguardextraflags.we.handlers;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.LocalPlayer;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import net.goldtreeservers.worldguardextraflags.flags.Flags;

public class WorldEditFlagHandler extends AbstractDelegateExtent
{
    private final LocalPlayer player;

    private final RegionManager regionManager;

    public WorldEditFlagHandler(World world, Extent extent, LocalPlayer player, RegionManager regionManager)
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
    public int setBlocks(Region region, Pattern pattern) throws MaxChangedBlocksException
    {
        for (BlockVector3 position : region.clone())
        {
            ApplicableRegionSet regions = this.regionManager.getApplicableRegions(position);
            if (regions.queryState(this.player, Flags.WORLDEDIT) != State.DENY)
            {
                return super.setBlocks(region, pattern);
            }
        }
        return 0;
    }

    @Override
    public int replaceBlocks(Region region, Mask mask, Pattern pattern) throws MaxChangedBlocksException
    {
        for (BlockVector3 position : region.clone())
        {
            ApplicableRegionSet regions = this.regionManager.getApplicableRegions(position);
            if (regions.queryState(this.player, Flags.WORLDEDIT) != State.DENY)
            {
                return super.replaceBlocks(region, mask, pattern);
            }
        }
        return 0;
    }
}
