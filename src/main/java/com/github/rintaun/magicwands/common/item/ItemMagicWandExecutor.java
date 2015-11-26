package com.github.rintaun.magicwands.common.item;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagicWandExecutor extends CommandBlockLogic
{
    private final ItemStack wand;
    private final World world;
    private final BlockPos pos;

    public ItemMagicWandExecutor(ItemStack wand, World world, BlockPos pos)
    {
        this.wand = wand;
        this.world = world;
        this.pos = pos;
    }

    public NBTTagCompound wandData()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.wand.writeToNBT(nbt);
        return nbt;
    }

    public boolean chatOutput()
    {
        return this.wandData().getBoolean("ChatOutput");
    }

    public void execute()
    {
        if (!this.world.isRemote)
        {
            NBTTagCompound nbt = this.wandData();
            NBTTagCompound tag = nbt.getCompoundTag("tag");

            this.triggerWithData(this.world, tag);

            nbt.setTag("tag", tag);
            this.wand.readFromNBT(nbt);
        }
    }

    public BlockPos getPosition()
    {
        return this.pos;
    }

    public Vec3 getPositionVector()
    {
        return new Vec3(this.pos.getX(),
                        this.pos.getY(),
                        this.pos.getZ());
    }

    public World getEntityWorld()
    {
        return this.world;
    }

    public void func_145756_e(){}

    @SideOnly(Side.CLIENT)
    public int func_145751_f()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void func_145757_a(ByteBuf p_145757_1_)
    {
        BlockPos pos = this.getPosition();
        p_145757_1_.writeInt(pos.getX());
        p_145757_1_.writeInt(pos.getY());
        p_145757_1_.writeInt(pos.getZ());
    }

    public Entity getCommandSenderEntity()
    {
        return null;
    }

    @Override
    public boolean sendCommandFeedback()
    {
        return super.sendCommandFeedback() && this.chatOutput();
    }

    public void triggerWithData(World world, NBTTagCompound data)
    {
        this.readDataFromNBT(data);
        this.trigger(world);
        this.writeDataToNBT(data);
    }
}
