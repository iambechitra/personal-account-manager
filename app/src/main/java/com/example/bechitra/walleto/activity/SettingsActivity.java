package com.example.bechitra.walleto.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bechitra.walleto.DatabaseHelper;
import com.example.bechitra.walleto.R;
import com.example.bechitra.walleto.dialog.ResetDialog;
import com.example.bechitra.walleto.dialog.listener.OnResetListener;
import com.example.bechitra.walleto.viewmodel.SettingActivityViewModel;

public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.resetWalletSwitch) Switch resetWalletSwitch;

    private SettingActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        viewModel = new ViewModelProvider(this).get(SettingActivityViewModel.class);

        resetWalletSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ResetDialog resetDialog = new ResetDialog();
                    resetDialog.show(getSupportFragmentManager(), "TAG");
                    resetDialog.setOnResetListener(action -> {
                        if(action) {
                            viewModel.reset();
                            toastMassage("Action Successful");
                            resetWalletSwitch.setChecked(false);
                        } else {
                            toastMassage("Action not Performed");
                            resetWalletSwitch.setChecked(false);
                        }
                    });
                }
            }
        });
    }

    private void toastMassage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
