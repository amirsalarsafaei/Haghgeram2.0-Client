package Models.Responses;

import java.util.ArrayList;

public class NotificationsResponse {
    private ArrayList<String> events;
    private ArrayList<String> requests;



    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<String> requests) {
        this.requests = requests;
    }
}
