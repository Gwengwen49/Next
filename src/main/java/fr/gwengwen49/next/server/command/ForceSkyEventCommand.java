package fr.gwengwen49.next.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import fr.gwengwen49.next.network.packet.s2c.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import javax.security.auth.login.FailedLoginException;

public class ForceSkyEventCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("next_event")
                        .then(CommandManager.argument("value", BoolArgumentType.bool()).executes(context -> execute(context.getSource(), BoolArgumentType.getBool(context, "value"))));
        dispatcher.register(builder);
    }

    public static int execute(ServerCommandSource source, boolean value){

        FallingBlockEntity barrel = FallingBlockEntity.spawnFromBlock(source.getWorld(), source.getPlayer().getBlockPos().mutableCopy().add(0, 100, 0), Blocks.BARREL.getDefaultState());
        Vec3d vec = barrel.getVelocity();
        barrel.setVelocity(vec.x, 0, vec.z);

        FallingBlockEntity barrier = FallingBlockEntity.spawnFromBlock(source.getWorld(), source.getPlayer().getBlockPos().mutableCopy().add(0, 101, 0), Blocks.OAK_FENCE.getDefaultState());
        Vec3d vec1 = barrier.getVelocity();
        barrel.setVelocity(vec1.x, 0, vec1.z);
        FallingBlockEntity wool;
        source.getWorld().spawnEntity(barrel);
        source.getWorld().spawnEntity(barrier);
        for(int x = -1; x <= 1; x++){
            for(int z = -1; z <= 1; z++){
               wool = FallingBlockEntity.spawnFromBlock(source.getWorld(), source.getPlayer().getBlockPos().mutableCopy().add(x, 102, z), Blocks.WHITE_WOOL.getDefaultState());
               Vec3d vec2 = wool.getVelocity();

               wool.setVelocity(vec2.x, 0, vec2.z);

               source.getWorld().spawnEntity(wool);
            }
        }
        return 1;
    }
}
