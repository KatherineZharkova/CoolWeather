package ru.cocovella.WeatherApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import ru.cocovella.WeatherApp.Model.Settings;

public class SettingsActivity extends AppCompatActivity {
    Settings settings;
    Button exitButton;
    Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = Settings.getInstance();
        setTheme(settings.getThemeID());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        themeSwitch = findViewById(R.id.themeSwitch);
        themeSwitch.setChecked(settings.getThemeID() == R.style.ColdTheme);
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setThemeID(isChecked ? R.style.ColdTheme : R.style.AppTheme);
                recreate();
            }
        });

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}
