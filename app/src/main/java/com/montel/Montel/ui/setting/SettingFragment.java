package com.montel.Montel.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.montel.Montel.R;
import com.montel.Montel.utils.ApplicationService;
import com.montel.Montel.utils.SharedPref;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends Fragment {

    Context context;
    boolean mNotification = false;

    @BindView(R.id.switchNotif)
    SwitchCompat switchCompat;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        context = this.getContext();
        ButterKnife.bind(this, root);



        mNotification = SharedPref.read(SharedPref.NOTIFICATION, false);

        if (mNotification == true){
            switchCompat.setChecked(true);
        }else {
            switchCompat.setChecked(false);
        }


        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled

                    SharedPref.write(SharedPref.NOTIFICATION, isChecked);
                } else {
                    // The toggle is disabled
                    SharedPref.write(SharedPref.NOTIFICATION, isChecked);


                }
            }
        });




        return root;
    }

}