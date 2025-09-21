package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Pull {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("pull")
                        .then(argument("pull_center", BlockPosArgumentType.blockPos())
                                .then(argument("radius", IntegerArgumentType.integer())
                                                        .executes(context -> {


                                //Fg = G * (m1m2)/r^2
                                // G is constant, m1,m2 are masses of objects
                                // r^2 is the raidus between objects
                                //Fg is the force applied
                                //use F=ma to accelerate, where f and m are known.


                                ServerWorld world = context.getSource().getWorld();
                                Integer r  = IntegerArgumentType.getInteger(context, "radius");

                                BlockPos pos = new BlockPos(BlockPosArgumentType.getBlockPos(context, "pull_center"));

                                float G = 1;
                                float m1, m2 = 1;

                                Box box = new Box(
                                  pos.getX() - r, pos.getY() - r, pos.getZ() - r,
                                  pos.getX() + r, pos.getY() + r, pos.getZ() + r
                                );

                                List<LivingEntity> entities = world.getEntitiesByClass(
                                        LivingEntity.class,
                                        box,
                                        entity -> true
                                );

                                for (LivingEntity e: entities) {
                                    Vec3d direction = new Vec3d(pos.getX() - e.getX(), pos.getY() - e.getY(), pos.getZ() - e.getZ());
                                    e.addVelocity(direction);
                                    e.velocityModified = true;
                                }

                                return Command.SINGLE_SUCCESS;


                          }

                          )


                        )));
    }
}
