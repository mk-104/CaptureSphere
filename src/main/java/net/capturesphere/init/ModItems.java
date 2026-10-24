package net.capturesphere.init;

import net.capturesphere.CaptureSphereMod;
import net.capturesphere.item.CopperSphereItem;
import net.capturesphere.item.GoldSphereItem;
import net.capturesphere.item.IronSphereItem;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.function.Function;

/**
 * Mod内のアイテムを登録・管理するクラス (Minecraft 26.3)。
 */
public class ModItems {

    public static final ResourceKey<Item> COPPER_SPHERE_KEY = ResourceKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath(CaptureSphereMod.MOD_ID, "copper_sphere")
    );

    public static final ResourceKey<Item> IRON_SPHERE_KEY = ResourceKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath(CaptureSphereMod.MOD_ID, "iron_sphere")
    );

    public static final ResourceKey<Item> GOLD_SPHERE_KEY = ResourceKey.create(
        Registries.ITEM,
        Identifier.fromNamespaceAndPath(CaptureSphereMod.MOD_ID, "gold_sphere")
    );

    public static final CopperSphereItem COPPER_SPHERE = registerItem(
        COPPER_SPHERE_KEY,
        CopperSphereItem::new
    );

    public static final IronSphereItem IRON_SPHERE = registerItem(
        IRON_SPHERE_KEY,
        IronSphereItem::new
    );

    public static final GoldSphereItem GOLD_SPHERE = registerItem(
        GOLD_SPHERE_KEY,
        GoldSphereItem::new
    );

    private static <T extends Item> T registerItem(ResourceKey<Item> key, Function<Item.Properties, T> factory) {
        Item.Properties properties = new Item.Properties().setId(key).stacksTo(16);
        T item = factory.apply(properties);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void initialize() {
        ResourceKey<CreativeModeTab> toolsTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB,
            Identifier.withDefaultNamespace("tools_and_utilities")
        );
        CreativeModeTabEvents.modifyOutputEvent(toolsTab).register(output -> {
            output.accept(COPPER_SPHERE);
            output.accept(IRON_SPHERE);
            output.accept(GOLD_SPHERE);
        });
    }
}
