package me.ppvan.moon.data.dto;

import com.google.gson.annotations.SerializedName;

public class TokenResponseDto {

    @SerializedName("accsess_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;

    public TokenResponseDto(String accessToken,
                            String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
