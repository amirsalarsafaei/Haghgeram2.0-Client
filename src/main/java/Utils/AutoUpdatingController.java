package Utils;

public interface AutoUpdatingController {
    void getData(boolean repeat);
    void setDoneTrue();
    void setDoneFalse();
    default void start() {
        setDoneFalse();
        getData(true);
    }
    default void stop() {
        setDoneTrue();
    }
}
