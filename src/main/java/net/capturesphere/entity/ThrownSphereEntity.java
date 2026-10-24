package net.capturesphere.entity;

import net.capturesphere.component.CapturedMobData;
import net.capturesphere.init.ModDataComponentTypes;
import net.capturesphere.init.ModEntities;
import net.capturesphere.init.ModItems;
import net.capturesphere.item.AbstractSphereItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntitySpawnRequest;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Optional;

/**
 * 投擲されたキャプチャースフィアのエンティティ (Minecraft 26.3 Mojang Mappings)。
 */
public class ThrownSphereEntity extends ThrowableItemProjectile {

    public ThrownSphereEntity(EntityType<? extends ThrownSphereEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownSphereEntity(Level level, LivingEntity owner, ItemStack itemStack) {
        super(ModEntities.THROWN_SPHERE, owner, level, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.COPPER_SPHERE;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (this.level().isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) this.level();
        Entity hitEntity = entityHitResult.getEntity();
        ItemStack sphereStack = this.getItem();

        // モブが既に格納されているスフィアの場合は、対象の足元にモブを召喚
        if (sphereStack.has(ModDataComponentTypes.CAPTURED_MOB) && sphereStack.has(DataComponents.CUSTOM_DATA)) {
            spawnCapturedEntity(serverLevel, sphereStack, hitEntity.getX(), hitEntity.getY(), hitEntity.getZ());
            this.discard();
            return;
        }

        // 空のスフィアがLivingEntityに命中した場合の捕獲処理
        if (!(hitEntity instanceof LivingEntity target)) {
            return;
        }

        if (!(sphereStack.getItem() instanceof AbstractSphereItem sphereItem)) {
            return;
        }

        // 対象モブの適格性判定
        if (!sphereItem.canCapture(target)) {
            serverLevel.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.DISPENSER_FAIL,
                SoundSource.NEUTRAL,
                1.0F,
                1.2F
            );
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 12, 0.2, 0.2, 0.2, 0.05);

            ItemStack emptyDrop = new ItemStack(sphereItem);
            ItemEntity drop = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), emptyDrop);
            drop.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(drop);

            this.discard();
            return;
        }

        // 捕獲確率の判定
        float chance = sphereItem.calculateCaptureChance(target);
        if (this.random.nextFloat() <= chance) {
            // --- 捕獲成功 ---
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, target.registryAccess());
            target.save(output);
            CompoundTag entityTag = output.buildResult();

            Identifier entityId = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
            entityTag.putString("id", entityId.toString());

            ItemStack capturedStack = new ItemStack(sphereItem);
            capturedStack.set(DataComponents.CUSTOM_DATA, CustomData.of(entityTag));
            capturedStack.set(ModDataComponentTypes.CAPTURED_MOB, new CapturedMobData(
                entityId,
                Optional.ofNullable(target.getCustomName()),
                target.getHealth(),
                target.getMaxHealth()
            ));

            if (this.getOwner() instanceof Player player) {
                sphereItem.onCaptureSuccess(player, target);
            }

            serverLevel.playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.RESPAWN_ANCHOR_CHARGE,
                SoundSource.NEUTRAL,
                1.0F,
                1.4F
            );
            serverLevel.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY() + 0.5, target.getZ(), 35, 0.3, 0.5, 0.3, 0.4);
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, target.getX(), target.getY() + 0.5, target.getZ(), 15, 0.3, 0.3, 0.3, 0.1);

            target.discard();

            ItemEntity drop = new ItemEntity(serverLevel, target.getX(), target.getY() + 0.5, target.getZ(), capturedStack);
            drop.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(drop);
        } else {
            // --- 捕獲失敗 ---
            serverLevel.playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.ITEM_BREAK,
                SoundSource.NEUTRAL,
                1.0F,
                0.9F
            );
            serverLevel.sendParticles(ParticleTypes.POOF, this.getX(), this.getY(), this.getZ(), 20, 0.2, 0.2, 0.2, 0.08);
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 10, 0.2, 0.2, 0.2, 0.05);
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (this.level().isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) this.level();
        ItemStack sphereStack = this.getItem();

        if (sphereStack.has(ModDataComponentTypes.CAPTURED_MOB) && sphereStack.has(DataComponents.CUSTOM_DATA)) {
            BlockPos hitPos = blockHitResult.getBlockPos();
            Direction side = blockHitResult.getDirection();
            BlockPos spawnPos = hitPos.relative(side);

            double spawnX = spawnPos.getX() + 0.5;
            double spawnY = spawnPos.getY();
            double spawnZ = spawnPos.getZ() + 0.5;

            spawnCapturedEntity(serverLevel, sphereStack, spawnX, spawnY, spawnZ);
        } else {
            ItemStack emptySphere = new ItemStack(sphereStack.getItem());
            ItemEntity drop = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), emptySphere);
            drop.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(drop);
        }

        this.discard();
    }

    private void spawnCapturedEntity(ServerLevel serverLevel, ItemStack sphereStack, double x, double y, double z) {
        CustomData customData = sphereStack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return;
        }

        CompoundTag tag = customData.copyTag();
        tag.remove("UUID");

        EntitySpawnRequest spawnRequest = new EntitySpawnRequest(EntitySpawnReason.TRIGGERED, false);
        Entity restoredEntity = EntityType.loadEntityRecursive(tag, serverLevel, spawnRequest, entity -> {
            entity.setPos(x, y, z);
            entity.setYRot(this.getYRot());
            return entity;
        });

        if (restoredEntity != null) {
            serverLevel.addFreshEntity(restoredEntity);

            serverLevel.playSound(
                null,
                x,
                y,
                z,
                SoundEvents.PLAYER_TELEPORT,
                SoundSource.NEUTRAL,
                0.8F,
                1.2F
            );
            serverLevel.sendParticles(ParticleTypes.POOF, x, y + 0.5, z, 20, 0.3, 0.4, 0.3, 0.05);
            serverLevel.sendParticles(ParticleTypes.FIREWORK, x, y + 0.5, z, 15, 0.2, 0.3, 0.2, 0.05);

            ItemStack emptySphere = new ItemStack(sphereStack.getItem());
            ItemEntity drop = new ItemEntity(serverLevel, x, y + 0.5, z, emptySphere);
            drop.setDefaultPickUpDelay();
            serverLevel.addFreshEntity(drop);
        }
    }
}
