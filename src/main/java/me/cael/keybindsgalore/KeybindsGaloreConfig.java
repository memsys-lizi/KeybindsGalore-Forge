package me.cael.keybindsgalore;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Client config. Gameplay bindings listed here never open the pie menu, even
 * when another action is bound to the same physical key.
 */
public final class KeybindsGaloreConfig {
    public static final Client CLIENT;
    public static final ForgeConfigSpec SPEC;

    static {
        Pair<Client, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = pair.getLeft();
        SPEC = pair.getRight();
    }

    private KeybindsGaloreConfig() {
    }

    public static final class Client {
        public final ForgeConfigSpec.BooleanValue enabled;
        public final ForgeConfigSpec.ConfigValue<List<String>> excludedKeybinds;

        private Client(ForgeConfigSpec.Builder builder) {
            builder.comment(
                    "Master switch.",
                    "When false, Keybinds Galore does nothing: no pie menu, and keys behave like vanilla Forge.",
                    "The in-game \"Toggle pie menu\" control flips this value and saves it."
            ).push("general");
            enabled = builder
                    .comment("Set to false to disable the pie menu entirely.")
                    .define("enabled", true);
            builder.pop();

            builder.comment(
                    "Bindings listed below never open the pie menu.",
                    "If the key you press matches any excluded binding, the menu is skipped and Minecraft handles that key the way Forge does without this mod:",
                    "every binding on the key is held, and one of them receives the click.",
                    "Each entry is a binding id (the KeyMapping name / translation key, such as \"key.jump\") or a category id (such as \"key.categories.movement\").",
                    "A category id excludes every binding in that category, including bindings added by other mods.",
                    "Remove an entry to allow the pie menu for that binding again. An empty list disables every exclusion."
            ).push("gameplay");

            excludedKeybinds = builder
                    .comment(
                            "Binding and category ids that keep vanilla behavior.",
                            "Defaults: movement (forward, back, left, right, jump, sneak, sprint),",
                            "gameplay mouse buttons (attack, use, pick block), and hotbar slots 1-9."
                    )
                    .define("excludedKeybinds", KeybindsGaloreConfig::defaultExcludedIds, KeybindsGaloreConfig::isValidExclusionList);

            builder.pop();
        }
    }

    public static List<String> defaultExcludedIds() {
        return new ArrayList<>(List.of(
                "key.forward",
                "key.back",
                "key.left",
                "key.right",
                "key.jump",
                "key.sneak",
                "key.sprint",
                "key.attack",
                "key.use",
                "key.pickItem",
                "key.hotbar.1",
                "key.hotbar.2",
                "key.hotbar.3",
                "key.hotbar.4",
                "key.hotbar.5",
                "key.hotbar.6",
                "key.hotbar.7",
                "key.hotbar.8",
                "key.hotbar.9"
        ));
    }

    public static boolean isValidExclusionList(Object value) {
        if (!(value instanceof List<?> list)) {
            return false;
        }
        for (Object element : list) {
            if (!(element instanceof String text) || text.isBlank()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isExcluded(String bindingName, String category) {
        List<? extends String> ids = SPEC.isLoaded() ? CLIENT.excludedKeybinds.get() : defaultExcludedIds();
        if (ids == null) {
            return false;
        }
        for (String entry : ids) {
            if (entry == null) {
                continue;
            }
            String id = entry.trim();
            if (id.equals(bindingName) || id.equals(category)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnabled() {
        if (!SPEC.isLoaded()) {
            return true;
        }
        return CLIENT.enabled.get();
    }

    public static boolean toggleEnabled() {
        boolean next = !isEnabled();
        if (SPEC.isLoaded()) {
            CLIENT.enabled.set(next);
            CLIENT.enabled.save();
        }
        return next;
    }
}
