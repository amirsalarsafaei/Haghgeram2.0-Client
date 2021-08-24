package Models.Responses;

import java.util.ArrayList;

public class ListsListResponse {
    private ArrayList<String> lists;

    public ArrayList<String> getLists() {
        return lists;
    }

    public void setLists(ArrayList<String> lists) {
        this.lists = lists;
    }

    public ListsListResponse(ArrayList<String> lists) {
        this.lists = lists;
    }
}
