package fr.gwengwen49.next.sky;

import fr.gwengwen49.next.Next;
import net.fabricmc.fabric.mixin.event.lifecycle.WorldMixin;
import net.fabricmc.fabric.mixin.rendering.data.attachment.WorldViewMixin;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WorldExtension {

    public static void onTick(ServerWorld world) {
        if(world.getRegistryKey().equals(Next.OUTSIDE_WORLD_DIMENSION_KEY)){

        }

    }



}
