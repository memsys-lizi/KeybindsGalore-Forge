package me.cael.keybindsgalore;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(KeybindsGaloreMod.MOD_ID)
public class KeybindsGaloreMod {
    public static final String MOD_ID = "keybindsgalore";
    public static final Logger LOGGER = LogManager.getLogger();

    public KeybindsGaloreMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, KeybindsGaloreConfig.SPEC, "keybindsgalore-client.toml");
        if (FMLEnvironment.dist == Dist.CLIENT) {
            LOGGER.info("Keybinds Galore (Forge) loaded — conflicting keybinds will show a radial menu. Gameplay bindings in the client config stay vanilla.");
        }
    }
}
