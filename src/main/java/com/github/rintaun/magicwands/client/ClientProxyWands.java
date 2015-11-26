package com.github.rintaun.magicwands.client;

import com.github.rintaun.magicwands.common.MagicWands;
import com.github.rintaun.magicwands.common.CommonProxyWands;
import com.github.rintaun.magicwands.common.item.ItemMagicWand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxyWands extends CommonProxyWands
{
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
