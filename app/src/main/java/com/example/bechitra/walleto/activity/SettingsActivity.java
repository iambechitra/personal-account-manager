package com.example.bechitra.walleto.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import com.example.bechitra.walleto.databinding.ActivitySettingsBinding;
import com.example.bechitra.walleto.dialog.ResetDialog;
import com.example.bechitra.walleto.viewmodel.SettingActivityViewModel;

public class SettingsActivity extends AppCompatActivity {
    private SettingActivityViewModel viewModel;
    private ActivitySettingsBinding viewBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(viewBind.getRoot());

        viewModel = new ViewModelProvider(this).get(SettingActivityViewModel.class);

        viewBind.resetWalletSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                ResetDialog resetDialog = new ResetDialog();
                resetDialog.show(getSupportFragmentManager(), "TAG");
                resetDialog.setOnResetListener(action -> {
                    if(action) {
                        viewModel.reset();
                        toastMassage("Action Successful");
                        viewBind.resetWalletSwitch.setChecked(false);
                    } else {
                        toastMassage("Action not Performed");
                        viewBind.resetWalletSwitch.setChecked(false);
                    }
                });
            }
        });
    }

    private void toastMassage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
