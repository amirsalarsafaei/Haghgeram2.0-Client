package Holder;

import FileHandling.FileHandler;
import Models.OfflineRequests;
import Utils.GsonHandler;

import java.io.*;
import java.util.Objects;

public class OfflineRequestHolder {
    public static OfflineRequests offlineRequests;


    public void loadIt() {
        try {
            Reader reader = new FileReader(Objects.requireNonNull(FileHandler.loadLocation("offline-requests")));
            OfflineRequests offlineRequests1 = GsonHandler.getGson().fromJson(reader, OfflineRequests.class);
            offlineRequests = offlineRequests1;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        File file = new File(Objects.requireNonNull(FileHandler.loadLocation("offline-requests")));
        try {
            PrintStream printStream = new PrintStream(file);
            String json = GsonHandler.getGson().toJson(offlineRequests);
            printStream.print(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
