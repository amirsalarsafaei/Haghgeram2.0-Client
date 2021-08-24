package DataBase;

import FileHandling.FileHandler;
import Models.Responses.ConversationListResponse;
import Utils.GsonHandler;

import java.io.*;

public class OfflineConversationList {
    public static ConversationListResponse loadLast() {
        File file = new File(FileHandler.loadLocation("conversationList"));
        try {
            Reader reader = new FileReader(file);
            ConversationListResponse conversationListResponse =
                    GsonHandler.getGson().fromJson(reader, ConversationListResponse.class);
            reader.close();
            return conversationListResponse;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(ConversationListResponse conversationResponse) {
        File file = new File(FileHandler.loadLocation("conversationList"));
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
