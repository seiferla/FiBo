package de.dhbw.ka.se.fibo.ui.adding;

import android.view.View;
import android.widget.DatePicker;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class MaterialDatePickerMatcher extends TypeSafeMatcher<View> {



    @Override
    protected boolean matchesSafely(View item) {
       return item instanceof DatePicker;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Matches MaterialDatePicker");
    }

}
