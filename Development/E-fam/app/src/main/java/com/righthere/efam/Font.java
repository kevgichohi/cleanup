package com.righthere.efam;

import android.content.Context;
import android.graphics.Typeface;

public class Font {
	private static Font font;
	public Typeface FONT_EKMUKTA_LIGHT;

	private Font() {

	}

	public static Font getInstance(Context context) {
		if (font == null) {
			font = new Font();
			font.init(context);
		}
		return font;
	}

	public void init(Context context) {
		FONT_EKMUKTA_LIGHT = Typeface.createFromAsset(context.getAssets(),
				"fonts/ek_mukta/EkMukta-Light.ttf");
	}

}
