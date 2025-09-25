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

public class ImmensePull {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("pull")
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



                                                                        for (LivingEntity e : entities) {
                                                                            Vec3d direction = new Vec3d(pos.getX() - e.getX(), pos.getY() - e.getY(), pos.getZ() - e.getZ());
                                                                            e.addVelocity(direction.normalize().multiply(ps));
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