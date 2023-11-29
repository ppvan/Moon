package me.ppvan.moon.data.dto;

public class LogoutDto {

    private String accessToken;

    public LogoutDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
