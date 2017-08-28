package tk.samgrogan.al;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tk.samgrogan.al.Adapters.ArticleAdapter;
import tk.samgrogan.al.Data.ArticlesModel;
import tk.samgrogan.al.Data.NewsResponse;
import tk.samgrogan.al.Data.Remote.ArticleService;
import tk.samgrogan.al.Services.ArticleIntentService;
import tk.samgrogan.al.Utils.ApiUtil;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 123;
    private TextView display;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private TextToSpeech tts;
    private ArticleAdapter mAdapter;
    private ArticleService service;
    private ArticleReceiver receiver;
    private AlarmManager alarmManager;
    private PendingIntent articleIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        auth = FirebaseAuth.getInstance();

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak("Hello");

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        service = ApiUtil.getArticleService();
        IntentFilter filter = new IntentFilter(ArticleReceiver.ACTION_RESP);
        receiver = new ArticleReceiver();
        registerReceiver(receiver, filter);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent article = new Intent(getApplicationContext(), AlarmManagerReceiver.class);
        articleIntent = PendingIntent.getBroadcast(getApplicationContext(),0, article, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_FIFTEEN_MINUTES, articleIntent);


        //display = (TextView) findViewById(R.id.body_text);
        mAdapter = new ArticleAdapter(new ArrayList<ArticlesModel>(), getApplicationContext());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.article_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(auth.getCurrentUser() != null) {
            reference = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("commands");
        }else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
            reference = database.getReference().child("users").child(auth.getCurrentUser().getUid()).child("commands");
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Call Check", "Called");
                CommandModel command = dataSnapshot.getValue(CommandModel.class);
                if (!command.getmUserCommand().equals("Ready for next command")) {
                    System.out.println(command.getmUserCommand());
                    //display.setText(command.getmUserCommand());
                    TcpClient client = new TcpClient("192.168.0.3", 5000, command.getmUserCommand().toString());
                    client.execute();
                    speak(command.getmUserCommand());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        loadArticles();
    }



    private void displayChatMessages() {

    }

    public void loadArticles(){
        service.getArticles().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()){
                    mAdapter.updateAnswers(response.body().getArticles());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Log.d("IDK", "Shits broke");

            }
        });
    }

    private void speak(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public class ArticleReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "tk.samgrogan.intent.action.MESSAGE_PROCESSED";
        @Override
        public void onReceive(Context context, Intent intent) {

            mAdapter.updateAnswers(intent.<ArticlesModel>getParcelableArrayListExtra(ArticleIntentService.LIST_MSG));

        }
    }

    public class AlarmManagerReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent service = new Intent(context, ArticleIntentService.class);
            context.startService(service);
        }
    }

    public void getPsyched(View view){
        Intent intent = new Intent(getApplicationContext(), GetPsyched.class);
        startActivity(intent);
    }


}
