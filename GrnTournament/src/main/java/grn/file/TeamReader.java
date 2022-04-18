package grn.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamReader {

    public static List<String> read(File file) {
        List<String> teams = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(""))
                    continue;
                teams.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teams;
    }
}
