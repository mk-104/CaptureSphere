package net.capturesphere.item;

import net.capturesphere.component.CapturedMobData;
import net.capturesphere.entity.ThrownSphereEntity;
import net.capturesphere.init.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * キャプチャースフィアアイテムの抽象基底クラス (Minecraft 26.3 Mojang Mappings)。
 * 投擲処理、ツールチップ表示、および捕獲ルールの抽象メソッドを定義。
 */
public abstract class AbstractSphereItem extends Item {

    public AbstractSphereItem(Properties properties) {
        super(properties);
    }

    /**
     * 右クリック投擲処理。雪玉のように前方に ThrownSphereEntity を射出する。
     */
    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        level.playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.SNOWBALL_THROW,
            SoundSource.PLAYERS,
            0.5F,
            0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F)
        );

        if (!level.isClientSide()) {
            ThrownSphereEntity sphereEntity = new ThrownSphereEntity(level, player, itemStack);
            sphereEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(sphereEntity);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * モブが格納されている場合はアイテムスロットで光彩エフェクト（エンチャントの輝き）を付与する。
     */
    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(ModDataComponentTypes.CAPTURED_MOB) || super.isFoil(stack);
    }

    /**
     * ツールチップ表示処理 (Minecraft 26.3)。
     * 格納されたモブの種類、カスタム名、HP情報をフォーマットして表示する。
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);

        CapturedMobData data = stack.get(ModDataComponentTypes.CAPTURED_MOB);
        if (data != null) {
            // 1. モブの種類（翻訳キー）
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(data.entityId());
            Component mobName = entityType != null ? entityType.getDescription() : Component.literal(data.entityId().toString());
            tooltip.accept(Component.translatable("tooltip.capturesphere.mob_type", mobName).withStyle(ChatFormatting.AQUA));

            // 2. カスタム名（付与されている場合）
            data.customName().ifPresent(name -> {
                tooltip.accept(Component.translatable("tooltip.capturesphere.custom_name", name).withStyle(ChatFormatting.YELLOW));
            });

            // 3. 残りHP / 最大HP（例: "HP: 15.0 / 20.0"）
            String hpText = String.format("%.1f / %.1f", data.currentHealth(), data.maxHealth());
            tooltip.accept(Component.translatable("tooltip.capturesphere.health", hpText).withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.accept(Component.translatable("tooltip.capturesphere.empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public abstract boolean canCapture(LivingEntity target);

    public abstract float calculateCaptureChance(LivingEntity target);

    public void onCaptureSuccess(Player thrower, LivingEntity target) {
        // サブクラスで必要に応じてオーバーライド
    }

    protected boolean isBoss(LivingEntity target) {
        return target instanceof EnderDragon || target instanceof WitherBoss || target instanceof Warden;
    }
}
