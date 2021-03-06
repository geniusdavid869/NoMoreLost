package com.project.android.nctu.nomorelost;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.util.ArrayList;

/**
 * Created by chichunchen on 5/25/15.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String HOMEPAGE = "http://github.com/chichunchen";
    private static final String SOURCE_CODE = "https://github.com/chichunchen/nomorelost";
    private static final String PLAY_STORE = "http://play.google.com/store/apps/";
    private static final ArrayList<String> PREFERENCE_ITEM = new ArrayList<String>(){{
        add(0, "about");
        add(1, "rating");
        add(2, "visit");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);
        initPreference();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    private void initPreference() {
        for(int i = 0 ; i < PREFERENCE_ITEM.size() ; i ++){
            Preference preference = (Preference) findPreference(PREFERENCE_ITEM.get(i));
            preference.setOnPreferenceClickListener(clickPref(i, getActivity()));
        }
    }

    private Preference.OnPreferenceClickListener clickPref(final int key, final Context context){
        return new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                switch (key) {
                    case 0:
                        setAbout(context);
                        break;
                    case 1:
                        openUrl(PLAY_STORE);
                        break;
                    case 2:
                        openUrl(HOMEPAGE);
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
    }

    private void setAbout(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.about));
        builder.setMessage(getString(R.string.about_app));
        builder.setNegativeButton(getString(R.string.back), null);
        builder.setPositiveButton(getString(R.string.get_source_code), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openUrl(SOURCE_CODE);
            }
        });
        builder.show();
    }

    public void openUrl(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        Intent chooser = Intent.createChooser(intent, getString(R.string.choose_service));
        startActivity(chooser);
    }
}
