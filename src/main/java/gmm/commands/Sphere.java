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
                                                            Identifier id = new Identifier(blockName);

                                                            Block block = Registries.BLOCK.get(id);

                                                            if (block == Blocks.AIR) {
                                                                context.getSource().sendFeedback(()-> Text.literal("Invalid Block: " + blockName), true);
                                                            }

                                                            ServerWorld world = context.getSource().getWorld();

                                                            BlockPos pos = new BlockPos(BlockPosArgumentType.getBlockPos(context, "pos"));

                                                            world.setBlockState(pos, block.getDefaultState());

                                                            context.getSource().sendFeedback(() -> Text.literal("Placed " + blockName + " at " + pos), true);


                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        ))
                                );

    }
}
