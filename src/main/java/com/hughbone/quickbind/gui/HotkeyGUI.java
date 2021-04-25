package com.hughbone.quickbind.gui;

import com.hughbone.quickbind.Main;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.Iterator;


public class HotkeyGUI extends LightweightGuiDescription {

    public static HotkeyButton selectedButton;
    public static WTextField searchField;
    public static WText selectText = new WText(Text.of("Selected: None"));
    public static WScrollPanel scrollPanel;
    public static WGridPanel root;

    public HotkeyGUI() {
        WGridPanel panel = new WGridPanel();
        HotkeyButton.hotkeyButtonList.clear();
        int y = 0;
        Iterator var0 = Main.keyBindings.values().iterator();
        while(var0.hasNext()) {
            KeyBinding keyBinding = (KeyBinding) var0.next();
            final String key = keyBinding.getTranslationKey();
            if (key.contains("key.hotbar.") || key.equals("key.right") || key.equals("key.left") || key.equals("key.forward") || key.equals("key.back")) {
                continue;
            }
            panel.add(new HotkeyButton(Text.of(I18n.translate(keyBinding.getTranslationKey())), keyBinding), 0, y, 14, 1);
            y++;
        }

        scrollPanel = new WScrollPanel(panel);
        root = new WGridPanel();
        root.add(scrollPanel, 0, 0, 17, 9);

        root.add(new NormalButton(Text.of("Search")), 0, 9, 2,1);

        WButton resetBtn = new WButton(Text.of("Reset"));
        root.add(resetBtn, 4, 9, 2,1);
        resetBtn.setOnClick(() -> {
            selectText.setText(Text.of("None Selected."));
        });

        searchField = new WTextField();
        searchField.setMaxLength(16);
        root.add(searchField, 0, 10, 6,1);

        selectText.setText(Text.of("None Selected."));
        selectText.setColor(16777215);
        if (MacroButton.clickedBtn.keyBinding != null) {
            selectText.setText(Text.of(I18n.translate(MacroButton.clickedBtn.keyBinding.getTranslationKey())));
        }
        root.add(selectText, 7, 10, 7, 1);

        WButton applyButton = new WButton(Text.of("APPLY"));
        root.add(applyButton, 15, 10, 2,1);
        applyButton.setOnClick(() -> {
            if (selectText.getText().getString().equals("None Selected.")) {
                ConfigGUI.keyBinding = null;
            }
            else if (HotkeyGUI.selectedButton != null) {
                ConfigGUI.keyBinding = HotkeyGUI.selectedButton.keyBinding;
            }
            MinecraftClient.getInstance().openScreen(new GUIScreen(new ConfigGUI(true)));
        });

        setRootPanel(root);
        root.setSize(300, 200);
        rootPanel.validate(this);

    }

    // Change background panel color to transparent black
    @Override
    public void addPainters() {
        super.addPainters();
        this.rootPanel.setBackgroundPainter(BackgroundPainter.createColorful(0x4D000000));
    }

}
