package gmm.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.text.Text;
import net.minecraft.particle.ParticleTypes;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ParticleSphere {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
                literal("particlesphere")
                        .then(argument("pos", BlockPosArgumentType.blockPos())
                                .then(argument("particle", ParticleEffectArgumentType.particleEffect(registryAccess))
                                                .then(argument("radius", DoubleArgumentType.doubleArg(1))
                                                        .then(argument("count", IntegerArgumentType.integer())
                                                        .then(argument("particle_count", IntegerArgumentType.integer())
                                                        .then(argument("particle_speed", DoubleArgumentType.doubleArg())

                                                        .executes(context -> {
                                                            ParticleEffect particle = ParticleEffectArgumentType.getParticle(context, "particle");

                                                            BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                                            int count = IntegerArgumentType.getInteger(context, "count");
                                                            double radius = DoubleArgumentType.getDouble(context, "radius");
                                                            double particleSpeed = DoubleArgumentType.getDouble(context, "particle_speed");
                                                            int particleCount = IntegerArgumentType.getInteger(context,"particle_count");

                                                            ServerWorld world = context.getSource().getWorld();
                                                            Vec3d center = new Vec3d(pos.getX(), pos.getY(), pos.getZ());

                                                            spawnSphereParticles(world, center, radius, count, particle, particleCount, particleSpeed); // 150 particles by default

                                                            context.getSource().sendFeedback(
                                                                    () -> Text.literal("Spawned particle sphere at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " with radius " + radius), true
                                                            );
                                                            return 1;
                                                        }))))))));
    }

    private static void spawnSphereParticles(ServerWorld world, Vec3d center, double radius, int count, ParticleEffect particle, int particleCount, double particleSpeed ) {
        for (int i = 0; i < count; i++) {
            double theta = Math.random() * 2 * Math.PI;
            double phi = Math.acos(2 * Math.random() - 1);

            double px = radius * Math.sin(phi) * Math.cos(theta);
            double py = radius * Math.sin(phi) * Math.sin(theta);
            double pz = radius * Math.cos(phi);

            Vec3d pos = center.add(px, py, pz);

            world.spawnParticles(
                    particle, // change to EXPLOSION if you want
                    pos.x, pos.y, pos.z,
                    particleCount, 0, 0, 0, particleSpeed
            );
        }
    }
}
