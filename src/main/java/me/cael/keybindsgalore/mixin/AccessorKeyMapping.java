package me.cael.keybindsgalore.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyMappingLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface AccessorKeyMapping {
    @Accessor("clickCount")
    int getClickCount();

    @Accessor("clickCount")
    void setClickCount(int clickCount);

    @Accessor("isDown")
    void setDown(boolean down);

    @Accessor("key")
    void setBoundKey(InputConstants.Key key);

    @Accessor("MAP")
    static KeyMappingLookup getLookup() {
        throw new AssertionError();
    }
}
