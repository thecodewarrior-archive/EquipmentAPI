package thecodewarrior.equipment.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GuiButtonCustomTexture extends GuiButton {

	ResourceLocation tex;
	IIcon inactive, active;
	
	public GuiButtonCustomTexture(int id, int x, int y, int width, int height,
			ResourceLocation tex, IIcon inactive, IIcon active) {
		super(id, x, y, width, height, null);
		this.tex = tex;
		this.inactive = inactive;
		this.active = active;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
		mc.getTextureManager().bindTexture(tex);
		IIcon icon = this.inactive;
		if( mouseX > this.xPosition && mouseX < this.xPosition + this.width &&
			mouseY > this.yPosition && mouseY < this.yPosition + this.height  ) {
			icon = this.active;
		}
		drawTexturedModelRectFromIcon(this.xPosition, this.yPosition, icon, this.width, this.height);
    }

}
