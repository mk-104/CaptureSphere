package net.capturesphere.init;

import net.capturesphere.CaptureSphereMod;
import net.capturesphere.entity.ThrownSphereEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * Mod内のカスタムエンティティタイプを登録・管理するクラス (Minecraft 26.3)。
 */
public class ModEntities {

    public static final ResourceKey<EntityType<?>> THROWN_SPHERE_KEY = ResourceKey.create(
        Registries.ENTITY_TYPE,
        Identifier.fromNamespaceAndPath(CaptureSphereMod.MOD_ID, "thrown_sphere")
    );

    public static final EntityType<ThrownSphereEntity> THROWN_SPHERE = Registry.register(
        BuiltInRegistries.ENTITY_TYPE,
        THROWN_SPHERE_KEY,
        EntityType.Builder.<ThrownSphereEntity>of(ThrownSphereEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .build(THROWN_SPHERE_KEY)
    );

    public static void initialize() {
        // 静的フィールドの初期化トリガー
    }
}
