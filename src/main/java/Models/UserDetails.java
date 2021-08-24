package Models;

import Models.Responses.EditPrivacyResponse;
import Models.Responses.UserInformationResponse;

public class UserDetails {
    public static UserDetails holder;
    private String username, token;
    private int id;
    private EditPrivacyResponse editPrivacyResponse;
    private UserInformationResponse userInformationResponse;

    public EditPrivacyResponse getEditPrivacyResponse() {
        return editPrivacyResponse;
    }

    public void setEditPrivacyResponse(EditPrivacyResponse editPrivacyResponse) {
        this.editPrivacyResponse = editPrivacyResponse;
    }

    public UserInformationResponse getUserInformationResponse() {
        return userInformationResponse;
    }

    public void setUserInformationResponse(UserInformationResponse userInformationResponse) {
        this.userInformationResponse = userInformationResponse;
    }

    public static UserDetails getHolder() {
        return holder;
    }

    public static void setHolder(UserDetails holder) {
        UserDetails.holder = holder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserDetails(String username, int id, String token) {
        this.username = username;
        this.id = id;
        this.token = token;
    }
}