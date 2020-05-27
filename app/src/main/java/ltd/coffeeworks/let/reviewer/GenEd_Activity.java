package ltd.coffeeworks.let.reviewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import akndmr.github.io.colorprefutil.*;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Collections;
import java.util.List;


public class GenEd_Activity extends AppCompatActivity implements View.OnClickListener
{

	private TextView Title;
	private TextView Subtitle;

	public static final String PREF_COLOR = "pref_color";
	public static final String THEME_SELECTED = "theme_selected";
	public static final String BACKGROUND = "background";
	public static final String HEADER = "header";
	public static final String BOX = "box";
	public static final String CORRECT = "correct";
	public static final String INCORRECT = "incorrect";
	public static final String TEXTCOLOR = "textcolor";
	public static final String ICONCOLOR = "iconcolor";
	public static final String FEATURES_Q = "features_q";
	int colorBackground, colorHeader, colorTextColor, colorButton, colorBox, colorBoxCorrect, colorBoxIncorrect;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

	private RelativeLayout background, appHeader;

	private LinearLayout question_linearlayout, OptionA, OptionB, OptionC, OptionD;
	private TextView question, question_gened, labelA, labelB, labelC, labelD, OptionAText, OptionBText, OptionCText, OptionDText;

	private GenEd_Helper GenEd_Helper;
	private GenEd_Question currentQuestion;
	private List<GenEd_Question> list;
	private int qid = 0;

	private LinearLayout OptionA_response, OptionB_response, OptionC_response, OptionD_response;

	private ImageView OptionACorrect, OptionAWrong, OptionBCorrect, OptionBWrong, OptionCCorrect, OptionCWrong, OptionDCorrect, OptionDWrong;

	private boolean iconsColor;

	private AlertDialog Question;
	

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mSharedPreferences = getSharedPreferences(PREF_COLOR, MODE_PRIVATE);
        int themeSelected = mSharedPreferences.getInt(THEME_SELECTED, R.style.AppTheme_Light);
        ColorPrefUtil.changeThemeStyle(this, themeSelected);

		setContentView(R.layout.reviewer_gened);
		customActionBar();
		initLayout();


		editor = mSharedPreferences.edit();
        colorBackground = mSharedPreferences.getInt(BACKGROUND, R.color.background_light);
		colorTextColor = mSharedPreferences.getInt(TEXTCOLOR, R.color.text_light);
		colorBox = mSharedPreferences.getInt(BOX,  R.drawable.box_light);
		colorBoxCorrect = mSharedPreferences.getInt(CORRECT, R.drawable.box_correct_light);
		colorBoxIncorrect = mSharedPreferences.getInt(INCORRECT, R.drawable.box_incorrect_light);

		

		ColorPrefUtil.changeBackgroundColorOfSingleView(this, background, colorBackground);
		ColorPrefUtil.changeTextColorOfChildViews(this, (LinearLayout)findViewById(R.id.title_holder), colorTextColor, colorTextColor);
		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, question_linearlayout, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(this, question_gened, colorTextColor, colorTextColor);

		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionA, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(this, labelA, colorTextColor, colorTextColor);
		ColorPrefUtil.changeTextColorOfSingleView(this, OptionAText, colorTextColor, colorTextColor);

		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionB, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(this, labelB, colorTextColor, colorTextColor);
		ColorPrefUtil.changeTextColorOfSingleView(this, OptionBText, colorTextColor, colorTextColor);

		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionC, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(this, labelC, colorTextColor, colorTextColor);
		ColorPrefUtil.changeTextColorOfSingleView(this, OptionCText, colorTextColor, colorTextColor);

		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionD, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(this, labelD, colorTextColor, colorTextColor);
		ColorPrefUtil.changeTextColorOfSingleView(this, OptionDText, colorTextColor, colorTextColor);

		Review();

		
	}

	private void Review()
	{
        GenEd_Helper = new GenEd_Helper(this);
        GenEd_Helper.getWritableDatabase();
		list = GenEd_Helper.getAllOfTheQuestions();
        Collections.shuffle(list);
        currentQuestion = list.get(qid);
		setQuestionsandOptions();

	}

	private void setQuestionsandOptions()
	{
		question_gened.setText(currentQuestion.getQuestion());
        OptionAText.setText(currentQuestion.getOption_a());
        OptionBText.setText(currentQuestion.getOption_b());
        OptionCText.setText(currentQuestion.getOption_c());
        OptionDText.setText(currentQuestion.getOption_d());


	}

	private void customActionBar()
	{
		Typeface NexaBold = Typeface.createFromAsset(this.getAssets(), "NexaBold.otf");
		Typeface NexaLite = Typeface.createFromAsset(this.getAssets(), "NexaLight.otf");

		Title = (TextView) findViewById(R.id.title);
		Subtitle = (TextView) findViewById(R.id.subtitle);
		Title.setText(getString(R.string.app_name).toUpperCase());
		Subtitle.setText("GENERAL EDUCATION");
		Title.setTypeface(NexaBold);
		Subtitle.setTypeface(NexaLite);
	}
	private void initLayout()
	{
		Typeface NexaBold = Typeface.createFromAsset(this.getAssets(), "NexaBold.otf");  
		Typeface NexaLite = Typeface.createFromAsset(this.getAssets(), "NexaLight.otf");
		background = (RelativeLayout) findViewById(R.id.background);
		appHeader = (RelativeLayout) findViewById(R.id.app_header);

		question = (TextView) findViewById(R.id.question_gened);
		question.setMovementMethod(new ScrollingMovementMethod());
		question.setTypeface(NexaLite);
		

		question_linearlayout = (LinearLayout) findViewById(R.id.question_linearlayout);
		question_linearlayout.setOnClickListener(this);
		question_gened = (TextView) findViewById(R.id.question_gened);
		
		
		OptionA = (LinearLayout) findViewById(R.id.OptionA);
		OptionA.setOnClickListener(this);
		OptionA_response = (LinearLayout) findViewById(R.id.OptionA_response);
		OptionACorrect = (ImageView) findViewById(R.id.OptionACorrect);
		OptionAWrong = (ImageView) findViewById(R.id.OptionAWrong);
		labelA = (TextView) findViewById(R.id.labelA);
		OptionAText = (TextView) findViewById(R.id.OptionAText);

		OptionB = (LinearLayout) findViewById(R.id.OptionB);
		OptionB.setOnClickListener(this);
		OptionB_response = (LinearLayout) findViewById(R.id.OptionB_response);
		OptionBCorrect = (ImageView) findViewById(R.id.OptionBCorrect);
		OptionBWrong = (ImageView) findViewById(R.id.OptionBWrong);
		labelB = (TextView) findViewById(R.id.labelB);
		OptionBText = (TextView) findViewById(R.id.OptionBText);

		OptionC = (LinearLayout) findViewById(R.id.OptionC);
		OptionC.setOnClickListener(this);
		OptionC_response = (LinearLayout) findViewById(R.id.OptionC_response);
		OptionCCorrect = (ImageView) findViewById(R.id.OptionCCorrect);
		OptionCWrong = (ImageView) findViewById(R.id.OptionCWrong);
		labelC = (TextView) findViewById(R.id.labelC);
		OptionCText = (TextView) findViewById(R.id.OptionCText);

		OptionD = (LinearLayout) findViewById(R.id.OptionD);
		OptionD.setOnClickListener(this);
		OptionD_response = (LinearLayout) findViewById(R.id.OptionD_response);
		OptionDCorrect = (ImageView) findViewById(R.id.OptionDCorrect);
		OptionDWrong = (ImageView) findViewById(R.id.OptionDWrong);
		labelD = (TextView) findViewById(R.id.labelD);
		OptionDText = (TextView) findViewById(R.id.OptionDText);

		labelA.setTypeface(NexaBold);
		OptionAText.setTypeface(NexaLite);

		labelB.setTypeface(NexaBold);
		OptionBText.setTypeface(NexaLite);

		labelC.setTypeface(NexaBold);
		OptionCText.setTypeface(NexaLite);

		labelD.setTypeface(NexaBold);
		OptionDText.setTypeface(NexaLite);

	}
	@Override
	public void onClick(View p1)
	{
		switch (p1.getId()){
			case R.id.question_linearlayout:
				dialog_question(currentQuestion.getQuestion());
				break;
			case R.id.OptionA:
				AllClickables(false);
				labelA.setVisibility(TextView.GONE);
				OptionA_response.setVisibility(LinearLayout.VISIBLE);
				if(currentQuestion.getAnswer().equals("A")){
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionA, colorBoxCorrect);
					OptionACorrect.setVisibility(ImageView.VISIBLE);
				}else{
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionA, colorBoxIncorrect);
					OptionAWrong.setVisibility(ImageView.VISIBLE);
					giveCorrect();
				}
				if ((list.size() - (qid + 1)) == 0) {
					dialog_last();
				} else {
					dialog_next();
				}
				break;
			case R.id.OptionB:
				AllClickables(false);
				labelB.setVisibility(TextView.GONE);
				OptionB_response.setVisibility(LinearLayout.VISIBLE);
				if(currentQuestion.getAnswer().equals("B")){
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionB, colorBoxCorrect);
					OptionBCorrect.setVisibility(ImageView.VISIBLE);
				}else{
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionB, colorBoxIncorrect);
					OptionBWrong.setVisibility(ImageView.VISIBLE);
					giveCorrect();
				}
				if ((list.size() - (qid + 1)) == 0) {
					dialog_last();
				} else {
					dialog_next();
				}
				break;
			case R.id.OptionC:
				AllClickables(false);
				labelC.setVisibility(TextView.GONE);
				OptionC_response.setVisibility(LinearLayout.VISIBLE);
				if(currentQuestion.getAnswer().equals("C")){
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionC, colorBoxCorrect);
					OptionCCorrect.setVisibility(ImageView.VISIBLE);
				}else{
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionC, colorBoxIncorrect);
					OptionCWrong.setVisibility(ImageView.VISIBLE);
					giveCorrect();
				}
				if ((list.size() - (qid + 1)) == 0) {
					dialog_last();
				} else {
					dialog_next();
				}
				break;
			case R.id.OptionD:
				AllClickables(false);
				labelD.setVisibility(TextView.GONE);
				OptionD_response.setVisibility(LinearLayout.VISIBLE);
				if(currentQuestion.getAnswer().equals("D")){
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionD, colorBoxCorrect);
					OptionDCorrect.setVisibility(ImageView.VISIBLE);
				}else{
					ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionD, colorBoxIncorrect);
					OptionDWrong.setVisibility(ImageView.VISIBLE);
					giveCorrect();
				}
				if ((list.size() - (qid + 1)) == 0) {
					dialog_last();
				} else {
					dialog_next();
				}
				break;
        }
	}

	private void giveCorrect()
	{
		if(currentQuestion.getAnswer().equals("A")){
			ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionA, colorBoxCorrect);
			labelA.setVisibility(TextView.GONE);
			OptionA_response.setVisibility(LinearLayout.VISIBLE);
			OptionACorrect.setVisibility(ImageView.VISIBLE);
		}else if(currentQuestion.getAnswer().equals("B")){
			ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionB, colorBoxCorrect);
			labelB.setVisibility(TextView.GONE);
			OptionB_response.setVisibility(LinearLayout.VISIBLE);
			OptionBCorrect.setVisibility(ImageView.VISIBLE);
		}else if(currentQuestion.getAnswer().equals("C")){
			ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionC, colorBoxCorrect);
			labelC.setVisibility(TextView.GONE);
			OptionC_response.setVisibility(LinearLayout.VISIBLE);
			OptionCCorrect.setVisibility(ImageView.VISIBLE);
		}else if(currentQuestion.getAnswer().equals("D")){
			ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionD, colorBoxCorrect);
			labelD.setVisibility(TextView.GONE);
			OptionD_response.setVisibility(LinearLayout.VISIBLE);
			OptionDCorrect.setVisibility(ImageView.VISIBLE);
		}
	}

	

	private void dialog_last()
	{
		LayoutInflater inflater = LayoutInflater.from(GenEd_Activity.this);
		final View v = inflater.inflate(R.layout.dialog_last, null); 
		TextView pText = (TextView) v.findViewById(R.id.ptext);
		TextView pTitle = (TextView) v.findViewById(R.id.ptitle);
		ImageView pIcon = (ImageView) v.findViewById(R.id.picon);
		TextView pEnd = (TextView) v.findViewById(R.id.pend);

		LinearLayout holder = (LinearLayout) v.findViewById(R.id.holder);
		LinearLayout pDialogLinear = (LinearLayout) v.findViewById(R.id.pDialogLinear);
		ColorPrefUtil.changeBackgroundDrawableOfChildViews(GenEd_Activity.this, holder, colorBox);
		ColorPrefUtil.changeTextColorOfChildViews(GenEd_Activity.this, pDialogLinear, colorTextColor, colorTextColor);
		ColorPrefUtil.changeBackgroundDrawableOfSingleView(GenEd_Activity.this, pEnd, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(GenEd_Activity.this, pEnd, colorTextColor, colorTextColor);
		Typeface NexaBoldTF = Typeface.createFromAsset(getAssets(), "NexaBold.otf");
		Typeface NexaLiteTF = Typeface.createFromAsset(getAssets(), "NexaLight.otf");

		pTitle.setText("LET REVIEWER");
		pTitle.setTypeface(NexaBoldTF);
		pIcon.setImageResource(R.drawable.icon);
		String content = "You have answered all available questions.\n\nNew questions will be added soon, we'll just notify you once everything is ready.";
		pText.setText(content);
		pText.setTypeface(NexaLiteTF);
		pEnd.setText("HOME");
		pEnd.setTypeface(NexaBoldTF);
		pEnd.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					startActivity(new Intent(GenEd_Activity.this, Home.class));
					finish();
				}
			});
		final BottomSheetDialog dialog =new BottomSheetDialog(GenEd_Activity.this,R.style.SheetDialog);
		dialog.setContentView(v);
		dialog.setCancelable(false);


		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dialog.show();
				}
			}, 3000);
	}

	private void nextQuestion()
	{
		qid++;
		if((list.size() - qid) >0){
			currentQuestion = list.get(qid);
			setQuestionsandOptions();
			resetOptionColors();
			AllClickables(true);
		}else{
			startActivity(new Intent(GenEd_Activity.this, Home.class));
			finish();
		}
	}

	private void resetOptionColors()
	{
		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionA, colorBox);
		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionB, colorBox);
		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionC, colorBox);
		ColorPrefUtil.changeBackgroundDrawableOfChildViews(this, OptionD, colorBox);

		labelA.setVisibility(TextView.VISIBLE);
		labelB.setVisibility(TextView.VISIBLE);
		labelC.setVisibility(TextView.VISIBLE);
		labelD.setVisibility(TextView.VISIBLE);


		OptionA_response.setVisibility(LinearLayout.GONE);
		OptionACorrect.setVisibility(ImageView.GONE);
		OptionAWrong.setVisibility(ImageView.GONE);

		OptionB_response.setVisibility(LinearLayout.GONE);
		OptionBCorrect.setVisibility(ImageView.GONE);
		OptionBWrong.setVisibility(ImageView.GONE);

		OptionC_response.setVisibility(LinearLayout.GONE);
		OptionCCorrect.setVisibility(ImageView.GONE);
		OptionCWrong.setVisibility(ImageView.GONE);

		OptionD_response.setVisibility(LinearLayout.GONE);
		OptionDCorrect.setVisibility(ImageView.GONE);
		OptionDWrong.setVisibility(ImageView.GONE);

	}

	private void AllClickables(Boolean clickable)
	{
		question_linearlayout.setEnabled(clickable);
		OptionA.setEnabled(clickable);
		OptionB.setEnabled(clickable);
		OptionC.setEnabled(clickable);
		OptionD.setEnabled(clickable);
	}

	@Override
    public void onBackPressed() {
        startActivity(new Intent(GenEd_Activity.this, Home.class));
        finish();
    }
	public void dialog_next() {
		LayoutInflater inflater = LayoutInflater.from(GenEd_Activity.this);  
		final View v = inflater.inflate(R.layout.dialog_next, null); 
		ImageView nHome = (ImageView) v.findViewById(R.id.nHome);
		TextView nNext = (TextView) v.findViewById(R.id.nNext);

		ColorPrefUtil.changeBackgroundDrawableOfSingleView(GenEd_Activity.this, nHome, colorBox);
		ColorPrefUtil.changeBackgroundDrawableOfSingleView(GenEd_Activity.this, nNext, colorBox);

		ColorPrefUtil.changeTextColorOfSingleView(GenEd_Activity.this, nNext, colorTextColor, colorTextColor);

		Typeface NexaBoldTF = Typeface.createFromAsset(getAssets(), "NexaBold.otf");

		iconsColor = mSharedPreferences.getBoolean(ICONCOLOR, true);
		if(iconsColor){
			nHome.setImageResource(R.drawable.home_black);
		}else{
			nHome.setImageResource(R.drawable.home_white);
		}

		nNext.setText("NEXT QUESTION");
		nNext.setTypeface(NexaBoldTF);
		final BottomSheetDialog dialog =new BottomSheetDialog(GenEd_Activity.this,R.style.SheetDialog);
		dialog.setContentView(v);
		dialog.setCancelable(false);
		nHome.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					startActivity(new Intent(GenEd_Activity.this, Home.class));
					finish();
				}
			});
		nNext.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					nextQuestion();
					dialog.dismiss();
				}
			});
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dialog.show();
				}
			}, 3000);

	}
	public void dialog_question(String questionstr){
		LayoutInflater inflater = LayoutInflater.from(GenEd_Activity.this);  
		final View v = inflater.inflate(R.layout.dialog_question, null); 

		LinearLayout question_bg = (LinearLayout) v.findViewById(R.id.question_bg);
		TextView question = (TextView) v.findViewById(R.id.question);
		Typeface NexaLiteTF = Typeface.createFromAsset(getAssets(), "NexaLight.otf");
		question.setTypeface(NexaLiteTF);
		question.setText(questionstr);

		ColorPrefUtil.changeBackgroundDrawableOfSingleView(GenEd_Activity.this, question_bg, colorBox);
		ColorPrefUtil.changeTextColorOfSingleView(GenEd_Activity.this, question, colorTextColor, colorTextColor);


		AlertDialog.Builder ab = new AlertDialog.Builder(GenEd_Activity.this);
		ab.setCancelable(true);	
		ab.setView(v);
		Question = ab.create();
		Question.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		Question.show();
	}
}
