package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Velocity {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                    literal("velocity")
                        .then(argument("Vx", FloatArgumentType.floatArg())
                                .then( argument("Vy", FloatArgumentType.floatArg())
                                    .then(argument( "Vz", FloatArgumentType.floatArg())
                    .executes(context -> {

                ServerCommandSource source = context.getSource();

                ServerPlayerEntity player = context.getSource().getPlayer();

                float VelocityX = FloatArgumentType.getFloat(context,"Vx");
                float VelocityY = FloatArgumentType.getFloat(context,"Vy");
                float VelocityZ = FloatArgumentType.getFloat(context,"Vz");

                player.addVelocity(new Vec3d(VelocityX, VelocityY, VelocityZ));
                player.velocityModified = true;

                source.sendFeedback(() -> Text.literal("Velocity of player changed."), true);
                return Command.SINGLE_SUCCESS;
                    })
                                    )
                                )
                        )
            );
    }
    }
