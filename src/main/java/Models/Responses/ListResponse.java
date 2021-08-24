package Models.Responses;

import java.util.ArrayList;

public class ListResponse {
    private String listName;
    private ArrayList<String> users;

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }

}
