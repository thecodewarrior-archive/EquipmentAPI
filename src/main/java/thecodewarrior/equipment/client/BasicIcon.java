package thecodewarrior.equipment.client;

import net.minecraft.util.IIcon;

public class BasicIcon implements IIcon {

	public static IIcon FULL = new BasicIcon(1, 1, 1, 1, 0, 0);
	
	float texW, texH;
	int w, h, t, l;
	
	public BasicIcon(float texW, float texH, int w, int h, int l, int t) {
		this.texW = texW;
		this.texH = texH;
		this.w = w;
		this.h = h;
		this.l = l;
		this.t = t;
	}
	
	@Override
	public int getIconWidth() {
		return w;
	}

	@Override
	public int getIconHeight() {
		return h;
	}

	@Override
	public float getMinU() {
		return (float)( l/texW );
	}

	@Override
	public float getMaxU() {
		return getMinU() + (float)( w/texW );
	}

	@Override
	public float getInterpolatedU(double frac) {
		return getMinU() + (float)( w*frac/texW );
	}

	@Override
	public float getMinV() {
		return (float)( t/texH );
	}

	@Override
	public float getMaxV() {
		return getMinV() + (float)( h/texH );
	}

	@Override
	public float getInterpolatedV(double frac) {
		return getMinV() + (float)( h*frac/texH );
	}

	@Override
	public String getIconName() {
		return null;
	}

}
