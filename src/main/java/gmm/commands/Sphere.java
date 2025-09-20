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


                                                            for (int x = minX; x <= maxX; x++) {
                                                                world.setBlockState(new BlockPos(x, pos.getY(), pos.getZ()), block.getDefaultState());
                                                            }
                                                            for (int y = minY; y <= maxY; y++)
                                                            {
                                                                world.setBlockState(new BlockPos(pos.getX(), y, pos.getZ()), block.getDefaultState());

                                                            }

                                                            for (int z = minZ; z <= maxZ; z++)
                                                            {
                                                                world.setBlockState(new BlockPos(pos.getX(), pos.getY(), z), block.getDefaultState());

                                                            }


                                                            context.getSource().sendFeedback(() -> Text.literal("Placed " + blockName + " at " + pos), true);
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        ))
                                );

    }
}
