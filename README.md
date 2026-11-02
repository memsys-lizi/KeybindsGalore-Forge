# Keybinds Galore (Forge)

A **Minecraft Forge 1.19.2** client-side port of [Keybinds Galore](https://github.com/CaelTheColher/KeybindsGalore) (Fabric) by Cael.

When you press a key that is bound to **two or more** actions, the mod opens a radial (pie) menu so you can choose which binding to activate. Keys with a single binding behave exactly as vanilla.

- **Mod ID:** `keybindsgalore`
- **Display name:** Keybinds Galore (Forge)
- **Minecraft:** 1.19.2
- **Forge:** 43.x (built against 43.3.0)
- **Java:** 17
- **Side:** client only

## How it works

1. A key with one binding fires that binding as usual.
2. A key with two or more bindings does not fire them all. Instead a pie menu opens while the key is held.
3. Move the mouse over the action you want, then release the key. That binding is pressed once.
4. Release the key without a selection (or while the pointer is still in the center) and nothing is activated.

The radial menu rendering is adapted from [Psi](https://github.com/Vazkii/Psi) by Vazkii (see the comment in `KeybindsScreen`).

## Install

1. Install [Minecraft 1.19.2](https://www.minecraft.net/) and [Minecraft Forge 1.19.2-43.x](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.2.html) (43.3.0 or another 43.x build).
2. Launch the game once with the Forge profile so the `mods` folder is created.
3. Copy `keybindsgalore-forge-1.0.0+1.19.2.jar` from `build/libs/` (or a release) into `.minecraft/mods/`.
4. Start Minecraft with the Forge 1.19.2 profile. The mod is client-only; a dedicated server does not need it.

To try it: open **Options → Controls** and bind two different actions to the same key (for example Drop and Swap Offhand). Press that key in a world. The pie menu should appear.

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
build/libs/keybindsgalore-forge-1.0.0+1.19.2.jar
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

- **模组 ID：** `keybindsgalore`
- **显示名称：** Keybinds Galore (Forge)
- **Minecraft：** 1.19.2
- **Forge：** 43.x（针对 43.3.0 构建）
- **Java：** 17
- **运行端：** 仅客户端

## 行为说明

1. 只绑定一个功能的按键，照常触发。
2. 绑定了两个及以上功能的按键不会全部触发，而是在按住时打开饼状菜单。
3. 把鼠标移到想要的功能上，然后松开按键，该绑定会被按下一次。
4. 没有选中任何扇区就松开按键（或指针仍在圆心）则不触发任何功能。

径向菜单的绘制改编自 Vazkii 的 [Psi](https://github.com/Vazkii/Psi)（见 `KeybindsScreen` 中的注释）。

## 安装

1. 安装 [Minecraft 1.19.2](https://www.minecraft.net/) 与 [Minecraft Forge 1.19.2-43.x](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.19.2.html)（43.3.0 或其他 43.x 版本）。
2. 用 Forge 配置启动一次游戏，以便生成 `mods` 文件夹。
3. 将 `build/libs/`（或发布包）中的 `keybindsgalore-forge-1.0.0+1.19.2.jar` 复制到 `.minecraft/mods/`。
4. 使用 Forge 1.19.2 配置启动游戏。本模组仅客户端需要，专用服务器不必安装。

试用方法：打开 **选项 → 控制**，把两个不同的操作绑到同一个键（例如丢弃与切换副手）。在世界中按下该键，应出现饼状菜单。

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
build/libs/keybindsgalore-forge-1.0.0+1.19.2.jar
```

## 致谢

- **原版模组（Fabric）：** [Cael](https://github.com/CaelTheColher) — [Keybinds Galore](https://github.com/CaelTheColher/KeybindsGalore)，[CurseForge](https://www.curseforge.com/minecraft/mc-mods/keybinds)
- **径向菜单界面：** 基于 Vazkii 的 [Psi](https://github.com/Vazkii/Psi)（[Psi 许可证](https://psi.vazkii.net/license.php)）
- **本仓库：** 上述行为的 Forge 1.19.2 移植

## 许可证

GNU 宽通用公共许可证第 3 版（LGPL-3.0），与原版 Keybinds Galore 相同。

Copyright (c) 2021 Cael。本 Forge 移植为衍生作品，同样以 LGPL-3.0 发布。详见 [LICENSE](LICENSE)。

`KeybindsScreen` 中的饼状菜单绘制修改自 Psi。Psi 以 [Psi License](https://psi.vazkii.net/license.php) 开源。
