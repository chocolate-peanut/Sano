package com.example.sano;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Objects;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        Button bt_hw,bt_by, bt_cd, bt_fb;
        EditText txt_weight, txt_height, txt_birth_year;
        FirebaseFirestore fStore;
        fStore = FirebaseFirestore.getInstance();
        bt_hw = rootView.findViewById(R.id.bt_hw);
        bt_by =rootView.findViewById(R.id.bt_by);
        txt_weight = rootView.findViewById(R.id.txt_weight);
        txt_height = rootView.findViewById(R.id.txt_height);
        txt_birth_year = rootView.findViewById(R.id.txt_birth_year);
        bt_cd = rootView.findViewById(R.id.bt_cd);
        bt_fb = rootView.findViewById(R.id.bt_fb);

        DocumentReference docRef = fStore.collection("userInfo").document("info");
        DocumentReference documentRef = fStore.collection("birthYear").document("by");

        //direct to Count down page
        bt_cd.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), CountDown.class);
            startActivity(intent);
        });

        //direct to feedback page
        bt_fb.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Feedback.class);
            startActivity(intent);
        });

        //Retrieve data from firebase and set text of height and weight
        docRef.addSnapshotListener((value, error) -> {
            assert value != null;
            if (value.exists()) {
                if(value.getDouble("Height")!=null){
                    txt_height.setText(String.valueOf(Objects.requireNonNull(value.getDouble("Height")).doubleValue()));
                }
                if(value.getDouble("Weight")!=null){
                    txt_weight.setText(String.valueOf(Objects.requireNonNull(value.getDouble("Weight")).doubleValue()));
                }
            }
        });

        //Retrieve data from firebase and set text of birth year
        documentRef.addSnapshotListener((value, error) -> {
            assert value != null;
            if (value.exists()) {
                if(value.getDouble("birthYear")!=null){
                    double by_txt;
                    by_txt = value.getDouble("birthYear").doubleValue();
                    txt_birth_year.setText(String.format("%.0f", by_txt));
                }
            }
        });

        //Button to set height and weight
        bt_hw.setOnClickListener(view -> {
            String hei = txt_height.getText().toString();
            String wei = txt_weight.getText().toString();
            //Validation to avoid empty field
            if(hei.isEmpty() || wei.isEmpty()) {
                makeText(view.getContext(), "Height and weight cannot be empty",
                        LENGTH_SHORT).show();
            }
            else {
                try {
                    double height_save;
                    double weight_save;

                    //The value is round off to 2 decimal points
                    BigDecimal height_d = new BigDecimal(Double.parseDouble(hei)).setScale(2, RoundingMode.HALF_UP);
                    height_save = height_d.doubleValue();
                    BigDecimal weight_d = new BigDecimal(Double.parseDouble(wei)).setScale(2, RoundingMode.HALF_UP);
                    weight_save = weight_d.doubleValue();

                    //Value validation of height and weight
                    if((height_save<300) && (height_save>100) && (weight_save>25) && (weight_save<300)) {
                        // save height & weight to the firebase
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("Height", height_save);
                        userInfo.put("Weight", weight_save);
                        docRef.set(userInfo).addOnSuccessListener(unused ->
                                makeText(view.getContext(), "Added successfully",
                                        LENGTH_SHORT).show()).addOnFailureListener
                                (e -> makeText(view.getContext(),
                                        "Please try again.", LENGTH_SHORT).show());
                    }
                    else {
                        makeText(view.getContext(), "Invalid input, please try again.",
                                LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    makeText(view.getContext(), "Invalid input, please try again.",
                            LENGTH_SHORT).show();
                }
            }
        });

        //Button to set birth year
        bt_by.setOnClickListener(view -> {
            String birth_year = txt_birth_year.getText().toString();
            //Current year
            int year = Calendar.getInstance().get(Calendar.YEAR);

            //Validation to avoid empty field
            if(birth_year.isEmpty()) {
                makeText(view.getContext(), "Birth year cannot be empty",
                        LENGTH_SHORT).show();
            }
            else {
                try {
                    int by = Integer.parseInt(birth_year);
                    //Value validation of height and weight
                    if (((year - by)<60) && ((year - by)>8)) {
                        // save birth year to the firebase
                        Map<String, Object> birthYear = new HashMap<>();
                        birthYear.put("birthYear", by);
                        documentRef.set(birthYear).addOnSuccessListener(
                                        unused ->
                                                makeText(view.getContext(), "Added successfully",
                                                        LENGTH_SHORT).show())
                                .addOnFailureListener(e -> makeText
                                        (view.getContext(), "Please try again.", LENGTH_SHORT).show());
                    }
                    else {
                        makeText(view.getContext(), "Invalid input, please try again.",
                                LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    makeText(view.getContext(), "Invalid input, Birth year is an integer",
                            LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}