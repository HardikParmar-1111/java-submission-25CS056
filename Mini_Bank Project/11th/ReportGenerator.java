package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ReportGenerator {
    public static String generate(Path logsFolder, Path reportFile) throws IOException {
        long deposits = 0;
        long withdrawals = 0;
        ArrayList<String> fileDetails = new ArrayList<>();

        Files.createDirectories(logsFolder);
        try (DirectoryStream<Path> files = Files.newDirectoryStream(logsFolder)) {
            for (Path file : files) {
                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(file, BasicFileAttributes.class);
                fileDetails.add(file.getFileName() + " - "
                        + attributes.size() + " bytes - "
                        + attributes.lastModifiedTime());

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.trim().split("\\s+");
                        if (parts.length < 3) {
                            continue;
                        }

                        try {
                            long amount = Long.parseLong(parts[2]);
                            if (parts[0].equals("DEPOSIT")) {
                                deposits += amount;
                            } else if (parts[0].equals("WITHDRAW")) {
                                withdrawals += amount;
                            }
                        } catch (NumberFormatException e) {
                            throw new IOException("Invalid amount in " + file + ": " + line, e);
                        }
                    }
                }
            }
        }

        StringBuilder report = new StringBuilder();
        report.append("Deposits: ").append(deposits).append('\n');
        report.append("Withdrawals: ").append(withdrawals).append('\n');
        report.append("Net change: ").append(deposits - withdrawals).append('\n');
        report.append("Log files:\n");
        for (String detail : fileDetails) {
            report.append(detail).append('\n');
        }

        Path parent = reportFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(reportFile, report.toString());
        return report.toString();
    }
}
