package Models.Responses;


public class SmallUserResponse {
    private String username;
    private int id;
    private byte[] image;

    public SmallUserResponse(BigUserResponse bigUserResponse) {
        username = bigUserResponse.getUsername();
        id = bigUserResponse.getId();
        image = bigUserResponse.getImage();
    }

    public SmallUserResponse(String username, int id, byte[] image) {
        this.image = image;
        this.id = id;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


}
