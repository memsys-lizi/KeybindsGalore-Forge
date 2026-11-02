package me.cael.keybindsgalore.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("clickCount")
    void setClickCount(int clickCount);

    @Accessor("isDown")
    void setDown(boolean down);
}
