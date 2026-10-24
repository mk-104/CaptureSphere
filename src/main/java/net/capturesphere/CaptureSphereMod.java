package net.capturesphere;

import net.capturesphere.init.ModDataComponentTypes;
import net.capturesphere.init.ModEntities;
import net.capturesphere.init.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Capture Sphere Mod のメイン初期化クラス (Minecraft 26.3)。
 */
public class CaptureSphereMod implements ModInitializer {

    public static final String MOD_ID = "capturesphere";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Capture Sphere Mod 初期化開始 (Minecraft 26.3)...");
        ModDataComponentTypes.initialize();
        ModItems.initialize();
        ModEntities.initialize();
        LOGGER.info("Capture Sphere Mod 初期化完了!");
    }
}
