package com.github.rintaun.magicwands.network;

import com.github.rintaun.magicwands.MagicWands;
import com.github.rintaun.magicwands.item.ItemMagicWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MagicWandsUpdateMessage implements IMessage
{
    private String command = "";

    public MagicWandsUpdateMessage(){}

    public MagicWandsUpdateMessage(String cmdNew)
    {
        this.setCommand(cmdNew);
    }

    public void setCommand(String cmdNew)
    {
        this.command = cmdNew;
    }

    public String getCommand()
    {
        return this.command;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.command = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.command);
    }

    public static class Handler implements IMessageHandler<MagicWandsUpdateMessage, IMessage>
    {
        @Override
        public IMessage onMessage(final MagicWandsUpdateMessage message, final MessageContext ctx)
        {
            IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
            mainThread.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    EntityPlayer player = ctx.getServerHandler().playerEntity;
                    ItemStack heldItem = player.getHeldItem();

                    if (heldItem.getItem() != MagicWands.magicWand)
                    {
                        System.out.println(String.format("Received update from %s, but item is not a wand!", player.getDisplayName()));
                        return;
                    }

                    System.out.println(String.format("Received command update (\"%s\") from %s", message.getCommand(), player.getDisplayName()));
                    ((ItemMagicWand)MagicWands.magicWand).doCommandEdit(heldItem, player.worldObj, player, message.getCommand());
                }
            });
            return null;
        }
    }
}
