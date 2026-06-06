package com.crm.common.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class CsvUtils {

    // Escape a single CSV field according to RFC4180 basic rules
    public static String escape(String field) {
        if (field == null) return "";
        boolean mustQuote = field.contains(",") || field.contains("\n") || field.contains("\r") || field.contains("\"");
        String escaped = field.replace("\"", "\"\"");
        if (mustQuote) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }

    public static void writeRow(Writer writer, List<String> columns) throws IOException {
        String line = columns.stream().map(CsvUtils::escape).collect(Collectors.joining(","));
        writer.write(line);
        writer.write('\n');
        writer.flush();
    }
}

