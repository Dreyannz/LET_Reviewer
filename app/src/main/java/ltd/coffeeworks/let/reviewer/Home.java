package ltd.coffeeworks.let.reviewer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.MultiplePulseRing;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import akndmr.github.io.colorprefutil.ColorPrefUtil;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;

public class Home extends AppCompatActivity implements View.OnClickListener{
    private TextView Title;
    private TextView Subtitle;
    private static final String URL = "https://pastebin.com/raw/vuRVmm7b";
    private TextView titleGenEd;
    private TextView titleProfEd;
    private TextView titleDload;
    private TextView descGened;
    private TextView descProfEd;
    private TextView descDload;
    public static final String PREF_COLOR = "pref_color";
    public static final String THEME_SELECTED = "theme_selected";
    public static final String BACKGROUND = "background";
    public static final String HEADER = "header";
    public static final String BOX = "box";
    public static final String CORRECT = "correct";
    public static final String INCORRECT = "incorrect";
    public static final String TEXTCOLOR = "textcolor";
    public static final String ICONCOLOR = "iconcolor";
    public static final String VERSION_CONTROL = "version_control";
    public static final String FIRST_DOWNLOAD = "first_download";
    public static final String FEATURES = "features";
    int colorBackground, colorHeader, colorTextColor, colorButton, colorBox, colorBoxCorrect, colorBoxIncorrect, features;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    private LinearLayout linearLayout_GenEd, linearLayout_ProfEd, linearLayout_Dload;
    private RelativeLayout background, appHeader;
    private ImageView arrowG, arrowP, arrowD;
    private boolean iconsColor;
    private ImageView darktheme, lighttheme;

    private GenEd_Helper GenEd_Helper;
    private List<GenEd_Question> listG;
    private ProfEd_Helper ProfEd_Helper;
    private List<ProfEd_Question> listP;
    private String versionControl;
    private Boolean isFirstDownload, isFeaturesShown;

    private GuideView mGuideView;
    private GuideView.Builder builder;

    private View view1, view2, view3, view4, view5;
    private AlertDialog Loading, Update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSharedPreferences = getSharedPreferences(PREF_COLOR, MODE_PRIVATE);
        int themeSelected = mSharedPreferences.getInt(THEME_SELECTED, R.style.AppTheme_Light);
        ColorPrefUtil.changeThemeStyle(this, themeSelected);


        setContentView(R.layout.home);
        customActionBar();
        initLayout();


        editor = mSharedPreferences.edit();
        colorBackground = mSharedPreferences.getInt(BACKGROUND, R.color.background_light);
        colorTextColor = mSharedPreferences.getInt(TEXTCOLOR, R.color.text_light);
        colorBox = mSharedPreferences.getInt(BOX,  R.drawable.box_light);
        colorBoxCorrect = mSharedPreferences.getInt(CORRECT, R.drawable.box_correct_light);
        colorBoxIncorrect = mSharedPreferences.getInt(INCORRECT, R.drawable.box_incorrect_light);

        isFirstDownload = mSharedPreferences.getBoolean(FIRST_DOWNLOAD, true);
        isFeaturesShown = mSharedPreferences.getBoolean(FEATURES, false);
        versionControl = mSharedPreferences.getString(VERSION_CONTROL, "1");

        iconsColor = mSharedPreferences.getBoolean(ICONCOLOR, true);
        if(iconsColor){
            arrowG.setImageResource(R.drawable.r_arrow_black);
            arrowP.setImageResource(R.drawable.r_arrow_black);
            arrowD.setImageResource(R.drawable.r_arrow_black);
            darktheme.setEnabled(true);
            lighttheme.setEnabled(false);
        }else{
            arrowG.setImageResource(R.drawable.r_arrow_white);
            arrowP.setImageResource(R.drawable.r_arrow_white);
            arrowD.setImageResource(R.drawable.r_arrow_white);
            darktheme.setEnabled(false);
            lighttheme.setEnabled(true);
        }

        ColorPrefUtil.changeBackgroundColorOfSingleView(this, background, colorBackground);
        ColorPrefUtil.changeTextColorOfChildViews(this, (LinearLayout)findViewById(R.id.title_holder), colorTextColor, colorTextColor);
        ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, linearLayout_GenEd, colorBox);
        ColorPrefUtil.changeTextColorOfSingleView(this, titleGenEd, colorTextColor, colorTextColor);
        ColorPrefUtil.changeTextColorOfSingleView(this, descGened, colorTextColor, colorTextColor);
        ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, linearLayout_ProfEd, colorBox);
        ColorPrefUtil.changeTextColorOfSingleView(this, titleProfEd, colorTextColor, colorTextColor);
        ColorPrefUtil.changeTextColorOfSingleView(this, descProfEd, colorTextColor, colorTextColor);
        ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, linearLayout_Dload, colorBox);
        ColorPrefUtil.changeTextColorOfSingleView(this, titleDload, colorTextColor, colorTextColor);
        ColorPrefUtil.changeTextColorOfSingleView(this, descDload, colorTextColor, colorTextColor);

        if(!isFeaturesShown){
            Features();
        }

        //Admob_Banner();

    }
    private void Features()
    {
        view1 = findViewById(R.id.title_holder);
        view2 = findViewById(R.id.themeChanger);
        view3 = findViewById(R.id.linearlayout_gened);
        view4 = findViewById(R.id.linearlayout_profed);
        view5 = findViewById(R.id.linearlayout_dload);
        editor.putBoolean(FEATURES, true);
        editor.commit();

        builder = new GuideView.Builder(this)
                .setContentText("About LET REVIEWER")
                .setContentTextSize(16)
                .setGravity(GuideView.Gravity.center)
                .setDismissType(GuideView.DismissType.anywhere)
                .setTargetView(view1)
                .setGuideListener(new GuideView.GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        switch (view.getId()) {
                            case R.id.title_holder:
                                builder.setTargetView(view2).setContentText("Theme Changer").build();
                                break;
                            case R.id.themeChanger:
                                builder.setTargetView(view3).setContentText("General Education Questions").build();
                                break;
                            case R.id.linearlayout_gened:
                                builder.setTargetView(view4).setContentText("Professional Education Questions").build();
                                break;
                            case R.id.linearlayout_profed:
                                builder.setTargetView(view5).setContentText("Download and Update Files").build();
                                break;
                            case R.id.linearlayout_dload:
                                return;
                        }
                        mGuideView = builder.build();
                        mGuideView.show();
                    }
                });


        mGuideView = builder.build();
        mGuideView.show();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.theme_dark:
                editor.putInt(THEME_SELECTED, R.style.AppTheme_Dark);
                editor.putInt(BACKGROUND, R.color.background_dark);
                editor.putInt(TEXTCOLOR, R.color.text_dark);
                editor.putInt(BOX, R.drawable.box_dark);
                editor.putInt(CORRECT, R.drawable.box_correct_dark);
                editor.putInt(INCORRECT, R.drawable.box_incorrect_dark);
                editor.putBoolean(ICONCOLOR,false);
                iconsColor = false;
                if(iconsColor){
                    arrowG.setImageResource(R.drawable.r_arrow_black);
                    arrowP.setImageResource(R.drawable.r_arrow_black);
                    arrowD.setImageResource(R.drawable.r_arrow_black);
                    darktheme.setEnabled(true);
                    lighttheme.setEnabled(false);
                }else{
                    arrowG.setImageResource(R.drawable.r_arrow_white);
                    arrowP.setImageResource(R.drawable.r_arrow_white);
                    arrowD.setImageResource(R.drawable.r_arrow_white);
                    darktheme.setEnabled(false);
                    lighttheme.setEnabled(true);
                }
                editor.commit();
                recreate();
                break;
            case R.id.theme_light:
                editor.putInt(THEME_SELECTED, R.style.AppTheme_Light);
                editor.putInt(BACKGROUND, R.color.background_light);
                editor.putInt(TEXTCOLOR, R.color.text_light);
                editor.putInt(BOX, R.drawable.box_light);
                editor.putInt(CORRECT, R.drawable.box_correct_light);
                editor.putInt(INCORRECT, R.drawable.box_incorrect_light);
                editor.putBoolean(ICONCOLOR,true);
                iconsColor = true;
                if(iconsColor){
                    arrowG.setImageResource(R.drawable.r_arrow_black);
                    arrowP.setImageResource(R.drawable.r_arrow_black);
                    arrowD.setImageResource(R.drawable.r_arrow_black);
                    darktheme.setEnabled(true);
                    lighttheme.setEnabled(false);
                }else{
                    arrowG.setImageResource(R.drawable.r_arrow_white);
                    arrowP.setImageResource(R.drawable.r_arrow_white);
                    arrowD.setImageResource(R.drawable.r_arrow_white);
                    darktheme.setEnabled(false);
                    lighttheme.setEnabled(true);
                }
                editor.commit();
                recreate();
                break;
            case R.id.app_header:
                dialog_about();
                break;
            case R.id.linearlayout_gened:
                GenEd_Helper = new GenEd_Helper(this);
                GenEd_Helper.getWritableDatabase();
                listG = GenEd_Helper.getAllOfTheQuestions();
                if(listG.size() != 0){
                    startActivity(new Intent(Home.this,GenEd_Activity.class));
                    finish();
                }else{
                    noFile();
                }
                break;
            case R.id.linearlayout_profed:
                ProfEd_Helper = new ProfEd_Helper(this);
                ProfEd_Helper.getWritableDatabase();
                listP = ProfEd_Helper.getAllOfTheQuestions();
                if(listP.size() != 0){
                    startActivity(new Intent(Home.this,ProfEd_Activity.class));
                    finish();
                }else{
                    noFile();
                }
                break;
            case R.id.linearlayout_dload:
                downloadData();
                break;
        }
    }


    private void customActionBar()
    {
        Typeface NexaBold = Typeface.createFromAsset(this.getAssets(), "NexaBold.otf");
        Typeface NexaLite = Typeface.createFromAsset(this.getAssets(), "NexaLight.otf");

        Title = (TextView) findViewById(R.id.title);
        Subtitle = (TextView) findViewById(R.id.subtitle);
        Title.setText(getString(R.string.app_name).toUpperCase());
        Subtitle.setText("Beta Test v1.0");
        Title.setTypeface(NexaBold);
        Subtitle.setTypeface(NexaLite);
    }
    private void initLayout()
    {
        Typeface NexaBold = Typeface.createFromAsset(this.getAssets(), "NexaBold.otf");
        Typeface NexaLite = Typeface.createFromAsset(this.getAssets(), "NexaLight.otf");
        background = (RelativeLayout) findViewById(R.id.background);
        appHeader = (RelativeLayout) findViewById(R.id.app_header);
        appHeader.setOnClickListener(this);
        linearLayout_GenEd = (LinearLayout) findViewById(R.id.linearlayout_gened);
        linearLayout_GenEd.setOnClickListener(this);
        linearLayout_ProfEd = (LinearLayout) findViewById(R.id.linearlayout_profed);
        linearLayout_ProfEd.setOnClickListener(this);
        linearLayout_Dload = (LinearLayout) findViewById(R.id.linearlayout_dload);
        linearLayout_Dload.setOnClickListener(this);

        titleGenEd = (TextView) findViewById(R.id.title_gened);
        descGened = (TextView) findViewById(R.id.desc_gened);
        titleProfEd = (TextView) findViewById(R.id.title_profed);
        descProfEd = (TextView) findViewById(R.id.desc_profed);
        titleDload = (TextView) findViewById(R.id.title_dload);
        descDload = (TextView) findViewById(R.id.desc_dload);
        titleGenEd.setText("GENERAL EDUCATION");
        descGened.setText("Questions ranges from the following subjects: English, Filipino, Mathematics, Science, Social Science, Information and Communication Technology. ");
        titleProfEd.setText("PROFESSIONAL EDUCATION");
        descProfEd.setText("Questions ranges from the following topics: Foundations of Education, Child and Adolescent Development, Principles and Theories of Learning and Motivation, Principles and Strategies of Teaching, Curriculum Development, Educational Technology, Assessment and Evaluation of Learning, Teaching Profession, Social Dimensions in Education/Developments in Education. ");
        titleDload.setText("DOWNLOAD/UPDATE FILES");
        descDload.setText("Download or Update local files to this application for your own usage. \nRequires Internet Connection. ");

        titleGenEd.setTypeface(NexaBold);
        descGened.setTypeface(NexaLite);
        titleProfEd.setTypeface(NexaBold);
        descProfEd.setTypeface(NexaLite);
        titleDload.setTypeface(NexaBold);
        descDload.setTypeface(NexaLite);

        arrowG = (ImageView)findViewById(R.id.arrow_gened);
        arrowP = (ImageView)findViewById(R.id.arrow_profed);
        arrowD = (ImageView)findViewById(R.id.arrow_dload);

        lighttheme = (ImageView)findViewById(R.id.theme_light);
        lighttheme.setOnClickListener(this);
        darktheme = (ImageView)findViewById(R.id.theme_dark);
        darktheme.setOnClickListener(this);
    }


    public void noFile(){
        View view = findViewById(R.id.linearlayout_dload);
        new GuideView.Builder(this)
                .setContentText("(File Missing) Download File Now")
                .setGravity(GuideView.Gravity.center) //optional
                .setDismissType(GuideView.DismissType.anywhere) //optional - default DismissType.targetView
                .setTargetView(view)
                .setContentTextSize(16)
                .build()
                .show();
    }


    public void dialog_about() {
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        final View v = inflater.inflate(R.layout.dialog_about, null);
        TextView pText = (TextView) v.findViewById(R.id.ptext);
        TextView pTitle = (TextView) v.findViewById(R.id.ptitle);
        ImageView pIcon = (ImageView) v.findViewById(R.id.picon);

        Typeface NexaBoldTF = Typeface.createFromAsset(getAssets(), "NexaBold.otf");
        Typeface NexaLiteTF = Typeface.createFromAsset(getAssets(), "NexaLight.otf");

        LinearLayout bottomSheet = (LinearLayout) v.findViewById(R.id.bottomSheet);
        LinearLayout pDialogLinear = (LinearLayout) v.findViewById(R.id.pDialogLinear);
        ColorPrefUtil.changeBackgroundDrawableOfChildViews(Home.this, bottomSheet, colorBox);
        ColorPrefUtil.changeTextColorOfChildViews(Home.this, pDialogLinear, colorTextColor, colorTextColor);

        pTitle.setText("LET REVIEWER");
        pTitle.setTypeface(NexaBoldTF);
        pIcon.setImageResource(R.drawable.icon);
        String content = "Developer: Coffeeworks Ltd.\npinkletblues@gmail.com\n\n LET REVIEWER is a supplementary app used for reviewing of users for the Licensure Examination for Teachers (LET). \n\nThe questions used for this application were selected from various sources. If ever some questions used were subject for copyright issues, just contact our developer for removal. You can also submit files that you want to be added to the App.\n\nLET REVIEWER is FREE to use, however to support our devs and data usage, we are implementing advertisements throughout the app.\n\nThank you for downloading LET REVIEWER, we hope our simple app would greatly help you on your endeavors.";
        pText.setText(content);
        pText.setTypeface(NexaLiteTF);

        BottomSheetDialog dialog =new BottomSheetDialog(Home.this,R.style.SheetDialog);
        dialog.setContentView(v);
        dialog.show();
    }

    private void downloadData()
    {
        loadingData();

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            private String version_control, gened_questions, profed_questions;

            @Override
            public void onResponse(String response) {
                ArrayList<GenEd_Question> gened_arraylist = new ArrayList<>();
                ArrayList<ProfEd_Question> profed_arraylist = new ArrayList<>();

                try
                {
                    JSONObject ob = new JSONObject(response);
                    version_control = ob.getString("version_control");
                    if(!version_control.equals(versionControl)){

                        gened_questions = ob.getString("general_education");
                        profed_questions = ob.getString("professional_education");

                        JSONArray gened_array = new JSONArray(gened_questions);
                        JSONArray profed_array = new JSONArray(profed_questions);
                        for(int i=0;i<gened_array.length();i++)
                        {
                            JSONObject gened_ob = gened_array.getJSONObject(i);
                            String question = gened_ob.getString("question");
                            String optA = gened_ob.getString("optionA");
                            String optB = gened_ob.getString("optionB");
                            String optC = gened_ob.getString("optionC");
                            String optD = gened_ob.getString("optionD");
                            String answer = gened_ob.getString("answer");
                            gened_arraylist.add(new GenEd_Question(question, optA, optB, optC, optD, answer));
                        }

                        for(int j=0;j<profed_array.length();j++)
                        {
                            JSONObject profed_ob = profed_array.getJSONObject(j);
                            String question = profed_ob.getString("question");
                            String optA = profed_ob.getString("optionA");
                            String optB = profed_ob.getString("optionB");
                            String optC = profed_ob.getString("optionC");
                            String optD = profed_ob.getString("optionD");
                            String answer = profed_ob.getString("answer");
                            profed_arraylist.add(new ProfEd_Question(question, optA, optB, optC, optD, answer));
                        }

                        GenEd_Helper = new GenEd_Helper(Home.this);
                        GenEd_Helper.getWritableDatabase();
                        ProfEd_Helper = new ProfEd_Helper(Home.this);
                        ProfEd_Helper.getWritableDatabase();

                        if (GenEd_Helper.getAllOfTheQuestions().size() != 0) {
                            GenEd_Helper.clearDatabase();
                        }
                        if (GenEd_Helper.getAllOfTheQuestions().size() == 0) {
                            GenEd_Helper.addAllQuestions(gened_arraylist);
                        }
                        if (ProfEd_Helper.getAllOfTheQuestions().size() != 0) {
                            ProfEd_Helper.clearDatabase();
                        }
                        if (ProfEd_Helper.getAllOfTheQuestions().size() == 0) {
                            ProfEd_Helper.addAllQuestions(profed_arraylist);
                        }
                        editor.putString(VERSION_CONTROL, version_control);
                        editor.commit();
                        Loading.dismiss();
                        Update(1);
                    }else{
                        Loading.dismiss();
                        Update(2);
                    }
                }  catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestQueue.getCache().clear();
                        Loading.dismiss();
                        Update(0);
                    }
                });
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }
    private void loadingData(){
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        final View v = inflater.inflate(R.layout.dialog_file_loading, null);

        TextView pLoading = (TextView) v.findViewById(R.id.loading);
        Typeface NexaBoldTF = Typeface.createFromAsset(getAssets(), "NexaBold.otf");
        pLoading.setTypeface(NexaBoldTF);

        ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.spin_kit);
        Sprite multiplePulseRing = new MultiplePulseRing();
        progressBar.setIndeterminateDrawable(multiplePulseRing);
        AlertDialog.Builder ab = new AlertDialog.Builder(Home.this);
        ab.setCancelable(false);
        ab.setView(v);
        Loading = ab.create();
        Loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Loading.show();
    }

    private void Update(int update){
        LayoutInflater inflater = LayoutInflater.from(Home.this);
        final View v = inflater.inflate(R.layout.dialog_update, null);

        ImageView uImg = (ImageView) v.findViewById(R.id.update);

        TextView uTxt = (TextView) v.findViewById(R.id.updateText);
        Typeface NexaBoldTF = Typeface.createFromAsset(getAssets(), "NexaBold.otf");
        uTxt.setTypeface(NexaBoldTF);
        if(update == 0){
            uTxt.setText("Error: Check Internet Connection");
            uImg.setImageResource(R.drawable.update_false);
        }else if(update == 1){
            uTxt.setText("Success: File Updated");
            uImg.setImageResource(R.drawable.update_true);
        }else if(update == 2){
            uTxt.setText("File Is Up-to-date");
            uImg.setImageResource(R.drawable.update_true);
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(Home.this);
        ab.setCancelable(true);
        ab.setView(v);
        Update = ab.create();
        Update.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Update.show();
    }
}