/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.BackupConfirmScreen
 *  net.minecraft.client.gui.screens.ChatScreen
 *  net.minecraft.client.gui.screens.ConfirmLinkScreen
 *  net.minecraft.client.gui.screens.ConfirmScreen
 *  net.minecraft.client.gui.screens.DeathScreen
 *  net.minecraft.client.gui.screens.DemoIntroScreen
 *  net.minecraft.client.gui.screens.OptionsScreen
 *  net.minecraft.client.gui.screens.OutOfMemoryScreen
 *  net.minecraft.client.gui.screens.PauseScreen
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.SimpleOptionsSubScreen
 *  net.minecraft.client.gui.screens.TitleScreen
 *  net.minecraft.client.gui.screens.WinScreen
 *  net.minecraft.client.gui.screens.achievement.StatsScreen
 *  net.minecraft.client.gui.screens.advancements.AdvancementsScreen
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.AnvilScreen
 *  net.minecraft.client.gui.screens.inventory.BeaconScreen
 *  net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen
 *  net.minecraft.client.gui.screens.inventory.BookViewScreen
 *  net.minecraft.client.gui.screens.inventory.BrewingStandScreen
 *  net.minecraft.client.gui.screens.inventory.CartographyTableScreen
 *  net.minecraft.client.gui.screens.inventory.CraftingScreen
 *  net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.EnchantmentScreen
 *  net.minecraft.client.gui.screens.inventory.FurnaceScreen
 *  net.minecraft.client.gui.screens.inventory.GrindstoneScreen
 *  net.minecraft.client.gui.screens.inventory.HopperScreen
 *  net.minecraft.client.gui.screens.inventory.HorseInventoryScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.client.gui.screens.inventory.LecternScreen
 *  net.minecraft.client.gui.screens.inventory.LoomScreen
 *  net.minecraft.client.gui.screens.inventory.MerchantScreen
 *  net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen
 *  net.minecraft.client.gui.screens.inventory.SmithingScreen
 *  net.minecraft.client.gui.screens.inventory.SmokerScreen
 *  net.minecraft.client.gui.screens.inventory.StonecutterScreen
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.InputEvent$Key
 *  net.minecraftforge.client.event.ScreenEvent$Opening
 *  net.minecraftforge.client.event.sound.PlaySoundEvent
 *  net.minecraftforge.eventbus.api.EventPriority
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.client.defense;

import com.manbo.v2c.client.defense.HorrorSoundBlacklist;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.DemoIntroScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.OutOfMemoryScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.BlastFurnaceScreen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.gui.screens.inventory.HopperScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.client.gui.screens.inventory.LoomScreen;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.gui.screens.inventory.SmokerScreen;
import net.minecraft.client.gui.screens.inventory.StonecutterScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod.EventBusSubscriber(modid = "manbov2c", value = { Dist.CLIENT }, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HorrorDefenseSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger(HorrorDefenseSystem.class);
    private static volatile boolean soundDialogOpen = false;
    private static volatile boolean pendingHorrorCheck = false;
    private static volatile Screen pendingScreen = null;
    private static volatile int pauseTicks = 0;
    private static final Set<Class<?>> VANILLA_SCREEN_CLASSES = new HashSet<>(Arrays.asList(ChatScreen.class,
            DeathScreen.class, InventoryScreen.class, CreativeModeInventoryScreen.class, BookViewScreen.class,
            MerchantScreen.class, AnvilScreen.class, BeaconScreen.class, BlastFurnaceScreen.class,
            BrewingStandScreen.class, CraftingScreen.class, EnchantmentScreen.class, FurnaceScreen.class,
            GrindstoneScreen.class, HopperScreen.class, LecternScreen.class, LoomScreen.class, SmithingScreen.class,
            SmokerScreen.class, StonecutterScreen.class, CartographyTableScreen.class, ShulkerBoxScreen.class,
            HorseInventoryScreen.class, SimpleOptionsSubScreen.class, OptionsScreen.class, PauseScreen.class,
            TitleScreen.class, WinScreen.class, ConfirmScreen.class, ConfirmLinkScreen.class, BackupConfirmScreen.class,
            DemoIntroScreen.class, OutOfMemoryScreen.class, StatsScreen.class, AdvancementsScreen.class));

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onScreenOpen(ScreenEvent.Opening event) {
        Minecraft mc = Minecraft.getInstance();
        Screen screen = event.getScreen();
        if (screen == null) {
            return;
        }
        if (HorrorDefenseSystem.isVanillaScreen(screen)) {
            return;
        }
        if (pendingHorrorCheck) {
            event.setCanceled(true);
            return;
        }
        pendingHorrorCheck = true;
        pendingScreen = screen;
        HorrorDefenseSystem.freezeGame(true);
        LocalPlayer player = mc.player;
        if (player != null) {
            player.displayClientMessage(
                    (Component) Component
                            .literal((String) "\u00a7c\u00a7l\u26a0 \u68c0\u6d4b\u5230\u975e\u539f\u7248GUI\uff01"),
                    false);
            player.displayClientMessage(
                    (Component) Component.literal(
                            (String) ("\u00a7e\u68c0\u6d4b\u5230: \u00a7f" + screen.getClass().getSimpleName())),
                    false);
            player.displayClientMessage((Component) Component.literal(
                    (String) "\u00a7a[Y] \u00a77\u786e\u8ba4 \u2192 \u00a7c\u8fd9\u662f\u6050\u6016\u7279\u6548\uff0c\u76f4\u63a5\u5904\u51b3"),
                    false);
            player.displayClientMessage((Component) Component.literal(
                    (String) "\u00a7a[N] \u00a77\u786e\u8ba4 \u2192 \u00a7e\u8fd9\u662f\u6b63\u5e38\u6a21\u7ec4GUI\uff0c\u653e\u884c"),
                    false);
            player.displayClientMessage((Component) Component.literal(
                    (String) "\u00a77\u8bf7\u6309 \u00a7eY \u00a77\u6216 \u00a7eN \u00a77\u952e\u505a\u51fa\u9009\u62e9..."),
                    false);
        }
        event.setCanceled(true);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onKeyInput(InputEvent.Key event) {
        if (!pendingHorrorCheck) {
            return;
        }
        if (event.getAction() != 1) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        int key = event.getKey();
        if (key == 89) {
            if (pendingScreen != null) {
                String className = pendingScreen.getClass().getName();
                HorrorSoundBlacklist.addEffectToBlacklist(className);
                LOGGER.info("[\u6050\u6016\u9632\u5fa1] \u5df2\u5904\u51b3\u6050\u6016\u7279\u6548: {}",
                        (Object) className);
                LocalPlayer player = mc.player;
                if (player != null) {
                    player.displayClientMessage(
                            (Component) Component.literal((String) ("\u00a7c\u00a7l\u2718 \u5df2\u5904\u51b3: "
                                    + pendingScreen.getClass().getSimpleName())),
                            false);
                }
            }
            pendingHorrorCheck = false;
            pendingScreen = null;
            HorrorDefenseSystem.freezeGame(false);
            if (mc.getSoundManager() != null) {
                mc.getSoundManager().stop();
            }
        } else if (key == 78) {
            LocalPlayer player = mc.player;
            if (player != null) {
                player.displayClientMessage(
                        (Component) Component.literal((String) ("\u00a7a\u00a7l\u2714 \u5df2\u653e\u884c: "
                                + (pendingScreen != null ? pendingScreen.getClass().getSimpleName() : "\u672a\u77e5"))),
                        false);
            }
            pendingHorrorCheck = false;
            pendingScreen = null;
            HorrorDefenseSystem.freezeGame(false);
            if (mc.getSoundManager() != null) {
                mc.getSoundManager().resume();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onHKeyInput(InputEvent.Key event) {
        if (event.getAction() != 1) {
            return;
        }
        if (event.getKey() != 72) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (soundDialogOpen) {
            soundDialogOpen = false;
            return;
        }
        HorrorDefenseSystem.openSoundMonitorDialog(mc);
    }

    private static void openSoundMonitorDialog(Minecraft mc) {
        soundDialogOpen = true;
        LinkedHashMap<String, SoundInstance> playingSounds = new LinkedHashMap<String, SoundInstance>();
        try {
            SoundManager soundManager = mc.getSoundManager();
            Field soundEngineField = SoundManager.class.getDeclaredField("soundEngine");
            soundEngineField.setAccessible(true);
            Object soundEngine = soundEngineField.get(soundManager);
            Field instanceToChannelField = soundEngine.getClass().getDeclaredField("instanceToChannel");
            instanceToChannelField.setAccessible(true);
            Map instanceToChannel = (Map) instanceToChannelField.get(soundEngine);
            for (Object siObj : instanceToChannel.keySet()) {
                SoundInstance si = (SoundInstance) siObj;
                String loc = si.getLocation().toString();
                if (playingSounds.containsKey(loc))
                    continue;
                playingSounds.put(loc, si);
            }
        } catch (Exception e) {
            LOGGER.warn("[\u6050\u6016\u9632\u5fa1] \u83b7\u53d6\u64ad\u653e\u97f3\u6548\u5931\u8d25: {}",
                    (Object) e.getMessage());
        }
        SoundMonitorData sharedData = new SoundMonitorData();
        sharedData.soundIds.addAll(playingSounds.keySet());
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("\u97f3\u6548\u76d1\u63a7 - \u8bd7\u5b9d\u4e4b\u7231\u00b7\u6050\u6016\u9632\u5fa1");
            dialog.setDefaultCloseOperation(2);
            dialog.setModal(false);
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            JLabel header = new JLabel("\u5f53\u524d\u6e38\u620f\u6b63\u5728\u64ad\u653e\u7684\u97f3\u6548 ("
                    + sharedData.soundIds.size() + " \u4e2a):");
            header.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", 1, 14));
            mainPanel.add((java.awt.Component) header, "North");
            DefaultListModel<SoundCheckItem> listModel = new DefaultListModel<SoundCheckItem>();
            for (String soundId : sharedData.soundIds) {
                boolean isHorror = HorrorSoundBlacklist.isSoundBlacklisted(soundId);
                listModel.addElement(new SoundCheckItem(soundId, isHorror));
            }
            JList<SoundCheckItem> list = new JList<SoundCheckItem>(listModel);
            list.setCellRenderer(new CheckboxListRenderer());
            list.setFont(new Font("Consolas", 0, 12));
            JPanel topButtonPanel = new JPanel(new FlowLayout(0));
            JButton selectAllBtn = new JButton("\u5168\u9009");
            JButton deselectAllBtn = new JButton("\u5168\u4e0d\u9009");
            topButtonPanel.add(selectAllBtn);
            topButtonPanel.add(deselectAllBtn);
            mainPanel.add((java.awt.Component) topButtonPanel, "North");
            selectAllBtn.addActionListener(e -> {
                for (int i = 0; i < listModel.size(); ++i) {
                    ((SoundCheckItem) listModel.get((int) i)).selected = true;
                }
                list.repaint();
            });
            deselectAllBtn.addActionListener(e -> {
                for (int i = 0; i < listModel.size(); ++i) {
                    ((SoundCheckItem) listModel.get((int) i)).selected = false;
                }
                list.repaint();
            });
            mainPanel.add((java.awt.Component) new JScrollPane(list), "Center");
            JPanel buttonPanel = new JPanel(new FlowLayout(2));
            JLabel hintLabel = new JLabel(
                    "\u52fe\u9009\u6050\u6016\u97f3\u6548\u540e\u70b9\u51fb\u786e\u8ba4\uff0c\u6309H\u952e\u5173\u95ed\u7a97\u53e3");
            hintLabel.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", 0, 11));
            hintLabel.setForeground(Color.GRAY);
            buttonPanel.add(hintLabel);
            JButton applyBtn = new JButton("\u2713 \u786e\u8ba4\u5c4f\u853d");
            applyBtn.setFont(new Font("\u5fae\u8f6f\u96c5\u9ed1", 1, 13));
            applyBtn.setBackground(new Color(255, 80, 80));
            applyBtn.setForeground(Color.WHITE);
            applyBtn.addActionListener(e -> {
                HashSet<String> toBlacklist = new HashSet<String>();
                for (int i = 0; i < listModel.size(); ++i) {
                    SoundCheckItem item = (SoundCheckItem) listModel.get(i);
                    if (!item.selected || HorrorSoundBlacklist.isSoundBlacklisted(item.soundId))
                        continue;
                    toBlacklist.add(item.soundId);
                }
                if (!toBlacklist.isEmpty()) {
                    HorrorSoundBlacklist.addSoundsToBlacklist(toBlacklist);
                    mc.execute(() -> {
                        SoundManager sm = mc.getSoundManager();
                        try {
                            Field soundEngineField2 = SoundManager.class.getDeclaredField("soundEngine");
                            soundEngineField2.setAccessible(true);
                            Object soundEngine2 = soundEngineField2.get(sm);
                            Field instanceToChannelField2 = soundEngine2.getClass()
                                    .getDeclaredField("instanceToChannel");
                            instanceToChannelField2.setAccessible(true);
                            Map instanceToChannel2 = (Map) instanceToChannelField2.get(soundEngine2);
                            ArrayList<SoundInstance> toStop = new ArrayList<SoundInstance>();
                            block2: for (Object siObj2 : instanceToChannel2.keySet()) {
                                SoundInstance si = (SoundInstance) siObj2;
                                String loc = si.getLocation().toString();
                                for (String bl : toBlacklist) {
                                    if (!loc.contains(bl) && !bl.contains(loc))
                                        continue;
                                    toStop.add(si);
                                    continue block2;
                                }
                            }
                            for (SoundInstance si : toStop) {
                                sm.stop(si);
                            }
                        } catch (Exception ex) {
                            sm.stop();
                        }
                        if (mc.player != null) {
                            mc.player.displayClientMessage((Component) Component.literal(
                                    (String) ("\u00a7c\u00a7l[\u6050\u6016\u9632\u5fa1] \u00a77\u5df2\u5c4f\u853d \u00a7c"
                                            + toBlacklist.size() + " \u00a77\u4e2a\u6050\u6016\u97f3\u6548")),
                                    false);
                        }
                    });
                }
                dialog.dispose();
                soundDialogOpen = false;
            });
            buttonPanel.add(applyBtn);
            mainPanel.add((java.awt.Component) buttonPanel, "South");
            dialog.add(mainPanel);
            dialog.setSize(600, 450);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    soundDialogOpen = false;
                }
            });
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlaySound(PlaySoundEvent event) {
        SoundInstance sound = event.getSound();
        if (sound == null) {
            return;
        }
        String loc = sound.getLocation().toString();
        if (HorrorSoundBlacklist.isSoundBlacklisted(loc)) {
            event.setSound(null);
            return;
        }
        if (pendingHorrorCheck && !loc.startsWith("minecraft:")) {
            event.setSound(null);
        }
    }

    private static boolean isVanillaScreen(Screen screen) {
        if (screen == null) {
            return true;
        }
        if (screen instanceof AbstractContainerScreen) {
            return true;
        }
        for (Class<?> clazz : VANILLA_SCREEN_CLASSES) {
            if (!clazz.isInstance(screen))
                continue;
            return true;
        }
        String className = screen.getClass().getName().toLowerCase();
        return className.startsWith("net.minecraft");
    }

    private static void freezeGame(boolean freeze) {
        Minecraft mc = Minecraft.getInstance();
        if (freeze) {
            try {
                Field pauseField = Minecraft.class.getDeclaredField("pause");
                pauseField.setAccessible(true);
                pauseField.setBoolean(mc, true);
            } catch (Exception pauseField) {
                // empty catch block
            }
            if (mc.getSoundManager() != null) {
                mc.getSoundManager().stop();
            }
        } else {
            try {
                Field pauseField = Minecraft.class.getDeclaredField("pause");
                pauseField.setAccessible(true);
                pauseField.setBoolean(mc, false);
            } catch (Exception exception) {
                // empty catch block
            }
            if (mc.getSoundManager() != null) {
                mc.getSoundManager().resume();
            }
        }
    }

    private static class SoundMonitorData {
        final List<String> soundIds = new ArrayList<String>();

        private SoundMonitorData() {
        }
    }

    public static class SoundCheckItem {
        public final String soundId;
        public boolean selected;

        public SoundCheckItem(String soundId, boolean selected) {
            this.soundId = soundId;
            this.selected = selected;
        }

        public String toString() {
            return (this.selected ? "\u2611 " : "\u2610 ") + this.soundId;
        }
    }

    private static class CheckboxListRenderer
            extends JCheckBox
            implements ListCellRenderer<SoundCheckItem> {
        private CheckboxListRenderer() {
        }

        @Override
        public java.awt.Component getListCellRendererComponent(JList<? extends SoundCheckItem> list,
                SoundCheckItem value, int index, boolean isSelected, boolean cellHasFocus) {
            this.setText(value.soundId);
            this.setSelected(value.selected);
            this.setFont(new Font("Consolas", 0, 12));
            this.setBackground(isSelected ? new Color(220, 220, 255) : Color.WHITE);
            this.setForeground(Color.BLACK);
            if (HorrorSoundBlacklist.isSoundBlacklisted(value.soundId)) {
                this.setForeground(Color.GRAY);
                this.setText("\u2713 " + value.soundId + " (\u5df2\u5c4f\u853d)");
            }
            this.addActionListener(e -> {
                value.selected = this.isSelected();
            });
            return this;
        }
    }
}
