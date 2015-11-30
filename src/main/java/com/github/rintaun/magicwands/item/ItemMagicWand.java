package com.github.rintaun.magicwands.item;

import com.github.rintaun.magicwands.MagicWands;
import com.github.rintaun.magicwands.client.gui.GuiEditWand;
import com.github.rintaun.magicwands.command.MagicWandsCommandHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/* NBT Structure:
 *   tags
 *     (String) Command: the command to perform
 *     (bool) useTargetPos: execute the command from target's position
 *     (bool) doChatOutput: log the result to chat
 */
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
        if (this.useTargetPos(stack))
        {
            FMLLog.info("Using Target... ("+pos.getX()+", "+pos.getY()+", "+pos.getZ()+")");
            handleWandUse(stack, worldIn, pos, playerIn);
        }
        else
        {
            handleWandUse(stack, worldIn, playerIn.getPosition(), playerIn);
        }
        return true;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
    {
        if (this.useTargetPos(stack))
        {
            handleWandUse(stack, target.worldObj, target.getPosition(), playerIn);
        }
        else
        {
            handleWandUse(stack, playerIn.worldObj, playerIn.getPosition(), playerIn);
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!this.useTargetPos(itemStackIn))
        {
            handleWandUse(itemStackIn, worldIn, playerIn.getPosition(), playerIn);
        }
        return itemStackIn;
    }

    public void doCommandEdit(ItemStack itemStack, World world, EntityPlayer player, String cmdNew)
    {
        MagicWandsCommandHandler commandHandler = new MagicWandsCommandHandler(world, player.getPosition(), this.getPermLevel(itemStack), player, ItemMagicWand.getDataTagCompound(itemStack));
        commandHandler.setCommand(cmdNew);
        commandHandler.updateWand(itemStack);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!MagicWands.wandEdit.isPressed()) { return; }

        EntityPlayer p = Minecraft.getMinecraft().thePlayer;
        if (p == null) { return; }

        ItemStack inHand = p.getHeldItem();
        if (inHand == null) { return; }

        Item theItem = inHand.getItem();
        if (!(theItem instanceof ItemMagicWand)) { return; }

        FMLLog.info("WE'RE GOING IN FOR THE EDIT");
        FMLLog.info("GET READY TO CRASH");
        this.editCommand(inHand, p.getEntityWorld(), p);
    }

    public void editCommand(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        FMLLog.info("Oh wow oh wow");
        if (playerIn.getEntityWorld().isRemote)
        {
            FMLLog.info("And they're remote to boot!");
            MagicWandsCommandHandler commandHandler = new MagicWandsCommandHandler(
                worldIn,
                playerIn.getPosition(),
                MagicWandsCommandHandler.getPermLevelForPlayer(playerIn),
                playerIn,
                ItemMagicWand.getDataTagCompound(itemStackIn)
            );
            Minecraft.getMinecraft().displayGuiScreen(new GuiEditWand(itemStackIn, commandHandler));
        } else {
            FMLLog.info("But they're not remote =(");
        }
    }

    @Override
    public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        NBTTagCompound tag;
        int[] ench = new int[1];
        if (!nbt.hasKey("tag")) {
            tag = new NBTTagCompound();
        } else {
            tag = nbt.getCompoundTag("tag");
        }
        tag.setIntArray("ench", ench);
        nbt.setTag("tag", tag);
        return true;
    }

    public static NBTTagCompound getDataTagCompound(ItemStack wand)
    {
        NBTTagCompound wandData = new NBTTagCompound();
        wand.writeToNBT(wandData);
        return wandData.getCompoundTag("tag");
    }

    public boolean useTargetPos(ItemStack wand)
    {
        NBTTagCompound tags = this.getDataTagCompound(wand);
        if (tags == null || !tags.hasKey("UseTargetPos"))
        {
            return false;
        }
        return tags.getByte("UseTargetPos") == 1;
    }

    public int getPermLevel(ItemStack wand)
    {
        NBTTagCompound wandData = new NBTTagCompound();
        wand.writeToNBT(wandData);
        return wandData.getInteger("permLevel");
    }

    public void handleWandUse(ItemStack wand, World world, BlockPos pos, EntityPlayer playerIn)
    {
        MagicWandsCommandHandler commandHandler = new MagicWandsCommandHandler(world, pos, this.getPermLevel(wand), playerIn, this.getDataTagCompound(wand));
        commandHandler.execute();
    }
}
