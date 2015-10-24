package thecodewarrior.equipment.client.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import thecodewarrior.equipment.client.BasicIcon;
import thecodewarrior.equipment.common.EquipmentMod;
import thecodewarrior.equipment.common.container.ContainerPlayerExpanded;
import thecodewarrior.equipment.common.container.SlotEquipment;
import thecodewarrior.equipment.common.network.PacketHandler;
import thecodewarrior.equipment.common.network.PacketNextPage;
import thecodewarrior.equipment.common.network.PacketPrevPage;


public class GuiPlayerExpanded extends InventoryEffectRenderer {
	
	public static final ResourceLocation background = 
			new ResourceLocation("equipment","textures/gui/expanded_inventory.png");
	public static final ResourceLocation slot = 
			new ResourceLocation("equipment","textures/gui/slot.png");
    
	/**
     * x size of the inventory window in pixels. Defined as  float, passed as int
     */
    private float xSizeFloat;
    /**
     * y size of the inventory window in pixels. Defined as  float, passed as int.
     */
    private float ySizeFloat;

    public GuiPlayerExpanded(EntityPlayer player)
    {
        super(new ContainerPlayerExpanded(player.inventory, !player.worldObj.isRemote, player));
//        this.xSize += 43;
    	this.guiLeft += 21;
        this.allowUserInput = true;
    }
    
    /**
     * Called from the main game loop to update the screen.
     */
    @Override 
    public void updateScreen()
    {
    	try {
			((ContainerPlayerExpanded)inventorySlots).equipment.blockEvents=false;
		} catch (Exception e) {	}
    }

    GuiButton nextButton, prevButton;
    
    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        this.buttonList.clear();
        
        this.buttonList.add(nextButton = new GuiButtonCustomTexture(
        		10, 203, 11, 16, 18,
        		background, new BasicIcon(256, 256, 16, 18, 203, 11), new BasicIcon(256, 256, 16, 18, 219, 11)
        	));
        this.buttonList.add(prevButton = new GuiButtonCustomTexture(
        		11, 203, 29, 16, 18,
        		background, new BasicIcon(256, 256, 16, 18, 203, 29), new BasicIcon(256, 256, 16, 18, 219, 29)
        	));
        
        super.initGui();
        
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 86, 16, 4210752);
        if(this.mc.thePlayer.inventory.getItemStack() == null) {
	        for(int i = 0; i < 8; i++) {
	        	Slot slot = this.inventorySlots.getSlot(45+i);
	        	if( slot instanceof SlotEquipment && ((SlotEquipment)slot).getType() != null && this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX, mouseY) ) {
	        		List<String> list = ((SlotEquipment)slot).getType().getTooltip(slot.getStack(), this.mc.thePlayer);
	
	                if(list != null)
	                	drawHoveringText(list, mouseX-guiLeft, mouseY-guiTop, fontRendererObj);
	        	}
	        }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        this.xSizeFloat = (float)par1;
        this.ySizeFloat = (float)par2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
    	nextButton.xPosition = prevButton.xPosition = this.guiLeft + 203;
    	nextButton.yPosition = this.guiTop + 11;
    	prevButton.yPosition = this.guiTop + 29;
    	
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture( background );
        int k = this.guiLeft;
        int l = this.guiTop;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize+27, this.ySize);
        
        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(i1);
            if (slot.getHasStack() && slot.getSlotStackLimit()==1)
            {
            	this.drawTexturedModalRect(k+slot.xDisplayPosition, l+slot.yDisplayPosition, 0, 170, 16, 16);
            }
        }
        
        for (int i = 0; i < 8; i++) {
        	SlotEquipment s = (SlotEquipment)this.inventorySlots.getSlot(45+i);
        	if(s != null && s.getType() != null && s.getType().getSlotOverlay(s.getStack()) != null) {
        		this.mc.getTextureManager().bindTexture( s.getType().getSlotOverlay(s.getStack()) );
            	this.drawTexturedModelRectFromIcon(k+s.xDisplayPosition, l+s.yDisplayPosition, BasicIcon.FULL, 16, 16);
        	}
        }
        
        drawPlayerModel(k + 51, l + 75, 30, (float)(k + 51) - this.xSizeFloat, (float)(l + 75 - 50) - this.ySizeFloat, this.mc.thePlayer);
    }

    public static void drawPlayerModel(int x, int y, int scale, float yaw, float pitch, EntityLivingBase playerdrawn)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, 50.0F);
        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = playerdrawn.renderYawOffset;
        float f3 = playerdrawn.rotationYaw;
        float f4 = playerdrawn.rotationPitch;
        float f5 = playerdrawn.prevRotationYawHead;
        float f6 = playerdrawn.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        playerdrawn.renderYawOffset = (float)Math.atan((double)(yaw / 40.0F)) * 20.0F;
        playerdrawn.rotationYaw = (float)Math.atan((double)(yaw / 40.0F)) * 40.0F;
        playerdrawn.rotationPitch = -((float)Math.atan((double)(pitch / 40.0F))) * 20.0F;
        playerdrawn.rotationYawHead = playerdrawn.rotationYaw;
        playerdrawn.prevRotationYawHead = playerdrawn.rotationYaw;
        GL11.glTranslatef(0.0F, playerdrawn.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(playerdrawn, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        playerdrawn.renderYawOffset = f2;
        playerdrawn.rotationYaw = f3;
        playerdrawn.rotationPitch = f4;
        playerdrawn.prevRotationYawHead = f5;
        playerdrawn.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }

        if (button.id == 1)
        {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
        
        if (button.id == 10) {
        	PacketHandler.INSTANCE.sendToServer(new PacketNextPage());
        	ContainerPlayerExpanded cpe = (ContainerPlayerExpanded)this.inventorySlots;
        }
        if (button.id == 11) {
        	PacketHandler.INSTANCE.sendToServer(new PacketPrevPage());
        	ContainerPlayerExpanded cpe = (ContainerPlayerExpanded)this.inventorySlots;
        }
    }

	@Override
	protected void keyTyped(char par1, int par2) {
		if (par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() || par2 == EquipmentMod.proxy.keyHandler.key.getKeyCode())
        {
            this.mc.thePlayer.closeScreen();
        } else
		super.keyTyped(par1, par2);
	}
    
	/**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
        this.xSize += 43;
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        this.xSize -= 43;
    }
    
}
