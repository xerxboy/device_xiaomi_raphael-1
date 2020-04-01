/*
 * Copyright (C) 2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.chaldeastudio.deviceparts.display;

import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import androidx.preference.PreferenceManager;

import org.chaldeastudio.deviceparts.R;

import vendor.xiaomi.hardware.displayfeature.V1_0.IDisplayFeature;

public class DcDimmingTileService extends TileService {
    private static final String DC_DIMMING_KEY = "dc_dimming_enable";
    private static final boolean DC_DIMMING_DEFAULT_VALUE = false;

    public DcDimmingTileService() { }

    @Override
    public void onStartListening() {
        super.onStartListening();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean state = mSharedPrefs.getBoolean(DC_DIMMING_KEY, false);
        updateTileState(state);
    }

    @Override
    public void onClick() {
        super.onClick();
        SharedPreferences mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enable = !(mSharedPrefs.getBoolean(DC_DIMMING_KEY, false));
        mSharedPrefs.edit().putBoolean(DC_DIMMING_KEY, enable).apply();
        try {
            IDisplayFeature mDisplayFeature = IDisplayFeature.getService();
            mDisplayFeature.setFeature(0, 20, enable ? 1 : 0, 255);
        } catch (Exception e) {}

        updateTileState();
    }

    private void updateTileState(boolean state) {
        final Tile tile = getQsTile();
        tile.setState(enabled ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        tile.updateTile();
    }
}
