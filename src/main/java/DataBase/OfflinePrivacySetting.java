package DataBase;

import FileHandling.FileHandler;
import Models.Events.ChangePrivacyEvent;
import Utils.GsonHandler;

import java.io.*;

public class OfflinePrivacySetting {
    public static ChangePrivacyEvent loadLast() {
        File file = new File(FileHandler.loadLocation("privacySetting"));
        try {
            Reader reader = new FileReader(file);
            ChangePrivacyEvent conversationListResponse =
                    GsonHandler.getGson().fromJson(reader, ChangePrivacyEvent.class);
            reader.close();
            return conversationListResponse;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(ChangePrivacyEvent conversationResponse) {
        File file = new File(FileHandler.loadLocation("privacySetting"));
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(file);
            printStream.print(GsonHandler.getGson().toJson(conversationResponse));
            printStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            printStream.close();
        }
    }
}
