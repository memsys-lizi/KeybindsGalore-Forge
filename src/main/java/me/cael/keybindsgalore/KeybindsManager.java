package me.cael.keybindsgalore;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import me.cael.keybindsgalore.mixin.AccessorKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyMappingLookup;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeybindsManager {

    private static final Map<InputConstants.Key, List<KeyMapping>> conflictingKeys = Maps.newHashMap();

    /**
     * Mapping that should be the only match while a synthetic input event is posted.
     * Epic Fight 19.5 ({@code ControllEngine.Events.keyboardEvent}) walks
     * {@code KeyMappingLookup.getAll} and toggles combat mode on {@code GLFW_PRESS}
     * when no screen is open. It does not call {@code consumeClick}.
     */
    private static KeyMapping solo;

    private static KeyMapping held;
    private static int fullTicksHeld;
    private static boolean releaseOnNextStart;

    public static boolean handleConflict(InputConstants.Key key) {
        if (!KeybindsGaloreConfig.isEnabled()) {
            conflictingKeys.remove(key);
            return false;
        }
        List<KeyMapping> matches = new ArrayList<>();
        KeyMapping[] keysAll = Minecraft.getInstance().options.keyMappings;
        for (KeyMapping bind : keysAll) {
            if (bindingMatches(bind, key)) {
                matches.add(bind);
            }
        }
        // A protected gameplay binding on this key must keep working. Skip the
        // pie menu and let Forge deliver the event the way it does without this mod.
        for (KeyMapping bind : matches) {
            if (KeybindsGaloreConfig.isExcluded(bind.getName(), bind.getCategory())) {
                KeybindsManager.conflictingKeys.remove(key);
                return false;
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

    public static void clearConflicts() {
        conflictingKeys.clear();
    }

    public static KeyMapping soloTarget() {
        return solo;
    }

    /**
     * Other bindings on the chosen mapping's key should not match for the duration of the synthetic event.
     */
    public static boolean suppresses(KeyMapping mapping) {
        KeyMapping chosen = solo;
        if (chosen == null || mapping == null || mapping == chosen) {
            return false;
        }
        InputConstants.Key chosenKey = chosen.getKey();
        return chosenKey != null && chosenKey.equals(mapping.getKey());
    }

    /**
     * Press the chosen binding the way mods actually observe it.
     * The click count goes up by one (for {@code consumeClick}), the mapping stays down for two
     * full client ticks (for {@code isDown} polls), and a Forge input event is posted while only
     * this mapping matches (for {@code InputEvent.Key} / {@code InputEvent.MouseButton} listeners).
     */
    public static void activateSelection(KeyMapping mapping) {
        if (mapping == null) {
            return;
        }
        if (held != null) {
            releaseHeld();
        }
        AccessorKeyMapping access = (AccessorKeyMapping) mapping;
        access.setClickCount(access.getClickCount() + 1);
        access.setDown(true);
        held = mapping;
        fullTicksHeld = 0;
        releaseOnNextStart = false;
        dispatch(mapping, GLFW.GLFW_PRESS);
    }

    public static void onClientTickStart() {
        if (held != null && releaseOnNextStart) {
            releaseHeld();
            return;
        }
        if (held != null) {
            fullTicksHeld++;
        }
    }

    public static void onClientTickEnd() {
        if (held != null && fullTicksHeld >= 2) {
            releaseOnNextStart = true;
        }
    }

    private static void releaseHeld() {
        KeyMapping mapping = held;
        held = null;
        fullTicksHeld = 0;
        releaseOnNextStart = false;
        if (mapping == null) {
            return;
        }
        ((AccessorKeyMapping) mapping).setDown(false);
        dispatch(mapping, GLFW.GLFW_RELEASE);
    }

    private static void dispatch(KeyMapping chosen, int action) {
        InputConstants.Key bound = chosen.getKey();
        if (bound == null || bound.equals(InputConstants.UNKNOWN)) {
            return;
        }
        List<KeyMapping> hidden = new ArrayList<>();
        List<InputConstants.Key> hiddenKeys = new ArrayList<>();
        List<Boolean> hiddenInLookup = new ArrayList<>();
        KeyMappingLookup lookup = AccessorKeyMapping.getLookup();
        KeyMapping previous = solo;
        try {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.options != null) {
                for (KeyMapping other : minecraft.options.keyMappings) {
                    if (other == null || other == chosen || !bound.equals(other.getKey())) {
                        continue;
                    }
                    boolean inLookup = false;
                    for (KeyMapping existing : lookup.getAll(other.getKey())) {
                        if (existing == other) {
                            inLookup = true;
                            break;
                        }
                    }
                    hidden.add(other);
                    hiddenKeys.add(other.getKey());
                    hiddenInLookup.add(inLookup);
                    if (inLookup) {
                        lookup.remove(other);
                    }
                    ((AccessorKeyMapping) other).setBoundKey(InputConstants.UNKNOWN);
                }
            }
            solo = chosen;
            if (bound.getType() == InputConstants.Type.MOUSE) {
                MinecraftForge.EVENT_BUS.post(new InputEvent.MouseButton.Post(bound.getValue(), action, 0));
            } else if (bound.getType() == InputConstants.Type.SCANCODE) {
                MinecraftForge.EVENT_BUS.post(new InputEvent.Key(GLFW.GLFW_KEY_UNKNOWN, bound.getValue(), action, 0));
            } else {
                MinecraftForge.EVENT_BUS.post(new InputEvent.Key(bound.getValue(), 0, action, 0));
            }
        } finally {
            solo = previous;
            for (int i = 0; i < hidden.size(); i++) {
                KeyMapping other = hidden.get(i);
                InputConstants.Key saved = hiddenKeys.get(i);
                ((AccessorKeyMapping) other).setBoundKey(saved);
                if (hiddenInLookup.get(i)) {
                    lookup.put(saved, other);
                }
            }
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
