package org.bitcoin.tfw.ltbc.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class RetrofitCallHelper {
    public static class SafeCallException extends RuntimeException {
        final ResponseBody responseBody;
        final int code;

        public SafeCallException(ResponseBody responseBody, int code) {
            this.responseBody = responseBody;
            this.code = code;
        }
    }
    public static <T> T safeCall(Call<T> call) {
        try {
            Response<T> response = call.execute();

            if (response.isSuccessful())
                return response.body();

            throw new SafeCallException(response.errorBody(), response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
