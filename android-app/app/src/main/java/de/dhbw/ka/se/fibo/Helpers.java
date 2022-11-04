package de.dhbw.ka.se.fibo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

public class Helpers {

    public static void updateSupportActionBarText(Context context, ActionBar actionBar, CharSequence text) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(actionBar);
        Objects.requireNonNull(text);

        TextView textview = new TextView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textview.setLayoutParams(layoutParams);
        textview.setText(text);
        textview.setTextColor(context.getResources().getColor(R.color.black));

        textview.setGravity(Gravity.CENTER);
        textview.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textview.setTextSize(20);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(textview);
    }

    public static char signumToHumanReadableSign(int signum) {
        if (signum == -1) {
            return '-';
        } else if (signum == 0) {
            return '+';
        } else if (signum == 1) {
            return '+';
        }

        throw new RuntimeException("signum must be -1, 0 or 1!");
    }

    public static String formatBigDecimalCurrency(BigDecimal bd) {
        return String.format(Locale.GERMANY, "%,.2f €", bd.setScale(2, RoundingMode.HALF_UP));
    }
}
