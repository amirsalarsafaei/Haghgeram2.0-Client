package DataBase;

import FileHandling.FileHandler;
import Models.Responses.ConversationResponse;
import Utils.GsonHandler;

import java.io.*;

public class OfflineConversation {
    public  static ConversationResponse loadLast(int id) {
        File file = new File(FileHandler.loadLocation("conversation.dir") +
                id);
        try {
            Reader reader = new FileReader(file);
            ConversationResponse conversationResponse =
                    GsonHandler.getGson().fromJson(reader, ConversationResponse.class);
            reader.close();
            return conversationResponse;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(ConversationResponse conversationResponse) {
        File file = new File(FileHandler.loadLocation("conversation.dir") +
                conversationResponse.getId());
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(file);
            printStream.print(GsonHandler.getGson().toJson(conversationResponse));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            printStream.close();
        }
    }
}
