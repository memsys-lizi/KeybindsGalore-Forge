package me.cael.keybindsgalore;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KeybindsGaloreMod.MOD_ID, value = Dist.CLIENT)
public final class ClientEvents {
    private ClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            KeybindsManager.onClientTickStart();
            return;
        }
        KeybindsManager.onClientTickEnd();
        pollToggle();
    }

    private static void pollToggle() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }
        while (ClientKeybinds.TOGGLE_PIE.consumeClick()) {
            boolean enabled = KeybindsGaloreConfig.toggleEnabled();
            if (!enabled) {
                KeybindsManager.clearConflicts();
            }
            minecraft.player.displayClientMessage(Component.translatable(
                    enabled ? "keybindsgalore.message.enabled" : "keybindsgalore.message.disabled"
            ).withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED), true);
        }
    }
}
