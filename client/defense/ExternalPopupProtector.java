/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.manbo.v2c.client.defense;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalPopupProtector {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalPopupProtector.class);
    private static final Set<String> ALLOWED_TITLE_KEYWORDS = new CopyOnWriteArraySet<String>();
    private static final Set<Integer> HANDLED_WINDOWS = new HashSet<Integer>();
    private static volatile int totalClosed = 0;

    public static void addAllowedTitleKeyword(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            ALLOWED_TITLE_KEYWORDS.add(keyword);
        }
    }

    public static int getTotalClosed() {
        return totalClosed;
    }

    private static void startWindowWatchdog() {
        Thread watchdog = new Thread(() -> {
            while (true) {
                try {
                    while (true) {
                        Thread.sleep(300L);
                        ExternalPopupProtector.checkAndCloseTerrorWindows();
                    }
                }
                catch (InterruptedException e) {
                }
                catch (Exception exception) {
                    continue;
                }
                break;
            }
        }, "manbov2c-external-popup-watchdog");
        watchdog.setDaemon(true);
        watchdog.setPriority(1);
        watchdog.start();
    }

    private static void checkAndCloseTerrorWindows() {
        try {
            Frame[] frames;
            Window[] windows = Window.getWindows();
            if (windows != null) {
                for (Window window : windows) {
                    ExternalPopupProtector.processWindow(window);
                }
            }
            if ((frames = Frame.getFrames()) != null) {
                for (Frame frame : frames) {
                    ExternalPopupProtector.processWindow(frame);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static void processWindow(Window window) {
        if (window == null) {
            return;
        }
        int hashCode = System.identityHashCode(window);
        if (HANDLED_WINDOWS.contains(hashCode)) {
            return;
        }
        String title = ExternalPopupProtector.getWindowTitle(window);
        boolean isVisible = window.isVisible();
        String className = window.getClass().getName();
        boolean isModal = ExternalPopupProtector.isModalWindow(window);
        if (ExternalPopupProtector.isTerrorPopup(title, className, window)) {
            try {
                window.setVisible(false);
                window.dispose();
                ++totalClosed;
                HANDLED_WINDOWS.add(hashCode);
                LOGGER.info("[\u5916\u90e8\u5f39\u7a97\u4fdd\u62a4] \u2718 \u5df2\u5173\u95ed\u6050\u6016\u5f39\u7a97: title={}, class={}", (Object)title, (Object)className);
            }
            catch (Exception exception) {}
        } else {
            HANDLED_WINDOWS.add(hashCode);
        }
    }

    private static boolean isTerrorPopup(String title, String className, Window window) {
        boolean isPopupType;
        if (!window.isVisible()) {
            return false;
        }
        String lowerTitle = title.toLowerCase();
        String lowerClass = className.toLowerCase();
        for (String keyword : ALLOWED_TITLE_KEYWORDS) {
            if (!lowerTitle.contains(keyword.toLowerCase())) continue;
            return false;
        }
        if (lowerClass.contains("jdialog") && lowerTitle.contains("\u97f3\u6548\u76d1\u63a7")) {
            return false;
        }
        if (lowerClass.contains("glfw") || lowerClass.contains("lwjgl")) {
            return false;
        }
        boolean bl = isPopupType = window instanceof JDialog || window instanceof JFrame || window instanceof JWindow;
        if (isPopupType) {
            Dimension size;
            double area;
            return !lowerTitle.isEmpty() && !lowerTitle.equals("") || !((area = (size = window.getSize()).getWidth() * size.getHeight()) < 90000.0);
        }
        return false;
    }

    private static String getWindowTitle(Window window) {
        if (window instanceof Frame) {
            String title = ((Frame)window).getTitle();
            return title != null ? title : "";
        }
        if (window instanceof Dialog) {
            String title = ((Dialog)window).getTitle();
            return title != null ? title : "";
        }
        return "";
    }

    private static boolean isModalWindow(Window window) {
        if (window instanceof Dialog) {
            return ((Dialog)window).isModal();
        }
        return false;
    }

    public static void reset() {
        HANDLED_WINDOWS.clear();
        totalClosed = 0;
    }

    static {
        ALLOWED_TITLE_KEYWORDS.add("\u8bd7\u5b9d\u4e4b\u7231");
        ALLOWED_TITLE_KEYWORDS.add("manbov2c");
        ALLOWED_TITLE_KEYWORDS.add("\u66fc\u6ce2");
        ALLOWED_TITLE_KEYWORDS.add("Java");
        ALLOWED_TITLE_KEYWORDS.add("Minecraft");
        ExternalPopupProtector.startWindowWatchdog();
        LOGGER.info("[\u5916\u90e8\u5f39\u7a97\u4fdd\u62a4] \u5b88\u62a4\u7ebf\u7a0b\u5df2\u542f\u52a8");
    }
}

