package Models.Events;

public class DeleteMessageEvent {
    private int id;

    public DeleteMessageEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
