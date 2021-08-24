package Models.Responses;

import java.util.ArrayList;

public class FollowRequestsStatusResponse {

    private ArrayList<String> accepted, rejected, pending;

    public ArrayList<String> getAccepted() {
        return accepted;
    }

    public void setAccepted(ArrayList<String> accepted) {
        this.accepted = accepted;
    }

    public ArrayList<String> getRejected() {
        return rejected;
    }

    public void setRejected(ArrayList<String> rejected) {
        this.rejected = rejected;
    }

    public ArrayList<String> getPending() {
        return pending;
    }

    public void setPending(ArrayList<String> pending) {
        this.pending = pending;
    }
}