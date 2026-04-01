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

        Set<Integer> numericColumns = detectNumericColumns(headers, rows, statusColumnSet);

        System.out.println(CliColor.title(title));
        String line = separator(widths);
        System.out.println(line);
        System.out.println(row(headers, widths, true, numericColumns));
        System.out.println(line);
        if (rows.isEmpty()) {
            System.out.println("| " + padRight(CliColor.warning("(No data)"), line.length() - 4) + " |");
        } else {
            for (String[] row : rows) {
                System.out.println(row(row, widths, false, numericColumns));
            }
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

    private static String row(String[] cells, int[] widths, boolean isHeader, Set<Integer> numericColumns) {
        StringBuilder builder = new StringBuilder("|");
        for (int i = 0; i < widths.length; i++) {
            String value = i < cells.length && cells[i] != null ? cells[i] : "";
            String padded = isHeader || !numericColumns.contains(i)
                    ? padRight(value, widths[i])
                    : padLeft(value, widths[i]);
            builder.append(" ").append(isHeader ? CliColor.header(padded) : padded).append(" |");
        }
        return builder.toString();
    }

    private static String padRight(String text, int width) {
        int printableLength = visibleLength(text);
        int padding = Math.max(width - printableLength, 0);
        return text + " ".repeat(padding);
    }

    private static String padLeft(String text, int width) {
        int printableLength = visibleLength(text);
        int padding = Math.max(width - printableLength, 0);
        return " ".repeat(padding) + text;
    }

    private static Set<Integer> detectNumericColumns(String[] headers, List<String[]> rows, Set<Integer> statusColumnSet) {
        Set<Integer> numericColumns = new HashSet<>();
        for (int i = 0; i < headers.length; i++) {
            if (statusColumnSet.contains(i)) {
                continue;
            }
            String header = headers[i] == null ? "" : headers[i].toLowerCase();
            if (header.contains("id") || header.contains("price") || header.contains("qty")
                    || header.contains("total") || header.contains("capacity") || header.contains("rating")) {
                numericColumns.add(i);
                continue;
            }

            boolean allNumeric = !rows.isEmpty();
            for (String[] row : rows) {
                String value = i < row.length ? CliColor.stripAnsi(row[i]).trim() : "";
                if (value.isEmpty()) {
                    continue;
                }
                if (!value.matches("^-?\\d+(\\.\\d+)?$")) {
                    allNumeric = false;
                    break;
                }
            }
            if (allNumeric) {
                numericColumns.add(i);
            }
        }
        return numericColumns;
    }

    private static int visibleLength(String text) {
        return CliColor.stripAnsi(text).length();
    }
}

