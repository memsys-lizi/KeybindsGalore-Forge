/*
 * This class is modified from the PSI mod created by Vazkii
 * Psi Source Code: https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the Psi License: https://psi.vazkii.net/license.php
 *
 * Original Keybinds Galore by Cael (Fabric): https://github.com/CaelTheColher/KeybindsGalore
 */
package me.cael.keybindsgalore;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
public class KeybindsScreen extends Screen {

    int timeIn = 0;
    int slotSelected = -1;
    private boolean closed;

    private InputConstants.Key conflictedKey = InputConstants.UNKNOWN;

    final Minecraft mc;

    public KeybindsScreen() {
        super(Component.empty());
        mc = Minecraft.getInstance();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.render(poseStack, mouseX, mouseY, partialTick);

        int x = width / 2;
        int y = height / 2;
        int maxRadius = 80;

        double angle = mouseAngle(x, y, mouseX, mouseY);

        List<KeyMapping> conflicting = KeybindsManager.getConflicting(conflictedKey);
        if (conflicting == null || conflicting.isEmpty()) {
            confirmSelection();
            return;
        }
        int segments = conflicting.size();
        float step = (float) Math.PI / 180;
        float degPer = (float) Math.PI * 2 / segments;

        slotSelected = -1;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buf.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
        for (int seg = 0; seg < segments; seg++) {
            boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
            float radius = Math.max(0F, Math.min((timeIn + partialTick - seg * 6F / segments) * 40F, maxRadius));
            if (mouseInSector) {
                radius *= 1.025f;
            }

            int gs = 0x40;
            if (seg % 2 == 0) {
                gs += 0x19;
            }
            int r = gs;
            int g = gs;
            int b = gs;
            int a = 0x66;

            if (seg == 0) {
                buf.vertex(x, y, 0).color(r, g, b, a).endVertex();
            }

            if (mouseInSector) {
                slotSelected = seg;
                r = g = b = 0xFF;
            }

            for (float i = 0; i < degPer + step / 2; i += step) {
                float rad = i + seg * degPer;
                float xp = x + Mth.cos(rad) * radius;
                float yp = y + Mth.sin(rad) * radius;

                if (i == 0) {
                    buf.vertex(xp, yp, 0).color(r, g, b, a).endVertex();
                }
                buf.vertex(xp, yp, 0).color(r, g, b, a).endVertex();
            }
        }
        tess.end();

        for (int seg = 0; seg < segments; seg++) {
            boolean mouseInSector = degPer * seg < angle && angle < degPer * (seg + 1);
            float radius = Math.max(0F, Math.min((timeIn + partialTick - seg * 6F / segments) * 40F, maxRadius));
            if (mouseInSector) {
                radius *= 1.025f;
            }

            float rad = (seg + 0.5f) * degPer;
            float xp = x + Mth.cos(rad) * radius;
            float yp = y + Mth.sin(rad) * radius;
            String boundKey = Component.translatable(conflicting.get(seg).getName()).getString();
            float xsp = xp - 4;
            float ysp = yp;
            String name = (mouseInSector ? ChatFormatting.UNDERLINE : ChatFormatting.RESET) + boundKey;
            int labelWidth = font.width(name);
            if (xsp < x) {
                xsp -= labelWidth - 8;
            }
            if (ysp < y) {
                ysp -= 9;
            }
            font.drawShadow(poseStack, name, xsp, ysp, 0xFFFFFF);
        }
    }

    public void setConflictedKey(InputConstants.Key key) {
        this.conflictedKey = key;
    }

    public InputConstants.Key getConflictedKey() {
        return this.conflictedKey;
    }

    public void confirmSelection() {
        if (closed) {
            return;
        }
        closed = true;
        int slot = slotSelected;
        InputConstants.Key key = conflictedKey;
        if (mc.screen == this) {
            mc.setScreen(null);
        }
        List<KeyMapping> conflicting = KeybindsManager.getConflicting(key);
        if (slot != -1 && conflicting != null && slot < conflicting.size()) {
            KeyMapping bind = conflicting.get(slot);
            KeybindsManager.activateSelection(bind);
        }
    }

    private static double mouseAngle(int x, int y, int mx, int my) {
        return (Mth.atan2(my - y, mx - x) + Math.PI * 2) % (Math.PI * 2);
    }

    @Override
    public void tick() {
        super.tick();
        if (!isConflictKeyDown()) {
            confirmSelection();
        }
        timeIn++;
    }

    private boolean isConflictKeyDown() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        if (conflictedKey.getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(window, conflictedKey.getValue()) == GLFW.GLFW_PRESS;
        }
        return InputConstants.isKeyDown(window, conflictedKey.getValue());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
