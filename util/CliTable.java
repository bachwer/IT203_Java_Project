package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CliTable {
    private CliTable() {
    }

    public static void print(String title, String[] headers, List<String[]> rawRows, int... statusColumns) {
        if (headers == null || headers.length == 0) {
            return;
        }

        Set<Integer> statusColumnSet = new HashSet<>();
        for (int index : statusColumns) {
            statusColumnSet.add(index);
        }

        List<String[]> rows = new ArrayList<>();
        if (rawRows != null) {
            for (String[] row : rawRows) {
                if (row == null) {
                    continue;
                }
                String[] normalized = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    String cell = i < row.length && row[i] != null ? row[i] : "";
                    normalized[i] = statusColumnSet.contains(i) ? CliColor.byStatus(cell) : cell;
                }
                rows.add(normalized);
            }
        }

        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = visibleLength(headers[i]);
        }

        for (String[] row : rows) {
            for (int i = 0; i < headers.length; i++) {
                widths[i] = Math.max(widths[i], visibleLength(row[i]));
            }
        }

        System.out.println(CliColor.title(title));
        String line = separator(widths);
        System.out.println(line);
        System.out.println(row(headers, widths, true));
        System.out.println(line);
        for (String[] row : rows) {
            System.out.println(row(row, widths, false));
        }
        System.out.println(line);
    }

    private static String separator(int[] widths) {
        StringBuilder builder = new StringBuilder("+");
        for (int width : widths) {
            builder.append("-").append("-".repeat(width)).append("-+");
        }
        return builder.toString();
    }

    private static String row(String[] cells, int[] widths, boolean isHeader) {
        StringBuilder builder = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            String value = i < cells.length && cells[i] != null ? cells[i] : "";
            String padded = padRight(value, widths[i]);
            builder.append(" ").append(isHeader ? CliColor.header(padded) : padded).append(" |");
        }
        return builder.toString();
    }

    private static String padRight(String text, int width) {
        int printableLength = visibleLength(text);
        int padding = Math.max(width - printableLength, 0);
        return text + " ".repeat(padding);
    }

    private static int visibleLength(String text) {
        return CliColor.stripAnsi(text).length();
    }
}

