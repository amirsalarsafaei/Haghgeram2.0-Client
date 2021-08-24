package DataBase;

import FileHandling.FileHandler;
import Models.Events.EditProfileEvent;
import Utils.GsonHandler;

import java.io.*;

public class OfflineProfileSetting {
    public static EditProfileEvent loadLast() {
        File file = new File(FileHandler.loadLocation("profileSetting"));
        try {
            Reader reader = new FileReader(file);
            EditProfileEvent conversationListResponse =
                    GsonHandler.getGson().fromJson(reader, EditProfileEvent.class);
            reader.close();
            return conversationListResponse;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(EditProfileEvent conversationResponse) {
        File file = new File(FileHandler.loadLocation("profileSetting"));
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
