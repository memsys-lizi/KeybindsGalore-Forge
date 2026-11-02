package me.cael.keybindsgalore.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import me.cael.keybindsgalore.KeybindsManager;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = KeyMapping.class, priority = -5000)
public abstract class MixinKeyMapping {

    @Shadow
    private InputConstants.Key key;

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private static void keybindsgalore$set(InputConstants.Key key, boolean value, CallbackInfo ci) {
        if (value) {
            boolean conflicting = KeybindsManager.handleConflict(key);
            if (conflicting) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "set", at = @At("RETURN"))
    private static void keybindsgalore$setReturn(InputConstants.Key key, boolean value, CallbackInfo ci) {
        if (!value) {
            KeybindsManager.onKeyReleased(key);
        }
    }

    @Inject(method = "click", at = @At("HEAD"), cancellable = true)
    private static void keybindsgalore$click(InputConstants.Key key, CallbackInfo ci) {
        // Re-check here so a gameplay exclusion is honored even when click runs
        // before set, and so a stale conflict flag cannot still open the menu.
        if (KeybindsManager.handleConflict(key)) {
            ci.cancel();
            KeybindsManager.openConflictMenu(key);
        }
    }

    @Inject(method = "setDown", at = @At("HEAD"), cancellable = true)
    private void keybindsgalore$setDown(boolean value, CallbackInfo ci) {
        if (value && KeybindsManager.isConflicting(this.key)) {
            ci.cancel();
        }
    }
}
