package gmm;

import net.fabricmc.api.ModInitializer;

import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gmm.commands.Velocity;


import java.util.function.Supplier;

public class GladiusMathMod implements ModInitializer {
    public static final String MOD_ID = "gmm";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        Velocity.register();


            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                    CommandManager.literal("gmm")
                            .executes(context -> {
                                ServerCommandSource source = context.getSource();
                                try {
                                    String modId = "gladius-math-mod";
                                    String version = FabricLoader.getInstance()
                                            .getModContainer(modId)
                                            .map(mod -> mod.getMetadata().getVersion().getFriendlyString())
                                            .orElse("unknown");

                                    source.sendFeedback(() -> Text.literal("Running GMM version " + version), true);
                                }  catch (Exception e) {
                                LOGGER.error("Error occurred!", e);
                                source.sendFeedback(() -> Text.literal("An internal error has occurred."), true);
        }

                                return Command.SINGLE_SUCCESS;
                            })
            ));





        LOGGER.info("Hello Fabric world!");
    }
}