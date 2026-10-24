package net.capturesphere.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.Optional;

/**
 * スフィアに格納されたモブの概要情報を保持するデータコンポーネント。
 * Minecraft 26.3 の Data Component システム（Mojang Mappings）に準拠。
 */
public record CapturedMobData(
    Identifier entityId,
    Optional<Component> customName,
    float currentHealth,
    float maxHealth
) {
    public static final Codec<CapturedMobData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Identifier.CODEC.fieldOf("entity_id").forGetter(CapturedMobData::entityId),
            ComponentSerialization.CODEC.optionalFieldOf("custom_name").forGetter(CapturedMobData::customName),
            Codec.FLOAT.fieldOf("current_health").forGetter(CapturedMobData::currentHealth),
            Codec.FLOAT.fieldOf("max_health").forGetter(CapturedMobData::maxHealth)
        ).apply(instance, CapturedMobData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CapturedMobData> STREAM_CODEC = StreamCodec.composite(
        Identifier.STREAM_CODEC, CapturedMobData::entityId,
        ComponentSerialization.OPTIONAL_STREAM_CODEC, CapturedMobData::customName,
        ByteBufCodecs.FLOAT, CapturedMobData::currentHealth,
        ByteBufCodecs.FLOAT, CapturedMobData::maxHealth,
        CapturedMobData::new
    );
}
