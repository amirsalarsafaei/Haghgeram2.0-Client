package Applications.Explorer.Listeners;

import Applications.Explorer.Controllers.ExplorerController;
import Utils.AutoUpdatingListener;

public class ExplorerListener implements AutoUpdatingListener {
    private ExplorerController explorerController;
    public ExplorerListener(ExplorerController explorerController) {
        this.explorerController = explorerController;
    }

    @Override
    public void startRefreshing() {
        explorerController.start();
    }

    @Override
    public void stopRefreshing() {
        explorerController.stop();
    }

    @Override
    public void refresh() {
        explorerController.getData(false);
    }
}
