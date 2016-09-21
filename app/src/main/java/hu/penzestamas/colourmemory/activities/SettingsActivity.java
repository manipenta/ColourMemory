package hu.penzestamas.colourmemory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import hu.penzestamas.colourmemory.R;
import hu.penzestamas.colourmemory.utils.StoredInfo;

/**
 * A simple Activity where user can enable or disable vibrates and sounds.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch vibrate = (Switch) findViewById(R.id.settings_vibrate_switch);
        vibrate.setChecked(StoredInfo.isVibrateEnabled(this.getApplicationContext()));
        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StoredInfo.setVibrateEnabled(SettingsActivity.this.getApplicationContext(), b);
            }
        });

        Switch sound = (Switch) findViewById(R.id.settings_sound_switch);
        sound.setChecked(StoredInfo.isSoundEnabled(this.getApplicationContext()));
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StoredInfo.setSoundEnabled(SettingsActivity.this.getApplicationContext(), b);
            }
        });

    }
}
