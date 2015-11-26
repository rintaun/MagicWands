package com.github.rintaun.magicwands.common.command;

import com.github.rintaun.magicwands.common.entity.EntityMagicDummyMinecartCommandBlock;
import com.github.rintaun.magicwands.common.entity.EntityThrowableCommand;
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
    private EntityMagicDummyMinecartCommandBlock dummy;
    private ItemStack wandEditing;

    private boolean doChatOutput = false;
    private int perms = 0;
    private boolean canExecute = true;

    public MagicWandsCommandHandler(World world, BlockPos pos, int perms, Entity sender, NBTTagCompound commandData)
    {
        this.setup(world, pos, perms, sender, commandData);
    }

    public MagicWandsCommandHandler(World world, BlockPos pos, EntityThrowableCommand sender)
    {
        this.setup(world, pos, 2, sender, sender.getCommandData());
    }

    public MagicWandsCommandHandler(World world, BlockPos pos, ItemStack wand, EntityMagicDummyMinecartCommandBlock dummy, EntityPlayer player, NBTTagCompound commandData)
    {
        this.canExecute = false;
        this.wandEditing = wand;
        this.setup(world, pos, MagicWandsCommandHandler.getPermLevelForPlayer(player), dummy, commandData);
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
        return permLevel <= this.perms;
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

    /**
     * public int advCdmPacketType()
     *
     * if 0, payload is int X, int Y, int Z
     * if 1, pa1load is int entityID
     */
    @SideOnly(Side.CLIENT)
    public int func_145751_f()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    /**
     * public void fillAdvCdmPayload(ByteBuf advCdmPacket)
     *
     * fills the AdvCdm packet payload with either XYZ coords or
     * the ID of the associated entity
     *
     * in our case, fill with the sender id if available
     */
    public void func_145757_a(ByteBuf p_145757_1_)
    {
        p_145757_1_.writeInt(this.sender.getEntityId());
    }

    public boolean func_175574_a(EntityPlayer player)
    {
        if (!(this.sender instanceof EntityMagicDummyMinecartCommandBlock) || !player.canUseCommand(this.perms, "@"))
        {
            return false;
        }
        else
        {
            player.openEditCommandBlock(this);
            return true;
        }
    }

    public void setCommand(String command)
    {
        super.setCommand(command);
        if (this.wandEditing instanceof ItemStack)
        {
            NBTTagCompound wandData = new NBTTagCompound();
            this.wandEditing.writeToNBT(wandData);

            NBTTagCompound tag = new NBTTagCompound();
            this.writeDataToNBT(tag);

            wandData.setTag("tag", tag);

            this.wandEditing.readFromNBT(wandData);
        }
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
