package com.github.rintaun.magicwands;

import com.github.rintaun.magicwands.item.ItemMagicWand;
import com.github.rintaun.magicwands.network.MagicWandsUpdateMessage;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.LanguageRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = MagicWands.MOD_ID, name = MagicWands.MOD_NAME, version = MagicWands.MOD_VER)
public class MagicWands
{
    public static final String MOD_ID = "${__MOD_ID__}";
    public static final String MOD_NAME = "${__MOD_NAME__}";
    public static final String MOD_VER = "${__MOD_VER__}";

    public static Item magicWand = new ItemMagicWand();
    public static KeyBinding wandEdit;
    public static SimpleNetworkWrapper network;

    static class Messages {
        public static final int UPDATEWAND = 0;
    }

    @SidedProxy(
        clientSide = "com.github.rintaun.magicwands.client.ClientProxyWands",
        serverSide = "com.github.rintaun.magicwands.common.CommonProxyWands"
    )
    public static CommonProxyWands proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MagicWands.network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        network.registerMessage(MagicWandsUpdateMessage.Handler.class, MagicWandsUpdateMessage.class, Messages.UPDATEWAND, Side.SERVER);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
