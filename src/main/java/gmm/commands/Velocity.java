package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Velocity {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                    literal("velocity")
                            .then(argument("target", EntityArgumentType.players())
                                .then(argument("Vx", FloatArgumentType.floatArg())
                                    .then( argument("Vy", FloatArgumentType.floatArg())
                                        .then(argument( "Vz", FloatArgumentType.floatArg())
                    .executes(context -> {

                ServerCommandSource source = context.getSource();
                        Collection<ServerPlayerEntity> players =
                                EntityArgumentType.getPlayers(context, "target");



                float VelocityX = FloatArgumentType.getFloat(context,"Vx");
                float VelocityY = FloatArgumentType.getFloat(context,"Vy");
                float VelocityZ = FloatArgumentType.getFloat(context,"Vz");

                        for (ServerPlayerEntity player : players) {
                            player.addVelocity(new Vec3d(VelocityX, VelocityY, VelocityZ));
                            player.velocityModified = true;
                        }


                source.sendFeedback(() -> Text.literal("Velocity of " + players.size() +  " player(s) changed."), true);
                return Command.SINGLE_SUCCESS;
                    })
                                    )
                                )
                        )
            ));
    }
    }
