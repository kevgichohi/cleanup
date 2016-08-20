package com.redhering.nunuaraha;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HelperFont {
	private static Font font;

	public static void applyFont(View parentView, Context context) {
		font = Font.getInstance(context);
		apply((ViewGroup) parentView);
	}

	private static void apply(ViewGroup parentView) {
		for (int i = 0; i < parentView.getChildCount(); i++) {

			View view = parentView.getChildAt(i);

			// You can add any view element here on which you want to apply font

			if (view instanceof EditText) {
				((EditText) view).setTypeface(font.FONT_EKMUKTA_LIGHT);
			}

			if (view instanceof TextView) {
				((TextView) view).setTypeface(font.FONT_EKMUKTA_LIGHT);
			}

			if (view instanceof Button) {
				((Button) view).setTypeface(font.FONT_EKMUKTA_LIGHT);
			}

			if (view instanceof ViewGroup
					&& ((ViewGroup) view).getChildCount() > 0) {
				apply((ViewGroup) view);
			}

		}

	}

}
