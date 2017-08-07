package tk.samgrogan.al.Data.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import tk.samgrogan.al.Data.NewsResponse;

/**
 * Created by ghost on 8/1/2017.
 */

public interface ArticleService {

    @GET("articles?apiKey=ef3566796356478d9f2e17c8628077f0&source=google-news&sortBy=top")
    Call<NewsResponse> getArticles();
}
