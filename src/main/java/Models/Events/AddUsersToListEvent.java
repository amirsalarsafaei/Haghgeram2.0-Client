package Models.Events;

import Models.Responses.ListResponse;

public class AddUsersToListEvent {
    private ListResponse listResponse;

    public ListResponse getListResponse() {
        return listResponse;
    }

    public void setListResponse(ListResponse listResponse) {
        this.listResponse = listResponse;
    }

    public AddUsersToListEvent(ListResponse listResponse) {
        this.listResponse = listResponse;
    }
}
