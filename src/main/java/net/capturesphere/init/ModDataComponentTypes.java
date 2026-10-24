package net.capturesphere.init;

import net.capturesphere.CaptureSphereMod;
import net.capturesphere.component.CapturedMobData;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

/**
 * Mod独自のデータコンポーネントタイプを定義・登録するクラス (Minecraft 26.3)。
 */
public class ModDataComponentTypes {

    public static final DataComponentType<CapturedMobData> CAPTURED_MOB = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        Identifier.fromNamespaceAndPath(CaptureSphereMod.MOD_ID, "captured_mob"),
        DataComponentType.<CapturedMobData>builder()
            .persistent(CapturedMobData.CODEC)
            .networkSynchronized(CapturedMobData.STREAM_CODEC)
            .build()
    );

    public static void initialize() {
        // クラス読み込み時に静的フィールドの登録処理を実行
    }
}
