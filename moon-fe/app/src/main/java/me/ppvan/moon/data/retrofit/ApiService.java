package me.ppvan.moon.data.retrofit;

import me.ppvan.moon.data.dto.AuthenticationDto;
import me.ppvan.moon.data.dto.LogoutDto;
import me.ppvan.moon.data.dto.RegisterDto;
import me.ppvan.moon.data.dto.TokenResponseDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/api/v1/auth/register")
    Call<TokenResponseDto> register(@Body RegisterDto registerDto);

    @POST("/api/v1/auth/authenticate")
    Call<TokenResponseDto> authenticate(@Body AuthenticationDto authenticationDto);

    @POST("/api/v1/auth/logout")
    Call<String> logout(@Body LogoutDto logoutDto);
}
