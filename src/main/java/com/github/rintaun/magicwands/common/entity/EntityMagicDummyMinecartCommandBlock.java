package com.github.rintaun.magicwands.common.entity;

import com.github.rintaun.magicwands.common.command.IMagicWandsCommandSender;
import com.github.rintaun.magicwands.common.command.MagicWandsCommandHandler;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityMagicDummyMinecartCommandBlock extends EntityMinecartCommandBlock implements IMagicWandsCommandSender
{
    private MagicWandsCommandHandler cbl;

    public EntityMagicDummyMinecartCommandBlock(World worldIn)
    {
        super(worldIn);
    }

    public void setCommandBlockLogic(MagicWandsCommandHandler newCBL) {
        this.cbl = newCBL;
    }

    public NBTTagCompound getCommandData()
    {
        return this.cbl.getCommandData();
    }

    @Override
    public CommandBlockLogic getCommandBlockLogic() {
        return (CommandBlockLogic)this.cbl;
    }
}
