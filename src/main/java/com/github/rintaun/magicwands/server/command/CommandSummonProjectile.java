package com.github.rintaun.magicwands.server.command;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IProjectile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class CommandSummonProjectile extends CommandBase
{
    public String getName()
    {
        return "summon_projectile";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.summon_projectile.usage";
    }

    public void execute (ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.summon_projectile.usage", new Object[0]);
        }
        else {
            // parse and execute the command
        }
    }

    private List getIProjectileList()
    {
        List<String> entityList = EntityList.getEntityNameList();
        List projectileList = Lists.newArrayList();
        for (String name : entityList)
        {
            Class clazz = (Class)EntityList.stringToClassMapping.get(name);
            if (IProjectile.class.isAssignableFrom(clazz))
            {
                projectileList.add(name);
            }
        }
        return entityList;
    }

    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1) {
            return func_175762_a(args, this.getIProjectileList());
        }
        return null;
    }
}
