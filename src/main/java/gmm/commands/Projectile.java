package gmm.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class Projectile {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("projectile")
                        .then(argument("Target", EntityArgumentType.players())
                                .then(argument("Angle", FloatArgumentType.floatArg())
                                        .then( argument("Speed", FloatArgumentType.floatArg())
                                                        .executes(context -> {

                                                            ServerCommandSource source = context.getSource();
                                                            Collection<ServerPlayerEntity> players =
                                                                    EntityArgumentType.getPlayers(context, "Target");



                                                            float Speed = FloatArgumentType.getFloat(context,"Speed");;
                                                            float Angle = FloatArgumentType.getFloat(context,"Angle");


                                                            for (ServerPlayerEntity player : players) {
                                                                double rad = Math.toRadians(Angle);
                                                                double yawRad = Math.toRadians(player.getYaw());

                                                                double HorizontalSpeed = Speed * Math.cos(rad);
                                                                double SpeedX = -Math.sin(yawRad) * HorizontalSpeed;
                                                                double SpeedY = Math.sin(rad) * HorizontalSpeed;
                                                                double SpeedZ = Math.cos(yawRad);


                                                                player.setVelocity(SpeedX, SpeedY, SpeedZ);
                                                                player.velocityModified = true;
                                                            }


                                                            source.sendFeedback(() -> Text.literal("Velocity of " + players.size() +  " player(s) changed."), true);
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                                )
                                        )
                                )
                        );
    }
}
