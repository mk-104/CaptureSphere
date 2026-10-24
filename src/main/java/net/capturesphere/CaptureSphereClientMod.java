package net.capturesphere;

import net.capturesphere.init.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

/**
 * Capture Sphere Mod のクライアント初期化クラス (Minecraft 26.3)。
 * 飛んでいるスフィアのエンティティレンダラーを登録する。
 */
@Environment(EnvType.CLIENT)
public class CaptureSphereClientMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // 飛んでいるスフィア (ThrownSphereEntity) をアイテムレンダラーで描画
        EntityRendererRegistry.register(ModEntities.THROWN_SPHERE, ThrownItemRenderer::new);
    }
}
