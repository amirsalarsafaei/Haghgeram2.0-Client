package Models.Events;

public class HyperLinkClickedEvent {
    private String hyperlinkText;

    public HyperLinkClickedEvent(String hyperlinkText) {
        this.hyperlinkText = hyperlinkText;
    }

    public String getHyperlinkText() {
        return hyperlinkText;
    }

    public void setHyperlinkText(String hyperlinkText) {
        this.hyperlinkText = hyperlinkText;
    }
}
