package com.skillslevel.joules.activities;


import android.support.annotation.Nullable;

import com.afollestad.appthemeengine.ATEActivity;
import com.skillslevel.joules.utils.Helpers;

public class BaseThemedActivity extends ATEActivity {
    @Nullable
    @Override
    public String getATEKey() {
        return Helpers.getATEKey(this);
    }
}
