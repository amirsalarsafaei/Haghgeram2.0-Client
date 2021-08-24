package DataBase;

import FileHandling.FileHandler;
import Models.UserDetails;
import Utils.GsonHandler;

import java.io.*;

public class OfflineUserDetails {
    public static UserDetails load() {
        File file = new File(FileHandler.loadLocation("userDetails"));
        try {
            Reader reader = new FileReader(file);
            UserDetails conversationListResponse =
                    GsonHandler.getGson().fromJson(reader, UserDetails.class);
            reader.close();
            return conversationListResponse;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static void Save(UserDetails conversationResponse) {
        File file = new File(FileHandler.loadLocation("userDetails"));
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
