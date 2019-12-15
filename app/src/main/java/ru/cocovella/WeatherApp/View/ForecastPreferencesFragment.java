package ru.cocovella.WeatherApp.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Pattern;

import ru.cocovella.WeatherApp.Model.DataLoader;
import ru.cocovella.WeatherApp.Model.DataParser;
import ru.cocovella.WeatherApp.Model.Settings;
import ru.cocovella.WeatherApp.R;

import static ru.cocovella.WeatherApp.Model.Keys.BAROMETER_KEY;
import static ru.cocovella.WeatherApp.Model.Keys.CITIES_LIST;
import static ru.cocovella.WeatherApp.Model.Keys.CITY_KEY;
import static ru.cocovella.WeatherApp.Model.Keys.HUMIDITY_KEY;
import static ru.cocovella.WeatherApp.Model.Keys.SHARED_PREFS;
import static ru.cocovella.WeatherApp.Model.Keys.WIND_KEY;


public class ForecastPreferencesFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextInputEditText city;
    private RecyclerView recyclerView;
    private CheckBox humidityCB;
    private CheckBox windCB;
    private CheckBox barometerCB;
    private CitiesListAdapter adapter;


    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setRetainInstance(true);
        sharedPreferences = Objects.requireNonNull(getActivity())
                .getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return inflater.inflate(R.layout.fragment_forecast_preferences, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initViews();
        inflateViews();
    }

    private void initViews() {
        city = Objects.requireNonNull(getView()).findViewById(R.id.cityInput);
        humidityCB = getView().findViewById(R.id.humidityCB);
        windCB = getView().findViewById(R.id.windCB);
        barometerCB = getView().findViewById(R.id.barometerCB);
        recyclerView = getView().findViewById(R.id.recycler_view);
    }

    private void inflateViews() {
        city.setText(sharedPreferences.getString(CITY_KEY, ""));
        humidityCB.setChecked(sharedPreferences.getBoolean(HUMIDITY_KEY, false));
        windCB.setChecked(sharedPreferences.getBoolean(WIND_KEY, false));
        barometerCB.setChecked(sharedPreferences.getBoolean(BAROMETER_KEY, false));
        setSpellingCheck();
        setRecyclerView();
        setApplyButton();
    }

    private void setSpellingCheck() {
        city.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus || Objects.requireNonNull(city.getText()).toString().isEmpty()) return;
            Pattern pattern = Pattern.compile(getString(R.string.checkCityNameRegex));
            String value = Objects.requireNonNull(city.getText()).toString();
            String message = getString(R.string.InputErrorMessage);
            city.setError(pattern.matcher(value).matches() ? null : message);
        });
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        CitiesListAdapter adapter = new CitiesListAdapter(Settings.getInstance().getCitiesChoice());
        adapter.setOnItemClickListener((itemName, position) -> city.setText(itemName));
        recyclerView.setAdapter(adapter);
    }

    private void setApplyButton() {
        Objects.requireNonNull(getView()).findViewById(R.id.applyButton).setOnClickListener(v -> {
            savePreferences();
            new Thread(() -> {
                JSONObject jsonObject = new DataLoader().load(sharedPreferences.getString(CITY_KEY, ""));
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> new DataParser(jsonObject));
            }).start();
        });
    }

    private void savePreferences() {
        String recentCity = Objects.requireNonNull(city.getText()).toString();
        ArrayList<String> citiesChoice = Settings.getInstance().getCitiesChoice();
        editor.putString(CITY_KEY, recentCity);
        editor.putBoolean(HUMIDITY_KEY, humidityCB.isChecked());
        editor.putBoolean(WIND_KEY, windCB.isChecked());
        editor.putBoolean(BAROMETER_KEY, barometerCB.isChecked());
        editor.putStringSet(CITIES_LIST, new HashSet<>(citiesChoice));
        editor.commit();
    }

}
