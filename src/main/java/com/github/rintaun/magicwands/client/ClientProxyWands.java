package com.github.rintaun.magicwands.client;

import com.github.rintaun.magicwands.common.MagicWands;
import com.github.rintaun.magicwands.common.CommonProxyWands;
import com.github.rintaun.magicwands.common.item.ItemMagicWand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;

public class ClientProxyWands extends CommonProxyWands
{
    @Override
    public void registerKeybinds()
    {
        FMLCommonHandler.instance().bus().register(MagicWands.magicWand);

        MagicWands.wandEdit = new KeyBinding("key.wandEdit", Keyboard.KEY_Z, "key.categories.magicwands");
        ClientRegistry.registerKeyBinding(MagicWands.wandEdit);
    }

    @Override
    public void registerRenderers()
    {
    }

    @Override
    public void registerItemResources()
    {
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        mesher.register(MagicWands.magicWand, 0, new ModelResourceLocation("magicwands:item_wand", "inventory"));
    }
}
