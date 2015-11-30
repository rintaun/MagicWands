package com.github.rintaun.magicwands.command;

import com.github.rintaun.magicwands.entity.EntityThrowableCommand;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MagicWandsCommandHandler extends CommandBlockLogic
{
    private World world;
    private BlockPos pos;
    private NBTTagCompound commandData;
    private Entity sender;

    private boolean doChatOutput = false;
    private int perms = 0;
    private boolean canExecute = true;

    public MagicWandsCommandHandler(World world, BlockPos pos, int perms, Entity sender, NBTTagCompound commandData)
    {
        this.setup(world, pos, perms, sender, commandData);
    }

    /*
    public MagicWandsCommandHandler(World world, BlockPos pos, IMagicWandsCommandSender sender, int perms)
    {
        this.setup(world, pos, perms, sender, sender.getCommandData());
    }
    */

    private void setup(World world, BlockPos pos, int perms, Entity sender, NBTTagCompound commandData)
    {
        this.world = world;
        this.pos = pos;
        this.perms = perms;
        this.sender = sender;
        this.commandData = commandData;
        this.readDataFromNBT(this.commandData);
    }

    public NBTTagCompound getCommandData()
    {
        return this.commandData;
    }

    public static int getPermLevelForPlayer(EntityPlayer player)
    {
        int perms = 0;
        MinecraftServer server = MinecraftServer.getServer();

        if (server.getConfigurationManager().canSendCommands(player.getGameProfile()))
        {
            UserListOpsEntry userlistopsentry = (UserListOpsEntry)server.getConfigurationManager().getOppedPlayers().getEntry(player.getGameProfile());
            if (userlistopsentry != null)
            {
                perms = userlistopsentry.getPermissionLevel();
            }
            else
            {
                perms = server.getOpPermissionLevel();
            }
        }
        return perms;
    }

    @Override
    public void readDataFromNBT(NBTTagCompound tagCompound)
    {
        super.readDataFromNBT(tagCompound);
        this.doChatOutput = tagCompound.getByte("doChatOutput") == 1;
    }

    @Override
    public void writeDataToNBT(NBTTagCompound tagCompound)
    {
        super.writeDataToNBT(tagCompound);
        tagCompound.setByte("doChatOutput", (byte)(this.doChatOutput ? 1 : 0));
    }

    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true;
        //return permLevel < 2;
        //return permLevel <= this.perms;
    }

    public void execute()
    {
        if (!this.canExecute) { return; }
        this.readDataFromNBT(this.commandData);
        if (!this.world.isRemote)
        {
            this.trigger(this.world);
        }
        this.writeDataToNBT(this.commandData);
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
    public int func_145751_f(){ return 2; }
    public void func_145757_a(ByteBuf packetAdvCdm){}

    public void setCommand(String command)
    {
        super.setCommand(command);
    }

    public void updateWand(ItemStack wand)
    {
        NBTTagCompound wandData = new NBTTagCompound();
        wand.writeToNBT(wandData);

        NBTTagCompound tag = new NBTTagCompound();
        this.writeDataToNBT(tag);

        wandData.setTag("tag", tag);

        wand.readFromNBT(wandData);
    }

    public Entity getCommandSenderEntity()
    {
        return this.sender;
    }

    @Override
    public boolean sendCommandFeedback()
    {
        return this.doChatOutput && super.sendCommandFeedback();
    }
}
