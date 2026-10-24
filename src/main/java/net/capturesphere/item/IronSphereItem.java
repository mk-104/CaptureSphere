package net.capturesphere.item;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * 鉄のスフィア (Iron Sphere) - Minecraft 26.3
 * - 対象: ボス（EnderDragon, WitherBoss, Warden等）を除くすべてのモブ。
 * - 捕獲率: 対象の最大HPと現在のHPから計算。(1.0 - (現在HP / 最大HP)) * 基礎確率。
 */
public class IronSphereItem extends AbstractSphereItem {

    private static final float BASE_CHANCE = 0.90F;

    public IronSphereItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canCapture(LivingEntity target) {
        return !isBoss(target);
    }

    @Override
    public float calculateCaptureChance(LivingEntity target) {
        float maxHealth = target.getMaxHealth();
        if (maxHealth <= 0.0F) {
            return 0.0F;
        }

        float currentHealth = target.getHealth();
        float healthRatio = Mth.clamp(currentHealth / maxHealth, 0.0F, 1.0F);

        float calculatedChance = (1.0F - healthRatio) * BASE_CHANCE + 0.05F;
        return Mth.clamp(calculatedChance, 0.0F, 1.0F);
    }
}
