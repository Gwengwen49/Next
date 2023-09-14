package fr.gwengwen49.next.sky;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class MeteoreCrashEvent implements SkyEvent{


    @Override
    public String getName() {
        return "meteore_crash";
    }

    public void onActivated(ServerWorld world, PlayerEntity triggerPlayer){

    }

}
