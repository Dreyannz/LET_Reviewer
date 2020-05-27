package ltd.coffeeworks.let.reviewer;



import android.content.*;
import android.content.res.*;
import android.database.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;

public class GenEd_Helper extends SQLiteOpenHelper
{
	private Context context;
    private static final String DB_NAME = "LET_REVIEWER_G.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "GenEdQuestions";
    private static final String UID = "_UID";
    private static final String QUESTION = "QUESTION";
    private static final String OPTION_A = "OPTION_A";
    private static final String OPTION_B = "OPTION_B";
    private static final String OPTION_C = "OPTION_C";
    private static final String OPTION_D = "OPTION_D";
    private static final String ANSWER = "ANSWER";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + UID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + QUESTION + " VARCHAR(255), " + OPTION_A + " VARCHAR(255), " + OPTION_B + " VARCHAR(255), " + OPTION_C + " VARCHAR(255), " + OPTION_D + " VARCHAR(255), " + ANSWER + " VARCHAR(255));";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;




    GenEd_Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }
	public void clearDatabase() {  
		SQLiteDatabase db = this.getWritableDatabase();  
		db.execSQL(DROP_TABLE);  
		db.execSQL(CREATE_TABLE);
	}  


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
	public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();
        return new String(formArray);
    }
	public void addAllQuestions(ArrayList<GenEd_Question> allQuestions)
	{
		SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (GenEd_Question question : allQuestions) {
                values.put(QUESTION, question.getQuestion());
                values.put(OPTION_A, question.getOption_a());
                values.put(OPTION_B, question.getOption_b());
                values.put(OPTION_C, question.getOption_c());
                values.put(OPTION_D, question.getOption_d());
                values.put(ANSWER, question.getAnswer());
                db.insert(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
	}

    List<GenEd_Question> getAllOfTheQuestions() {

        List<GenEd_Question> questionsList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        String coloumn[] = {UID, QUESTION, OPTION_A, OPTION_B, OPTION_C, OPTION_D, ANSWER};
        Cursor cursor = db.query(TABLE_NAME, coloumn, null, null, null, null, null);


        while (cursor.moveToNext()) {
            GenEd_Question question = new GenEd_Question();
            question.setId(cursor.getInt(0));
            question.setQuestion(cursor.getString(1));
            question.setOption_a(cursor.getString(2));
            question.setOption_b(cursor.getString(3));
            question.setOption_c(cursor.getString(4));
            question.setOption_d(cursor.getString(5));
            question.setAnswer(cursor.getString(6));
            questionsList.add(question);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        cursor.close();
        db.close();
        return questionsList;
    }

}
