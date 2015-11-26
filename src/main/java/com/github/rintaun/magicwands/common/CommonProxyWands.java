package com.github.rintaun.magicwands.common;

import com.github.rintaun.magicwands.common.entity.EntityMagicDummyMinecartCommandBlock;
import com.github.rintaun.magicwands.common.entity.EntityThrowableCommand;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class CommonProxyWands
{
    public void preInit(FMLPreInitializationEvent event)
    {
        registerKeybinds();
        registerEntities();
        registerItems();
        registerBlocks();
        registerRecipes();
    }

    protected void registerKeybinds()
    {
    }

    private void registerEntities()
    {
        int nextId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMagicDummyMinecartCommandBlock.class, "MagicDummyMinecartCommandBlock", nextId);

        nextId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityThrowableCommand.class, "ThrowableCommand", nextId);
    }

    private void registerItems()
    {
        GameRegistry.registerItem(MagicWands.magicWand, "item_wand");
    }

    private void registerBlocks()
    {
    }

    private void registerRecipes()
    {
    }

    public void init(FMLInitializationEvent event)
    {
        registerRenderers();
        registerItemResources();
    }

    protected void registerRenderers() {}
    protected void registerItemResources() {}

    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
