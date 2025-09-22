package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;


import net.minecraft.util.math.Vec3d;

import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SmoothPull {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("gpull")
                        .then(argument("pull_center", BlockPosArgumentType.blockPos())
                                .then(argument("pull_radius", IntegerArgumentType.integer())
                                        .then(argument("pull_strength", FloatArgumentType.floatArg())
                                                .then(argument("pull_player", BoolArgumentType.bool())
                                                        .executes(context -> {


                                //Fg = G * (m1m2)/r^2
                                // G is constant, m1,m2 are masses of objects
                                // r^2 is the raidus between objects
                                //Fg is the force applied
                                //use F=ma to accelerate, where f and m are known.

                                try {
                                    ServerWorld world = context.getSource().getWorld();
                                    Integer r = IntegerArgumentType.getInteger(context, "pull_radius");
                                    float ps = FloatArgumentType.getFloat(context, "pull_strength");
                                    boolean pullPlayer = BoolArgumentType.getBool(context,"pull_player");


                                    BlockPos pos = new BlockPos(BlockPosArgumentType.getBlockPos(context, "pull_center"));


                                    Box box = new Box(
                                            pos.getX() - r, pos.getY() - r, pos.getZ() - r,
                                            pos.getX() + r, pos.getY() + r, pos.getZ() + r
                                    );


                                        List<LivingEntity> entities = world.getEntitiesByClass(
                                                LivingEntity.class,
                                                box,
                                                entity -> !pullPlayer ? !(entity instanceof PlayerEntity) : true
                                        );



                                    Vec3d targetPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                                    double speed = 0.5; // blocks per tick


                                    for (LivingEntity e : entities) {
                                        Vec3d direction = targetPos.subtract(e.getPos());
                                        double distance = direction.length();

                                        if (distance < 0.1) {
                                            e.setVelocity(Vec3d.ZERO); // stop entity
                                        } else {
                                            // clamp velocity so we never overshoot
                                            Vec3d move = direction.normalize().multiply(Math.min(speed, distance));
                                            e.setVelocity(move);
                                        }

                                        e.velocityModified = true;
                                    }


                                } catch (Exception e) {
                                    LOGGER.error("An error has occurred " + e);
                                }

                                return Command.SINGLE_SUCCESS;


                          }

                          )


                        )))));
    }
}
