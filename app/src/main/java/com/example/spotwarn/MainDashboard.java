package com.example.spotwarn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainDashboard extends AppCompatActivity {

    private Button btnUser, btnPolice, btnAdmin;
    private Spinner spinnerLanguage;

    private SharedPreferences preferences;
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_LANGUAGE = "app_language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load saved language
        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String lang = preferences.getString(KEY_LANGUAGE, "en"); // default English
        setLocale(lang);

        setContentView(R.layout.activity_main_dashboard); // replace with your XML filename

        // Initialize buttons
        btnUser = findViewById(R.id.btnUser);
        btnPolice = findViewById(R.id.btnPolice);
        btnAdmin = findViewById(R.id.btnAdmin);

        // Initialize spinner
        spinnerLanguage = findViewById(R.id.spinnerLanguage);

        String[] languages = {"English", "हिन्दी"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        // Set current selection
        spinnerLanguage.setSelection(lang.equals("hi") ? 1 : 0);

        spinnerLanguage.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedLang = position == 1 ? "hi" : "en";
                if (!selectedLang.equals(preferences.getString(KEY_LANGUAGE, "en"))) {
                    // Save selection
                    preferences.edit().putString(KEY_LANGUAGE, selectedLang).apply();
                    // Change language
                    setLocale(selectedLang);
                    // Reload activity
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Set click listeners
        btnUser.setOnClickListener(v -> {
            startActivity(new Intent(MainDashboard.this, UserDashboard.class));
        });

        btnPolice.setOnClickListener(v -> {
            startActivity(new Intent(MainDashboard.this, LoginActivity.class));
        });

        btnAdmin.setOnClickListener(v -> {
            startActivity(new Intent(MainDashboard.this, AdminLogin.class));
        });
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
