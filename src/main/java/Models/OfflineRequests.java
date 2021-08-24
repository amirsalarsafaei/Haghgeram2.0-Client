package Models;

import DataBase.OfflineRequest;
import Models.Networking.Request;

import java.util.ArrayList;

public class OfflineRequests {
    public static OfflineRequests offlineRequests;
    private ArrayList<Request> requests;

    public OfflineRequests() {
        requests = new ArrayList<>();
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public void addRequest(Request request) {
        requests.add(request);
        OfflineRequest.Save(this);
    }

}
