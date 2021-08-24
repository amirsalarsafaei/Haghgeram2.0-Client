package DataBase;

import FileHandling.FileHandler;
import Models.UnsentMessages;
import Utils.GsonHandler;

import java.io.*;

public class OfflineUnsentMessages {
    public static UnsentMessages load() {
        File file = new File(FileHandler.loadLocation("unsentMessages"));
        try {
            Reader reader = new FileReader(file);
           UnsentMessages conversationListResponse =
                    GsonHandler.getGson().fromJson(reader, UnsentMessages.class);
            reader.close();
            return conversationListResponse;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(UnsentMessages conversationResponse) {
        File file = new File(FileHandler.loadLocation("unsentMessages"));
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
