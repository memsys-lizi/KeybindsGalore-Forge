package me.cael.keybindsgalore;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeybindsManager {

    private static final Map<InputConstants.Key, List<KeyMapping>> conflictingKeys = Maps.newHashMap();

    public static boolean handleConflict(InputConstants.Key key) {
        List<KeyMapping> matches = new ArrayList<>();
        KeyMapping[] keysAll = Minecraft.getInstance().options.keyMappings;
        for (KeyMapping bind : keysAll) {
            if (bindingMatches(bind, key)) {
                matches.add(bind);
            }
        }
        if (matches.size() > 1) {
            KeybindsManager.conflictingKeys.put(key, matches);
            return true;
        } else {
            KeybindsManager.conflictingKeys.remove(key);
            return false;
        }
    }

    public static boolean isConflicting(InputConstants.Key key) {
        return conflictingKeys.containsKey(key);
    }

    public static void openConflictMenu(InputConstants.Key key) {
        KeybindsScreen screen = new KeybindsScreen();
        screen.setConflictedKey(key);
        Minecraft.getInstance().setScreen(screen);
    }

    public static List<KeyMapping> getConflicting(InputConstants.Key key) {
        return conflictingKeys.get(key);
    }

    public static void onKeyReleased(InputConstants.Key key) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen instanceof KeybindsScreen screen && key.equals(screen.getConflictedKey())) {
            screen.confirmSelection();
        }
    }

    private static KeyMapping pendingRelease;

    public static void armRelease(KeyMapping bind) {
        pendingRelease = bind;
    }

    public static void finishPending() {
        if (pendingRelease != null) {
            pendingRelease.setDown(false);
            pendingRelease = null;
        }
    }

    private static boolean bindingMatches(KeyMapping bind, InputConstants.Key key) {
        if (key.getType() == InputConstants.Type.MOUSE) {
            return bind.matchesMouse(key.getValue());
        }
        if (key.getType() == InputConstants.Type.SCANCODE) {
            return bind.matches(InputConstants.UNKNOWN.getValue(), key.getValue());
        }
        return bind.matches(key.getValue(), InputConstants.UNKNOWN.getValue());
    }
}
