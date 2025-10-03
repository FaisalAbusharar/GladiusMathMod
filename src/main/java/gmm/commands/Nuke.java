package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Nuke {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("nuke")
                        .then(argument("pos", BlockPosArgumentType.blockPos())
                                .then(argument("radius", IntegerArgumentType.integer(1))
                                        .then(argument("hollow", BoolArgumentType.bool())
                                                .executes(context -> {
                                                    BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                                    int radius = IntegerArgumentType.getInteger(context, "radius");
                                                    boolean hollow = BoolArgumentType.getBool(context, "hollow");

                                                    ServerWorld world = context.getSource().getWorld();
                                                    int count = 0;

                                                    for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
                                                        for (int y = pos.getY() - radius; y <= pos.getY() + radius; y++) {
                                                            for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
                                                                double dx = x + 0.5 - pos.getX();
                                                                double dy = y + 0.5 - pos.getY();
                                                                double dz = z + 0.5 - pos.getZ();

                                                                double distanceSq = dx * dx + dy * dy + dz * dz;

                                                                double rSq = (radius + 0.5) * (radius + 0.5);
                                                                double innerSq = (radius - 1) * (radius - 1);

                                                                if (!hollow && distanceSq <= rSq ||
                                                                        hollow && distanceSq <= rSq && distanceSq >= innerSq) {

                                                                    TntEntity tnt = new TntEntity(world, x + 0.5, y, z + 0.5, null);
                                                                    tnt.setFuse(40); // ~2 seconds fuse
                                                                    world.spawnEntity(tnt);
                                                                    count++;
                                                                }
                                                            }
                                                        }
                                                    }

                                                    int finalCount = count;
                                                    context.getSource().sendFeedback(() -> Text.literal("Spawned " + finalCount + " TNTs at " + pos), true);
                                                    return Command.SINGLE_SUCCESS;
                                                })))));
    }
}
