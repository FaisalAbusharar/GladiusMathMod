package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;

import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.text.Text;


import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Sphere {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("sphere")
                        .then(argument("pos", BlockPosArgumentType.blockPos())
                                        .then(argument("Radius",IntegerArgumentType.integer())
                                                .then(argument("Block", StringArgumentType.string())
                                                        .executes(context -> {

                                                            ServerCommandSource source = context.getSource();

                                                            String blockName = StringArgumentType.getString(context, "Block");
                                                            java.lang.Integer r = IntegerArgumentType.getInteger(context, "Radius");

                                                            Identifier id = new Identifier(blockName);

                                                            Block block = Registries.BLOCK.get(id);

                                                            if (block == Blocks.AIR) {
                                                                context.getSource().sendFeedback(()-> Text.literal("Invalid Block: " + blockName), true);
                                                            }

                                                            ServerWorld world = context.getSource().getWorld();

                                                            BlockPos pos = new BlockPos(BlockPosArgumentType.getBlockPos(context, "pos"));


                                                            // Circle code.
//                                                            world.setBlockState(pos, block.getDefaultState());


                                                            BlockPos start = new BlockPos(pos.add(r, r ,r));
                                                            BlockPos end = new BlockPos(pos.add(-r, -r ,-r));

                                                            int minX = Math.min(start.getX(), end.getX());
                                                            int maxX = Math.max(start.getX(), end.getX());
                                                            int minY = Math.min(start.getY(), end.getY());
                                                            int maxY = Math.max(start.getY(), end.getY());
                                                            int minZ = Math.min(start.getZ(), end.getZ());
                                                            int maxZ = Math.max(start.getZ(), end.getZ());


//                                                            for (int x = minX; x <= maxX; x++) {
//                                                                world.setBlockState(new BlockPos(x, pos.getY(), pos.getZ()), block.getDefaultState());
//                                                            }
//                                                            for (int y = minY; y <= maxY; y++)
//                                                            {
//                                                                world.setBlockState(new BlockPos(pos.getX(), y, pos.getZ()), block.getDefaultState());
//
//                                                            }
//
//                                                            for (int z = minZ; z <= maxZ; z++)
//                                                            {
//                                                                world.setBlockState(new BlockPos(pos.getX(), pos.getY(), z), block.getDefaultState());
//
//                                                            }


                                                            // cx and cz = circle center
                                                            // (x-cx)^2 + (x-cz)^ <= r^2
                                                            // circle center is pos argument. (cx, cz)
                                                            // r is radii argument

                                                            // for(int x = cx -r; x <= cx + r; x++)

                                                            for(int x = pos.getX() - r; x <= pos.getX() + r; x++){
                                                                for(int z = pos.getZ() - r; z <= pos.getZ() + r; z++) {
                                                                    for (int y = pos.getY() - r; y <= pos.getY() + r; y++) {
                                                                        double dx = x + 0.5 - pos.getX(); // horizontal distance from current block x to the center x
                                                                        double dz = z + 0.5 - pos.getZ(); // horizontal distance from current block z to the center z
                                                                        double dy = y + 0.5 - pos.getY(); // vertical distance from current block z to the center y


                                                                        // This asks if the block is within the circle edge or not, we use the formula which says that
                                                                        // the sum of squares of distances from the center (dx² + dz²) must be less than or equal to the radius squared (r²)
                                                                        if (dx * dx + dz * dz + dy*dy <= r * r) {
                                                                            world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
                                                                            context.getSource().sendFeedback(() -> Text.literal("should place"), true);

                                                                        }
                                                                        context.getSource().sendFeedback(() -> Text.literal("In for loop"), true);

                                                                    }
                                                                }
                                                            }




                                                            context.getSource().sendFeedback(() -> Text.literal("Placed " + blockName + " at " + pos), true);
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        ))
                                );

    }
}
