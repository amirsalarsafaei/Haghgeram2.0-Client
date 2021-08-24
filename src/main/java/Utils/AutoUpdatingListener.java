package Utils;

public interface AutoUpdatingListener {
    void startRefreshing();
    void stopRefreshing();
    void refresh();
}
