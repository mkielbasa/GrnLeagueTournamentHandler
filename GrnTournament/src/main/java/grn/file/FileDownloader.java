package grn.file;

import grn.error.ConsoleHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileDownloader {

    public static File download (String url, String savePath) {
        ConsoleHandler.handleInfo("Downloading file " + url);
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());) {
            return updateFile(in, savePath);
        } catch (IOException e) {
            ConsoleHandler.handleException(e);
        }
        return null;
    }

    private static File updateFile (BufferedInputStream in, String savePath) throws IOException {
        File outputFile = new File(savePath);
        if (!outputFile.exists())
            outputFile.createNewFile();
        Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
        return outputFile;
    }
}
