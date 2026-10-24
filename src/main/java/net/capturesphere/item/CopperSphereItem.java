package net.capturesphere.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

/**
 * 銅のスフィア (Copper Sphere) - Minecraft 26.3
 * - 対象: 友好モブ・中立モブのみ（Enemyインターフェースを持たないもの）。
 * - 捕獲率: 100%成功。
 */
public class CopperSphereItem extends AbstractSphereItem {

    public CopperSphereItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canCapture(LivingEntity target) {
        // Enemyインターフェースを持たない友好モブ・中立モブのみ捕獲可能
        return !(target instanceof Enemy);
    }

    @Override
    public float calculateCaptureChance(LivingEntity target) {
        return 1.0F;
    }
}
