package org.bitcoin.tfw.ltbc.client;

import retrofit2.Call;
import retrofit2.http.*;

public interface IDaemonController {
    @GET("/ltbc/daemon/status")
    Call<Boolean> getStatus();

    @PUT("/ltbc/daemon/start")
    Call<Void> start();

    @PUT("/ltbc/daemon/stop")
    Call<Void> stop();
}
