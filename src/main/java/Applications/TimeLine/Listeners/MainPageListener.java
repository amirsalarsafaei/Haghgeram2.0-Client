package Applications.TimeLine.Listeners;

import Applications.TimeLine.Controllers.MainPageController;
import Applications.TimeLine.Controllers.TimeLineController;
import Models.Events.SendTweetEvent;
import Utils.AutoUpdatingListener;

public class MainPageListener implements AutoUpdatingListener {
    private MainPageController mainPageController;
    private TimeLineController timeLineController;
    public MainPageListener(TimeLineController timeLineController) {
        this.timeLineController = timeLineController;
        mainPageController = new MainPageController();
    }
    public void sendTweetListener(SendTweetEvent e) {
        mainPageController.sendTweet(e);
    }

    @Override
    public void startRefreshing() {
        timeLineController.setDoneFalse();
        timeLineController.getData(true);
    }

    @Override
    public void stopRefreshing() {
        timeLineController.setDoneTrue();
    }

    @Override
    public void refresh() {
        timeLineController.getData(false);
        System.out.println("shit3");
    }
}
