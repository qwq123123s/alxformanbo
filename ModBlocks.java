/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.Rarity
 *  net.minecraft.world.level.block.Block
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.IForgeRegistry
 *  net.minecraftforge.registries.RegistryObject
 */
package com.manbo.v2c;

import com.manbo.v2c.ModItems;
import com.manbo.v2c.block.TianyuanBlock;
import com.manbo.v2c.block.TianyuanBlockUn;
import com.manbo.v2c.item.materials.TianyuanBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create((IForgeRegistry)ForgeRegistries.BLOCKS, (String)"manbov2c");
    public static final RegistryObject<Block> TIANYUAN_BLOCK_UN = BLOCKS.register("tianyuan_block_un", TianyuanBlockUn::new);
    public static final RegistryObject<Block> TIANYUAN_BLOCK = BLOCKS.register("tianyuan_block", TianyuanBlock::new);
    public static final RegistryObject<Item> TIANYUAN_BLOCK_UN_ITEM = ModItems.ITEMS.register("tianyuan_block_un", () -> new TianyuanBlockItem((Block)TIANYUAN_BLOCK_UN.get(), new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryObject<Item> TIANYUAN_BLOCK_ITEM = ModItems.ITEMS.register("tianyuan_block", () -> new TianyuanBlockItem((Block)TIANYUAN_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).fireResistant()));
}

