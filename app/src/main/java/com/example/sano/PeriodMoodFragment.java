package com.example.sano;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.EventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class PeriodMoodFragment extends AppCompatActivity {
    private ConstraintLayout periodLayout;
    private ConstraintLayout moodLayout;
    private CheckBox crampCheckbox,bloatingCheckbox,pimplesCheckbox,headacheCheckbox,fatigueCheckbox,insomniaCheckbox, YesIntercourseCheckbox, NoIntercourseCheckbox, YesPeriodCheckbox, NoPeriodCheckbox;
    private RadioButton happyRB,sadRB,angryRB,neutralRB,sickRB;
    private RadioButton walkingRB,swimmingRB,tennisRB,cyclingRB,basketballRB;
    private RadioButton gameRB,meditationRB,movieRB,partyRB,readingRB;
    periodMood periodMood=new periodMood();
    List<String> symptom = new ArrayList<>();
    FirebaseFirestore firestore;

    public PeriodMoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate the view for fragment
        //View view = inflater.inflate(R.layout.fragment_period_mood, container, false);
        setContentView(R.layout.fragment_period_mood);
        //display date
        TextView showSelectedDate = findViewById(R.id.showDate);
        showSelectedDate.setText(calendarFragment.clickedDate);

        //convert String date to localDate and save to class
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");
        LocalDate localDate = LocalDate.parse(calendarFragment.clickedDate, formatter);
        periodMood.setDate(localDate);

        //declare xml symptoms checkbox
        crampCheckbox = findViewById(R.id.checkboxCramp);
        bloatingCheckbox = findViewById(R.id.checkboxBloating);
        pimplesCheckbox = findViewById(R.id.checkBoxPimples);
        headacheCheckbox = findViewById(R.id.checkboxHeadache);
        fatigueCheckbox = findViewById(R.id.checkboxFatigue);
        insomniaCheckbox = findViewById(R.id.checkboxInsomnia);

        //declare xml intercourse checkbox
        YesIntercourseCheckbox = findViewById(R.id.checkboxYes);
        NoIntercourseCheckbox = findViewById(R.id.checkboxNo);

        //declare xml Period checkbox
        YesPeriodCheckbox = findViewById(R.id.PeriodCheckboxYes);
        NoPeriodCheckbox = findViewById(R.id.PeriodCheckboxNo);

        //mood layout action
        happyRB = findViewById(R.id.happyRadioButton);
        sadRB = findViewById(R.id.sadRadioButton);
        angryRB = findViewById(R.id.angryRadioButton);
        neutralRB = findViewById(R.id.neutralRadioButton);
        sickRB = findViewById(R.id.sickRadioButton);

        walkingRB = findViewById(R.id.walkingRadioButton);
        swimmingRB = findViewById(R.id.swimmingRadioButton);
        tennisRB = findViewById(R.id.tennisRadioButton);
        cyclingRB = findViewById(R.id.cyclingRadioButton);
        basketballRB = findViewById(R.id.basketballRadioButton);

        gameRB = findViewById(R.id.gameRadioButton);
        meditationRB = findViewById(R.id.meditationRadioButton);
        movieRB = findViewById(R.id.movieRadioButton);
        partyRB = findViewById(R.id.partyRadioButton);
        readingRB = findViewById(R.id.readingRadioButton);

        firestore = FirebaseFirestore.getInstance();

        //display Mood layout
        Button displayMoodButton = findViewById(R.id.moodBtn);
        //end on click
        displayMoodButton.setOnClickListener(v -> {
            displayMood(v);
            DocumentReference documentReference = firestore.collection("periodMood").document(localDate.toString());

            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    if(documentSnapshot.exists()){
                        //retrieval
                        if(documentSnapshot.getString("mood")!=null){
                            periodMood.setMood(documentSnapshot.getString("mood"));
                            periodMood.setSport(documentSnapshot.getString("sport"));
                            periodMood.setActivity(documentSnapshot.getString("activity"));
                            //retrieval data of mood
                            if(periodMood.getMood().equals("happy")){
                                happyRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        happyRB.performClick();
                                    }
                                });
                            }
                            else if (periodMood.getMood().equals("sad")){
                                sadRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sadRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getMood().equals("angry")){
                                angryRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        angryRB.performClick();
                                    }
                                });
                            }
                            else if (periodMood.getMood().equals("neutral")){
                                neutralRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        neutralRB.performClick();
                                    }
                                });
                            }
                            else{
                                sickRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        sickRB.performClick();
                                    }
                                });
                            }

                            //retrieval of sport
                            if(periodMood.getSport().equals("walking")){
                                walkingRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        walkingRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getSport().equals("swimming")){
                                swimmingRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swimmingRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getSport().equals("tennis")){
                                tennisRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tennisRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getSport().equals("cycling")){
                                cyclingRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        cyclingRB.performClick();
                                    }
                                });
                            }
                            else{
                                basketballRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        basketballRB.performClick();
                                    }
                                });
                            }

                            //retrieval data of activity
                            if(periodMood.getActivity().equals("game")){
                                gameRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        gameRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getActivity().equals("meditation")){
                                meditationRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        meditationRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getActivity().equals("movie")){
                                movieRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        movieRB.performClick();
                                    }
                                });
                            }
                            else if(periodMood.getActivity().equals("party")){
                                partyRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        partyRB.performClick();
                                    }
                                });
                            }
                            else{
                                readingRB.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        readingRB.performClick();
                                    }
                                });
                            }
                        }
                    }//end document reference exists

                    //click action for mood
                    //mood Layout
                    //radiogroup of mood consists of 6 radio button(happy, sad, angry, neutral, sick)
                    //any is choose ->save into mood(periodMood class)
                    RadioGroup moodRadioGroup = findViewById(R.id.moodRadioGroup);
                    moodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            switch ((checkedId)) {
                                case R.id.happyRadioButton:
                                    if (happyRB.isChecked()) {
                                        happyRB.setChecked(true);
                                        periodMood.setMood("happy");
                                    }
                                    break;
                                case R.id.sadRadioButton:
                                    if (sadRB.isChecked()) {
                                        sadRB.setChecked(true);
                                        periodMood.setMood("sad");
                                    }
                                    break;
                                case R.id.angryRadioButton:
                                    if (angryRB.isChecked()) {
                                        angryRB.setChecked(true);
                                        periodMood.setMood("angry");
                                    }
                                    break;
                                case R.id.neutralRadioButton:
                                    if (neutralRB.isChecked()) {
                                        neutralRB.setChecked(true);
                                        periodMood.setMood("neutral");
                                    }
                                    break;
                                case R.id.sickRadioButton:
                                    if (sickRB.isChecked()) {
                                        sickRB.setChecked(true);
                                        periodMood.setMood("sick");
                                    }
                                    break;
                            }
                        }
                    });

                    RadioGroup sportRadioGroup = findViewById(R.id.sportRadioGroup);
                    if (sportRadioGroup != null) {
                        sportRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.walkingRadioButton:
                                        if (walkingRB.isChecked()) {
                                            walkingRB.setChecked(true);
                                            periodMood.setSport("walking");
                                        }
                                        break;
                                    case R.id.swimmingRadioButton:
                                        if (swimmingRB.isChecked()) {
                                            swimmingRB.setChecked(true);
                                            periodMood.setSport("swimming");
                                        }
                                        break;
                                    case R.id.tennisRadioButton:
                                        if (tennisRB.isChecked()) {
                                            tennisRB.setChecked(true);
                                            periodMood.setSport("tennis");
                                        }
                                        break;
                                    case R.id.cyclingRadioButton:
                                        if (cyclingRB.isChecked()) {
                                            cyclingRB.setChecked(true);
                                            periodMood.setSport("cycling");
                                        }
                                        break;
                                    case R.id.basketballRadioButton:
                                        if (basketballRB.isChecked()) {
                                            basketballRB.setChecked(true);
                                            periodMood.setSport("basketball");
                                        }
                                }
                            }
                        });
                    }
                    RadioGroup activityRadioGroup = findViewById(R.id.activityRadioGroup);
                    if (activityRadioGroup != null) {
                        activityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                switch (checkedId) {
                                    case R.id.gameRadioButton:
                                        if (gameRB.isChecked()) {
                                            gameRB.setChecked(true);
                                            periodMood.setActivity("game");
                                        }
                                        break;
                                    case R.id.meditationRadioButton:
                                        if (meditationRB.isChecked()) {
                                            meditationRB.setChecked(true);
                                            periodMood.setActivity("meditation");
                                        }
                                        break;
                                    case R.id.movieRadioButton:
                                        if (movieRB.isChecked()) {
                                            movieRB.setChecked(true);
                                            periodMood.setActivity("movie");
                                        }

                                        break;
                                    case R.id.partyRadioButton:
                                        if (partyRB.isChecked()) {
                                            partyRB.setChecked(true);
                                            periodMood.setActivity("party");
                                        }

                                        break;
                                    case R.id.readingRadioButton:
                                        if (readingRB.isChecked()) {
                                            readingRB.setChecked(true);
                                            periodMood.setActivity("reading");
                                        }
                                        break;
                                }//end activity radio group
                            }//end activityRadioGroup.setOnCheckedChangeListener
                        });
                    }
                }//end on event
            });//end snapshot listener
        });// end mood layout

        //display Period layout
        Button displayPeriodButton = findViewById(R.id.periodBtn);
        displayPeriodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPeriod(v);
                String dateDocumentReference;
                DocumentReference documentReference = firestore.collection("periodMood").document(localDate.toString());
                //data retrieval
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (documentSnapshot.exists()) {

                            //get array of strings from symptom
                            List<String> map = (List<String>) documentSnapshot.get("symptom");
                            periodMood.setIntercourse(documentSnapshot.getString("intercourse"));
                            periodMood.setPeriod(documentSnapshot.getString("period"));
                            System.out.println(periodMood.getIntercourse());
                            System.out.println(periodMood.getPeriod());

                            if(map.size()!=0) {
                                for (int x = 0; x < map.size(); x++) {
                                    System.out.println(map.get(x));
                                    if (map.get(x).equals("cramp")) {
                                        crampCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                crampCheckbox.performClick();
                                            }
                                        });
                                    } else if (map.get(x).equals("bloating")) {
                                        bloatingCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                bloatingCheckbox.performClick();
                                            }
                                        });
                                    } else if (map.get(x).equals("pimples")) {
                                        pimplesCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pimplesCheckbox.performClick();
                                            }
                                        });
                                    } else if (map.get(x).equals("pimples")) {
                                        pimplesCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                pimplesCheckbox.performClick();
                                            }
                                        });
                                    } else if (map.get(x).equals("headache")) {
                                        headacheCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                headacheCheckbox.performClick();
                                            }
                                        });
                                    } else if (map.get(x).equals("fatigue")) {
                                        fatigueCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                fatigueCheckbox.performClick();
                                            }
                                        });
                                    } else {
                                        insomniaCheckbox.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                insomniaCheckbox.performClick();
                                            }
                                        });
                                    }
                                    //map.clear();
                                }//end symptom array list
                                map.clear();
                                //intercourse Checkbox
                                if (periodMood.getIntercourse().equals("Yes")) {
                                    YesIntercourseCheckbox.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            YesIntercourseCheckbox.performClick();
                                        }
                                    });
                                } else {//no
                                    NoIntercourseCheckbox.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            NoIntercourseCheckbox.performClick();
                                        }
                                    });
                                }

                                //period checkbox
                                if (periodMood.getPeriod().equals("Yes")) {
                                    YesPeriodCheckbox.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            YesPeriodCheckbox.performClick();
                                        }
                                    });
                                } else {//no
                                    NoPeriodCheckbox.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            NoPeriodCheckbox.performClick();
                                        }
                                    });
                                }
                            }//end of map size()

                        }//end if snapshot exist
                        crampCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (crampCheckbox.isChecked()) {
                                    crampCheckbox.setChecked(true);
                                    symptom.add("cramp");
                                } else {
                                    crampCheckbox.setChecked(false);
                                    symptom.remove("cramp");
                                }
                            }

                        });
                        bloatingCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (bloatingCheckbox.isChecked()) {
                                    bloatingCheckbox.setChecked(true);
                                    symptom.add("bloating");
                                } else {
                                    bloatingCheckbox.setChecked(false);
                                    symptom.remove("bloating");
                                }
                            }
                        });
                        pimplesCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (pimplesCheckbox.isChecked()) {
                                    pimplesCheckbox.setChecked(true);
                                    symptom.add("pimples");
                                } else {
                                    pimplesCheckbox.setChecked(false);
                                    symptom.remove("pimples");
                                }
                            }
                        });

                        headacheCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (headacheCheckbox.isChecked()) {
                                    headacheCheckbox.setChecked(true);
                                    symptom.add("headache");
                                } else {
                                    headacheCheckbox.setChecked(false);
                                    symptom.remove("headache");
                                }
                            }
                        });

                        fatigueCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (fatigueCheckbox.isChecked()) {
                                    fatigueCheckbox.setChecked(true);
                                    symptom.add("fatigue");
                                } else {
                                    fatigueCheckbox.setChecked(false);
                                    symptom.remove("fatigue");
                                }
                            }
                        });

                        insomniaCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (insomniaCheckbox.isChecked()) {
                                    insomniaCheckbox.setChecked(true);
                                    symptom.add("insomnia");
                                } else {
                                    insomniaCheckbox.setChecked(false);
                                    symptom.remove("insomnia");
                                }
                            }
                        });

                        //if yes is ticked-"yes" save into intercourse(periodMood class)
                        //if yes is unticked-"no" save into intercourse(periodMood class)
                        YesIntercourseCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (YesIntercourseCheckbox.isChecked()) {
                                    YesIntercourseCheckbox.setChecked(true);
                                    NoIntercourseCheckbox.setChecked(false);
                                    periodMood.setIntercourse("Yes");
                                } else {
                                    periodMood.setIntercourse("No");
                                }
                            }
                        });

                        NoIntercourseCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (NoIntercourseCheckbox.isChecked()) {
                                    NoIntercourseCheckbox.setChecked(true);
                                    YesIntercourseCheckbox.setChecked(false);
                                    periodMood.setIntercourse("No");
                                } else {
                                    periodMood.setIntercourse("Yes");
                                }
                            }
                        });

                        //if yes is ticked-"yes" save into period(periodMood class)
                        //if yes is unticked-"no" save into period(periodMood class)
                        YesPeriodCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (YesPeriodCheckbox.isChecked()) {
                                    YesPeriodCheckbox.setChecked(true);
                                    NoPeriodCheckbox.setChecked(false);
                                    periodMood.setPeriod("Yes");
                                } else {
                                    periodMood.setPeriod("No");
                                }
                            }
                        });

                        NoPeriodCheckbox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (NoPeriodCheckbox.isChecked()) {
                                    NoPeriodCheckbox.setChecked(true);
                                    YesPeriodCheckbox.setChecked(false);
                                    periodMood.setPeriod("No");
                                } else {
                                    periodMood.setPeriod("Yes");
                                }
                            }
                        });
                    }//end onEvent
                });//end snapshot listener
            }//end on click
        });//end setOn listener

        //save into firebase
        Button periodSaveBtn = findViewById(R.id.periodSaveButton);
        //once save btn(period layout)is clicked ->save into firebase
        periodSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = periodMood.getDate().toString();
                DocumentReference documentReference = firestore.collection("periodMood").document(date);
                Map<String, Object> periodAndMood = new HashMap<>();
                //put into firebase from periodMood class method
                if (symptom != null) {
                    //add into firebase
                    periodAndMood.put("date",date);
                    periodAndMood.put("symptom", symptom);
                    periodAndMood.put("intercourse", periodMood.getIntercourse());
                    periodAndMood.put("period", periodMood.getPeriod());
                    firestore.collection("periodMood").document(date).update(periodAndMood);
                    documentReference.set(periodAndMood, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void unused) {
                            Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        //if the date to save is existed, then it will update the field
        //if the date is existed and the field data is existed, field data will update accordingly
        Button moodSaveBtn = findViewById(R.id.moodSaveButton);
        moodSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //put into firebase from periodMood class method
                String date = periodMood.getDate().toString();
                DocumentReference documentReference = firestore.collection("periodMood").document(date);
                Map<String, Object> periodAndMood = new HashMap<>();
                periodAndMood.put("date",date);
                periodAndMood.put("mood", periodMood.getMood());
                periodAndMood.put("sport", periodMood.getSport());
                periodAndMood.put("activity", periodMood.getActivity());
                firestore.collection("periodMood").document(date).update(periodAndMood);
                documentReference.set(periodAndMood, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //retrieval data and show into period and mood layout
    }

    public void displayPeriod(View view)
    {
        periodLayout=findViewById(R.id.period_layout);
        moodLayout=findViewById(R.id.mood_layout);

        moodLayout.setVisibility(View.GONE);
        periodLayout.setVisibility(View.VISIBLE);
    }

    public void displayMood(View view)
    {
        periodLayout=findViewById(R.id.period_layout);
        moodLayout=findViewById(R.id.mood_layout);

        periodLayout.setVisibility(View.GONE);
        moodLayout.setVisibility(View.VISIBLE);
    }
}