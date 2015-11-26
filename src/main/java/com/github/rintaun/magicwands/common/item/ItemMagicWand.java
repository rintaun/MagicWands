package com.github.rintaun.magicwands.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class ItemMagicWand extends Item
{
    public ItemMagicWand()
    {
        setMaxStackSize(1);
        setUnlocalizedName("item_wand");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (this.useTarget(stack))
        {
            FMLLog.info("Using Target... ("+pos.getX()+", "+pos.getY()+", "+pos.getZ()+")");
            handleWandUse(stack, worldIn, pos);
        }
        else
        {
            handleWandUse(stack, worldIn, playerIn.getPosition());
        }
        return true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
    {
        if (this.useTarget(stack))
        {
            handleWandUse(stack, target.worldObj, target.getPosition());
        }
        else
        {
            handleWandUse(stack, playerIn.worldObj, playerIn.getPosition());
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!this.useTarget(itemStackIn))
        {
            handleWandUse(itemStackIn, worldIn, playerIn.getPosition());
        }
        return itemStackIn;
    }

    public boolean useTarget(ItemStack wand)
    {
        NBTTagCompound wandData = new NBTTagCompound();
        wand.writeToNBT(wandData);
        wandData = wandData.getCompoundTag("tag");

        if (!wandData.hasKey("UseTargetPos")) { return false; }

        return wandData.getBoolean("UseTargetPos");
    }

    public void handleWandUse(ItemStack wand, World world, BlockPos pos)
    {
        ItemMagicWandExecutor wandListener = new ItemMagicWandExecutor(wand, world, pos);
        wandListener.execute();
    }
}
