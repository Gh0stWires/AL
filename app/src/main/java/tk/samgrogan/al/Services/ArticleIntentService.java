package tk.samgrogan.al.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tk.samgrogan.al.Data.ArticlesModel;
import tk.samgrogan.al.Data.NewsResponse;
import tk.samgrogan.al.Data.Remote.ArticleService;
import tk.samgrogan.al.MainActivity;
import tk.samgrogan.al.Utils.ApiUtil;

/**
 * Created by ghost on 8/10/2017.
 */

public class ArticleIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    ArticleService articleService;
    List<ArticlesModel> articleData;
    public static final String LIST_MSG = "articles";

    public ArticleIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        articleService = ApiUtil.getArticleService();
        articleService.getArticles().enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                Log.d("Are you seeing this?", "Then its working");
                articleData = response.body().getArticles();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(MainActivity.ArticleReceiver.ACTION_RESP);
                broadcastIntent.putExtra(LIST_MSG, (Parcelable) articleData);
                sendBroadcast(broadcastIntent);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {

            }
        });
    }
}
