package com.github.rintaun.magicwands.client.gui;

import com.github.rintaun.magicwands.MagicWands;
import com.github.rintaun.magicwands.command.MagicWandsCommandHandler;
import com.github.rintaun.magicwands.item.ItemMagicWand;
import com.github.rintaun.magicwands.network.MagicWandsUpdateMessage;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiEditWand extends GuiScreen
{
    private final ItemStack localWand;
    private final MagicWandsCommandHandler localCommandBlock;

    private boolean trackingInitial;

    public static final int BTN_DONE = 0;
    public static final int BTN_CANCEL = 1;
    public static final int TEXT_COMMAND = 2;
    public static final int TEXT_OUTPUT = 3;
    public static final int BTN_TRACK_TOGGLE = 4;

    private GuiTextField commandTextField;
    private GuiTextField outputTextField;

    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton trackToggleBtn;

    public GuiEditWand(ItemStack wand, MagicWandsCommandHandler commandHandler)
    {
        this.localWand = wand;
        this.localCommandBlock = commandHandler;
    }

    public void updateScreen()
    {
        this.commandTextField.updateCursorCounter();
    }

    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);

        this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButton(BTN_DONE, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButton(BTN_CANCEL, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(this.trackToggleBtn = new GuiButton(BTN_TRACK_TOGGLE, this.width / 2 + 150 - 20, 150, 20, 20, "O"));

        this.commandTextField = new GuiTextField(TEXT_COMMAND, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        this.commandTextField.setMaxStringLength(32767);
        this.commandTextField.setFocused(true);
        this.commandTextField.setText(this.localCommandBlock.getCustomName());

        this.outputTextField = new GuiTextField(TEXT_OUTPUT, this.fontRendererObj, this.width / 2 - 150, 150, 275, 20);
        this.outputTextField.setMaxStringLength(32767);
        this.outputTextField.setEnabled(false);
        this.outputTextField.setText("-");
        this.trackingInitial = this.localCommandBlock.shouldTrackOutput();
        this.updateTrackFields();
        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id) {
                case BTN_DONE:
                    this.localCommandBlock.setCommand(this.commandTextField.getText().trim());
                    this.localCommandBlock.updateWand(this.localWand);
                    if (!this.localCommandBlock.shouldTrackOutput()) {
                        this.localCommandBlock.setLastOutput((IChatComponent) null);
                    }

                    MagicWands.network.sendToServer(new MagicWandsUpdateMessage(this.localCommandBlock.getCustomName()));

                    this.mc.displayGuiScreen((GuiScreen) null);
                    break;
                case BTN_CANCEL:
                    this.localCommandBlock.setTrackOutput(this.trackingInitial);
                    this.mc.displayGuiScreen((GuiScreen) null);
                    break;
                case BTN_TRACK_TOGGLE:
                    this.localCommandBlock.setTrackOutput(!this.localCommandBlock.shouldTrackOutput());
                    this.updateTrackFields();
                    break;
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.commandTextField.textboxKeyTyped(typedChar, keyCode);
        this.outputTextField.textboxKeyTyped(typedChar, keyCode);
        this.doneBtn.enabled = this.commandTextField.getText().trim().length() > 0;

        if (keyCode == 1) {
            this.actionPerformed(this.cancelBtn);
        } else if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.doneBtn);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
        this.outputTextField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 37, 10526880);
        this.commandTextField.drawTextBox();
        byte b0 = 75;
        String s = I18n.format("advMode.nearestPlayer", new Object[0]);
        int i1 = this.width / 2 - 150;
        int l = 0;
        this.drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), i1, b0 + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), i1, b0 + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), i1, b0 + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
        this.drawString(this.fontRendererObj, "", i1, b0 + l++ * this.fontRendererObj.FONT_HEIGHT, 10526880);

        if (this.outputTextField.getText().length() > 0) {
            this.drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), i1, b0 + l * this.fontRendererObj.FONT_HEIGHT + 16, 10526880);
            this.outputTextField.drawTextBox();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateTrackFields()
    {
        if (this.localCommandBlock.shouldTrackOutput()) {
            this.trackToggleBtn.displayString = "O";
            if (this.localCommandBlock.getLastOutput() != null)
            {
                this.outputTextField.setText(this.localCommandBlock.getLastOutput().getUnformattedText());
            }
        }
        else {
            this.trackToggleBtn.displayString = "X";
            this.outputTextField.setText("-");
        }
    }
}
