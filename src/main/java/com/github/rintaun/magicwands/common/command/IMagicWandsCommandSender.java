package com.github.rintaun.magicwands.common.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;

public interface IMagicWandsCommandSender extends ICommandSender
{
    public NBTTagCompound getCommandData();
}
