/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.modlauncher.api.ITransformer
 *  net.minecraftforge.forgespi.coremod.ICoreModFile
 *  net.minecraftforge.forgespi.coremod.ICoreModProvider
 */
package com.manbo.v2c.coremod;

import com.manbo.v2c.coremod.FinalSwordBypassTransformer;
import com.manbo.v2c.coremod.UnknownArmorTransformer;
import cpw.mods.modlauncher.api.ITransformer;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.forgespi.coremod.ICoreModFile;
import net.minecraftforge.forgespi.coremod.ICoreModProvider;

public class UnknownArmorCoreModProvider
implements ICoreModProvider {
    private final UnknownArmorTransformer transformer1 = new UnknownArmorTransformer();
    private final FinalSwordBypassTransformer transformer2 = new FinalSwordBypassTransformer();

    public void addCoreMod(ICoreModFile coreModFile) {
    }

    public List<ITransformer<?>> getCoreModTransformers() {
        ArrayList list = new ArrayList();
        list.add(this.transformer1);
        list.add(this.transformer2);
        return list;
    }
}

