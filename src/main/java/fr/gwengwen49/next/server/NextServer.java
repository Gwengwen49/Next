package fr.gwengwen49.next.server;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

@Environment(EnvType.SERVER)
public class NextServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
    }
}
