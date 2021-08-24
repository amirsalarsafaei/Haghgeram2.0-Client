package Applications.TimeLine.Listeners;

import Applications.TimeLine.Controllers.CommentController;
import Models.Events.SendCommentEvent;
import Utils.AutoUpdatingListener;

public class CommentListener implements AutoUpdatingListener {
    private CommentController commentController;
    public CommentListener(CommentController commentController) {
        this.commentController = commentController;
    }
    @Override
    public void startRefreshing() {
        commentController.start();
    }

    @Override
    public void stopRefreshing() {
        commentController.setDoneFalse();
    }

    @Override
    public void refresh() {
        commentController.getData(false);
    }

    public void sendComment(SendCommentEvent e) {
        commentController.sendComment(e);
    }
}
