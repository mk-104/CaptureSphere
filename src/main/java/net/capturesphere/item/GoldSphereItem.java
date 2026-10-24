package net.capturesphere.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;

/**
 * 金のスフィア (Gold Sphere) - Minecraft 26.3
 * - 対象: 村人（Villager）専用。
 * - 捕獲率: 100%成功。
 * - 特殊効果: 捕獲成功時、投げたプレイヤーに「不吉な予感（Bad Omen）」エフェクトを付与する。
 */
public class GoldSphereItem extends AbstractSphereItem {

    public GoldSphereItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canCapture(LivingEntity target) {
        return target instanceof Villager;
    }

    @Override
    public float calculateCaptureChance(LivingEntity target) {
        return 1.0F;
    }

    @Override
    public void onCaptureSuccess(Player thrower, LivingEntity target) {
        thrower.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 12000, 0));
    }
}
