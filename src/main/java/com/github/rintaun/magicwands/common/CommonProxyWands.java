package com.github.rintaun.magicwands.common;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class CommonProxyWands
{
    public void preInit(FMLPreInitializationEvent event)
    {
        registerEntities();
        registerItems();
        registerBlocks();
        registerRecipes();
    }

    private void registerEntities()
    {
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
