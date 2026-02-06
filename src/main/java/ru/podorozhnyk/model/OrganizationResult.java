package ru.podorozhnyk.model;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class OrganizationResult {
    private final Map<String, String> successLogs;
    private final Map<String, String> skipLogs;

    private OrganizationResult(Builder builder) {
        successLogs = Map.copyOf(builder.successLogs);
        skipLogs = Map.copyOf(builder.skipLogs);
    }

    @Override
    public String toString() {
        return "Finished! " + "Processed: " + successLogs.size() +
                " Skipped: " + skipLogs.size() + " Total: " + (successLogs.size() + skipLogs.size());
    }

    public String toStringFull() {
        StringBuilder output = new StringBuilder();

        output.append("=".repeat(10)).append(" FINISHED ").append("=".repeat(10)).append('\n');
        successLogs.forEach((source, target)
                -> output.append("[OK] ").append(source).append(" -> ").append(target).append('\n'));
        skipLogs.forEach((source, target)
                -> output.append("[SKIPPED] ").append(source).append(" -> ").append(target).append('\n'));

        String counter = " P:" + successLogs.size()
                + " S:" + skipLogs.size()
                + " T:" + (skipLogs.size() + successLogs.size()) + " ";
        int borderLength = (30 - counter.length()) / 2;
        output.append("-".repeat(borderLength)).append(counter).append("-".repeat(borderLength + 1)).append('\n');

        output.append("=".repeat(30)).append('\n');
        return output.toString();
    }




    public static class Builder {
        private final Map<String, String> successLogs;
        private final Map<String, String> skipLogs;

        private Builder() {
            skipLogs = new HashMap<>();
            successLogs = new HashMap<>();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder ok(Path source, Path target) {
            successLogs.put(source.toString(), target.toString());
            return this;
        }

        public Builder skip(Path source, Path target) {
            skipLogs.put(source.toString(), target.toString());
            return this;
        }

        public OrganizationResult build() {
            return new OrganizationResult(this);
        }
    }
}
