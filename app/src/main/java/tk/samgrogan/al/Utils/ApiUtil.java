package tk.samgrogan.al.Utils;

import tk.samgrogan.al.Data.Remote.ArticleService;
import tk.samgrogan.al.Data.Remote.RetrofitClient;

/**
 * Created by ghost on 8/1/2017.
 */

public class ApiUtil {
    public static final String BASE_URL = " https://newsapi.org/v1";

    public static ArticleService getArticleService(){
        return RetrofitClient.getClient(BASE_URL).create(ArticleService.class);
    }
}
