package com.shileiyu.okgo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.get, R.id.post})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get:
                get();
                break;
            case R.id.post:
                break;
            default:
                break;
        }
    }

    private void get() {
        NetWork.get("https://www.sojson.com/open/api/weather/json.shtml?city=%E5%8C%97%E4%BA%AC",
                new BizCallBack<HttpResponse<Data>>() {

                    @Override
                    public void onSuccess(HttpResponse<Data> data) {
                        String text = "请求成功=" + JsonUtil.toJson(data);
                        mTv.setText(text);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        mTv.setText("失败");
                    }
                });
    }

    /**
     * 网络层
     */
    public static class NetWork {
        /**
         * Get请求
         */
        public static <T> void get(String url, final BizCallBack<T> bizCallBack) {
            final GetRequest<T> request = OkGo.get(url);
            request.execute(new BaseCallBack<T>(bizCallBack));

        }

        /**
         * Post 请求
         */
        public static <T> void post(String url, BizCallBack<T> bizCallBack) {
            PostRequest<T> post = OkGo.post(url);
            post.upJson(JsonUtil.toJson(new Data())).execute(new BaseCallBack<T>(bizCallBack));
        }
    }

    /**
     * 业务层回调
     *
     * @param <T>
     */
    public abstract static class BizCallBack<T> extends TypeToken<T> {

        public abstract void onSuccess(T data);

        public abstract void onFail(int code, String msg);

    }

    /**
     * 网络层回调
     *
     * @param <T>
     */
    public static class BaseCallBack<T> implements Callback<T> {
        final BizCallBack<T> real;

        public BaseCallBack(BizCallBack<T> back) {
            this.real = back;
        }

        @Override
        public void onStart(Request<T, ? extends Request> request) {

        }

        @Override
        public void onSuccess(Response<T> response) {
            real.onSuccess(response.body());
        }

        @Override
        public void onCacheSuccess(Response<T> response) {

        }

        @Override
        public void onError(Response<T> response) {
            real.onFail(response.code(), response.message());
        }

        @Override
        public void onFinish() {

        }

        @Override
        public void uploadProgress(Progress progress) {

        }

        @Override
        public void downloadProgress(Progress progress) {

        }

        @Override
        public T convertResponse(okhttp3.Response response) throws Throwable {
            if (response.isSuccessful()) {
                String string = response.body().string();
                Object o = new Gson().fromJson(string, real.getType());
                return (T) o;
            }
            return null;
        }
    }
}
