package sky.example.http;

import java.util.List;

import retrofit2.SKCall;
import retrofit2.http.GET;
import sky.example.http.model.Model;

/**
 * @author sky
 * @version 1.0 on 2017-11-03 上午9:48
 * @see GithubHttp github 公开接口
 */
public interface GithubHttp {

	@GET("events") SKCall<List<Model>> rateLimit();

}
