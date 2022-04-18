package grn.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerReader {
    public static Map<String, String> read(File file) {
        Map<String,String> players = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                String[] splitted = line.split("\\|");
                players.put(splitted[0], splitted[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }
}
