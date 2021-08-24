package DataBase;

import FileHandling.FileHandler;
import Models.OfflineRequests;
import Utils.GsonHandler;

import java.io.*;

public class OfflineRequest {
    public static OfflineRequests load() {
        File file = new File(FileHandler.loadLocation("offlineRequests") );
        try {
            Reader reader = new FileReader(file);
            OfflineRequests conversationListResponse =
                    GsonHandler.getGson().fromJson(reader, OfflineRequests.class);
            reader.close();
            return conversationListResponse;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(OfflineRequests conversationResponse) {
        File file = new File(FileHandler.loadLocation("offlineRequests"));
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
