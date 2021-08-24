package Models.Responses;

import Models.Events.ChangePrivacyEvent;
import Models.LastSeen;

public class EditPrivacyResponse {
    private LastSeen lastSeen;
    private boolean DeActive, Private;

    public EditPrivacyResponse(ChangePrivacyEvent changePrivacyEvent) {
        lastSeen = changePrivacyEvent.getLastSeen();
        DeActive = changePrivacyEvent.isDeActive();
        Private = changePrivacyEvent.isPrivate();
    }

    public LastSeen getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LastSeen lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isDeActive() {
        return DeActive;
    }

    public void setDeActive(boolean deActive) {
        DeActive = deActive;
    }

    public boolean isPrivate() {
        return Private;
    }

    public void setPrivate(boolean Private) {
        this.Private = Private;
    }
}
