package org.bitcoin.tfw.ltbc.client;

import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IController {

    @PUT("/ltbc/controller/mine")
    Call<Void> mine(@Query("blocks") int blocks);

    @PUT("/ltbc/controller/mineTo")
    Call<Void> mineTo(@Query("blocks") int blocks, @Query("address") String address);

    @PUT("/ltbc/controller/sendTo")
    Call<Void> sendTo(@Query("amount") double amount, @Query("address") String address);
}
