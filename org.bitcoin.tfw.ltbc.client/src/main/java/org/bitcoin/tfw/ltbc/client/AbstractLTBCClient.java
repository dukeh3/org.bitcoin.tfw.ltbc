package org.bitcoin.tfw.ltbc.client;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

abstract public class AbstractLTBCClient<T> {
    T avatar;

    public AbstractLTBCClient(String baseUrl, Class<T> cls) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client)
                .build();

        avatar = retrofit.create(cls);
    }
}
