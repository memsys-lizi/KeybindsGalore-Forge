# Keybinds Galore (Forge)

A **Minecraft Forge 1.19.2** client-side port of [Keybinds Galore](https://github.com/CaelTheColher/KeybindsGalore) (Fabric) by Cael.

When you press a key that is bound to **two or more** actions, the mod opens a radial (pie) menu so you can choose which binding to activate. Keys with a single binding behave exactly as vanilla.

Core gameplay controls are the exception. Movement, jump, sneak, sprint, attack, use, and the other bindings in the client exclusion list never open the pie menu, even when they share a key with something else. Those keys behave like vanilla Forge.

- **Mod ID:** `keybindsgalore`
- **Display name:** Keybinds Galore (Forge)
- **Version:** 1.0.1
- **Minecraft:** 1.19.2
- **Forge:** 43.x (built against 43.3.0)
- **Java:** 17
- **Side:** client only

## How it works

1. A key with one binding fires that binding as usual.
2. A key with two or more bindings does not open the pie menu when any of those bindings is excluded (see below). Otherwise a pie menu opens while the key is held.
3. Move the mouse over the action you want, then release the key. That binding is pressed once.
4. Release the key without a selection (or while the pointer is still in the center) and nothing is activated.

The radial menu rendering is adapted from [Psi](https://github.com/Vazkii/Psi) by Vazkii (see the comment in `KeybindsScreen`).

## Gameplay controls stay vanilla

Pressing forward, back, left, right, jump, sneak, sprint, attack, or use must keep working when another action is bound to the same key. A pie menu on those inputs blocks movement and combat, so they are excluded.

If the physical key you press matches **any** binding in the exclusion list, this mod does not open the pie menu and does not cancel the event. Forge then handles the key the way it does without this mod: every binding on that key is held, and one of them receives the click. The exclusion follows the binding, not the keycap. `key.jump` stays excluded if you move jump from Space to another key.

The list is the client config `config/keybindsgalore-client.toml`, created the first time you launch the game. In a normal install that file is `.minecraft/config/keybindsgalore-client.toml`. Close the game, edit the file, then start the game again.

```toml
[gameplay]
	#Binding and category ids that keep vanilla behavior.
	#Defaults: movement (forward, back, left, right, jump, sneak, sprint),
	#gameplay mouse buttons (attack, use, pick block), and hotbar slots 1-9.
	excludedKeybinds = ["key.forward", "key.back", "key.left", "key.right", "key.jump", "key.sneak", "key.sprint", "key.attack", "key.use", "key.pickItem", "key.hotbar.1", "key.hotbar.2", "key.hotbar.3", "key.hotbar.4", "key.hotbar.5", "key.hotbar.6", "key.hotbar.7", "key.hotbar.8", "key.hotbar.9"]
```

- An entry is a binding id: the name registered with `KeyMapping`, which is also the translation key, such as `key.jump`.
- An entry that is a category id, such as `key.categories.movement` or `key.categories.gameplay`, excludes every binding in that category, including bindings other mods add to it.
- Delete a line to let that binding open the pie menu again.
- An empty list (`excludedKeybinds = []`) turns exclusions off, so every conflict opens the pie menu, including movement and the mouse buttons.
- Drop (`key.drop`) and Swap Offhand (`key.swapOffhand`) are not in the default list, so binding those two to one key still opens the pie menu.

| Id | Action |
| --- | --- |
| `key.forward` | Walk forward |
| `key.back` | Walk backward |
| `key.left` | Strafe left |
| `key.right` | Strafe right |
| `key.jump` | Jump |
| `key.sneak` | Sneak |
| `key.sprint` | Sprint |
| `key.attack` | Attack / destroy (left mouse) |
| `key.use` | Use item / place block (right mouse) |
| `key.pickItem` | Pick block (middle mouse) |
| `key.hotbar.1` … `key.hotbar.9` | Hotbar slots 1–9 |

Pick block and the hotbar slots are included because they are used the same way during play as attack, use, and movement. Delete those lines if you want a pie menu for them. Other ids you may want to add yourself include `key.drop`, `key.swapOffhand`, and `key.inventory`.

## Install

1. Install [Minecraft 1.19.2](https://www.minecraft.net/) and [Minecraft Forge 1.19.2-43.x](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.2.html) (43.3.0 or another 43.x build).
2. Launch the game once with the Forge profile so the `mods` folder is created.
3. Copy `keybindsgalore-forge-1.0.1+1.19.2.jar` from `build/libs/` (or a release) into `.minecraft/mods/`.
4. Start Minecraft with the Forge 1.19.2 profile. The mod is client-only; a dedicated server does not need it.

To try the pie menu: open **Options → Controls** and bind two different actions to the same key (for example Drop and Swap Offhand). Press that key in a world. The pie menu should appear. Binding a second action to W, Space, Shift, Ctrl, or the left or right mouse button should **not** open the menu; that gameplay control keeps working.

## Build from source

Requires JDK 17 and a network connection (ForgeGradle downloads Minecraft and Forge).

```bash
# Debian/Ubuntu
sudo apt-get install openjdk-17-jdk

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew build
```

The reobfuscated mod jar is written to:

```
build/libs/keybindsgalore-forge-1.0.1+1.19.2.jar
```

## Credits

- **Original mod (Fabric):** [Cael](https://github.com/CaelTheColher) — [Keybinds Galore](https://github.com/CaelTheColher/KeybindsGalore), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/keybinds)
- **Radial menu UI:** based on code from [Psi](https://github.com/Vazkii/Psi) by Vazkii ([Psi license](https://psi.vazkii.net/license.php))
- **This repository:** Forge 1.19.2 port of that behavior

## License

GNU Lesser General Public License v3.0 (LGPL-3.0), same family as the original Keybinds Galore project.

Copyright (c) 2021 Cael. The Forge port is a derivative work and remains under LGPL-3.0. See [LICENSE](LICENSE).

The pie-menu drawing in `KeybindsScreen` is modified from Psi. Psi is open source under the [Psi License](https://psi.vazkii.net/license.php).

---

# 键位盛宴（Forge 移植）

这是 Fabric 模组 [Keybinds Galore](https://github.com/CaelTheColher/KeybindsGalore)（作者 Cael）的 **Minecraft Forge 1.19.2** 客户端移植。

当某个按键同时绑定了 **两个或更多** 功能时，按下该键会弹出饼状（径向）菜单，让你选择要触发的那一个。只绑定了一个功能的按键与原版行为相同。

核心游戏操作是例外。移动、跳跃、潜行、疾跑、攻击、使用，以及客户端排除列表中的其他绑定，即使和别的功能共用同一个键，也不会弹出饼状菜单，而是保持原版 Forge 的行为。

- **模组 ID：** `keybindsgalore`
- **显示名称：** Keybinds Galore (Forge)
- **版本：** 1.0.1
- **Minecraft：** 1.19.2
- **Forge：** 43.x（针对 43.3.0 构建）
- **Java：** 17
- **运行端：** 仅客户端

## 行为说明

1. 只绑定一个功能的按键，照常触发。
2. 绑定了两个及以上功能的按键，如果其中任意一个在排除列表中（见下文），则不打开饼状菜单；否则在按住时打开饼状菜单。
3. 把鼠标移到想要的功能上，然后松开按键，该绑定会被按下一次。
4. 没有选中任何扇区就松开按键（或指针仍在圆心）则不触发任何功能。

径向菜单的绘制改编自 Vazkii 的 [Psi](https://github.com/Vazkii/Psi)（见 `KeybindsScreen` 中的注释）。

## 游戏操作保持原版行为

前进、后退、左移、右移、跳跃、潜行、疾跑、攻击和使用，在同一个键上还绑了其他功能时也必须能正常使用。这些输入一旦弹出饼状菜单，移动和战斗就会被打断，因此默认把它们排除在外。

如果按下的物理按键匹配到排除列表中的 **任意一个** 绑定，本模组不会打开饼状菜单，也不会取消这次按键。Forge 会按「没有本模组拦截」时的方式处理：该键上的每一个绑定都会处于按住状态，并且其中的一个会收到点击。排除的是绑定本身，不是键帽。把跳跃从空格改到别的键后，`key.jump` 仍然被排除。

列表写在客户端配置 `config/keybindsgalore-client.toml` 中，第一次启动游戏时生成。常规安装下的路径是 `.minecraft/config/keybindsgalore-client.toml`。请先关闭游戏，改完文件后再启动。

```toml
[gameplay]
	#Binding and category ids that keep vanilla behavior.
	#Defaults: movement (forward, back, left, right, jump, sneak, sprint),
	#gameplay mouse buttons (attack, use, pick block), and hotbar slots 1-9.
	excludedKeybinds = ["key.forward", "key.back", "key.left", "key.right", "key.jump", "key.sneak", "key.sprint", "key.attack", "key.use", "key.pickItem", "key.hotbar.1", "key.hotbar.2", "key.hotbar.3", "key.hotbar.4", "key.hotbar.5", "key.hotbar.6", "key.hotbar.7", "key.hotbar.8", "key.hotbar.9"]
```

- 每一项是绑定 id，也就是注册 `KeyMapping` 时使用的名字（同时是翻译键），例如 `key.jump`。
- 如果填写的是分类 id，例如 `key.categories.movement` 或 `key.categories.gameplay`，则该分类下的所有绑定都会被排除，包括其他模组注册到这个分类里的绑定。
- 删掉某一行后，该绑定可以再次打开饼状菜单。
- 空列表（`excludedKeybinds = []`）会关闭全部排除。此时任何冲突都会打开饼状菜单，包括移动键和鼠标键。
- 丢弃（`key.drop`）和切换副手（`key.swapOffhand`）不在默认列表中，因此把这两个功能绑到同一个键仍然会打开饼状菜单。

| Id | 功能 |
| --- | --- |
| `key.forward` | 向前移动 |
| `key.back` | 向后移动 |
| `key.left` | 向左平移 |
| `key.right` | 向右平移 |
| `key.jump` | 跳跃 |
| `key.sneak` | 潜行 |
| `key.sprint` | 疾跑 |
| `key.attack` | 攻击 / 破坏（鼠标左键） |
| `key.use` | 使用物品 / 放置方块（鼠标右键） |
| `key.pickItem` | 选取方块（鼠标中键） |
| `key.hotbar.1` … `key.hotbar.9` | 快捷栏 1–9 |

选取方块和快捷栏与攻击、使用、移动一样，都是游戏过程中会持续用到的操作，因此默认一并排除。如果希望它们弹出饼状菜单，删掉对应的行即可。你也可以自行加入 `key.drop`、`key.swapOffhand`、`key.inventory` 等 id。

## 安装

1. 安装 [Minecraft 1.19.2](https://www.minecraft.net/) 与 [Minecraft Forge 1.19.2-43.x](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.2.html)（43.3.0 或其他 43.x 版本）。
2. 用 Forge 配置启动一次游戏，以便生成 `mods` 文件夹。
3. 将 `build/libs/`（或发布包）中的 `keybindsgalore-forge-1.0.1+1.19.2.jar` 复制到 `.minecraft/mods/`。
4. 使用 Forge 1.19.2 配置启动游戏。本模组仅客户端需要，专用服务器不必安装。

试用饼状菜单：打开 **选项 → 控制**，把两个不同的操作绑到同一个键（例如丢弃与切换副手）。在世界中按下该键，应出现饼状菜单。若把另一个功能绑到 W、空格、Shift、Ctrl 或鼠标左键 / 右键，则 **不应** 出现菜单，对应的游戏操作仍可正常使用。

## 从源码构建

需要 JDK 17，以及可访问网络的环境（ForgeGradle 会下载 Minecraft 与 Forge）。

```bash
# Debian/Ubuntu
sudo apt-get install openjdk-17-jdk

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
./gradlew build
```

混淆后的模组 jar 位于：

```
build/libs/keybindsgalore-forge-1.0.1+1.19.2.jar
```

## 致谢

- **原版模组（Fabric）：** [Cael](https://github.com/CaelTheColher) — [Keybinds Galore](https://github.com/CaelTheColher/KeybindsGalore)，[CurseForge](https://www.curseforge.com/minecraft/mc-mods/keybinds)
- **径向菜单界面：** 基于 Vazkii 的 [Psi](https://github.com/Vazkii/Psi)（[Psi 许可证](https://psi.vazkii.net/license.php)）
- **本仓库：** 上述行为的 Forge 1.19.2 移植

## 许可证

GNU 宽通用公共许可证第 3 版（LGPL-3.0），与原版 Keybinds Galore 相同。

Copyright (c) 2021 Cael。本 Forge 移植为衍生作品，同样以 LGPL-3.0 发布。详见 [LICENSE](LICENSE)。

`KeybindsScreen` 中的饼状菜单绘制修改自 Psi。Psi 以 [Psi License](https://psi.vazkii.net/license.php) 开源。
