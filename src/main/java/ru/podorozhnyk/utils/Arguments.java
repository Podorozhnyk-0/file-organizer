package ru.podorozhnyk.utils;

import java.util.*;

public final class Arguments {
    private final Map<String, String> arguments;
    private Arguments(String[] args) {
        arguments = new LinkedHashMap<>();

        for (String arg : args) {
            if (arg.contains("=")) {
                String[] splitArgs = arg.split("=", 2);
                String key = splitArgs[0].trim();
                String value = splitArgs[1].trim();
                if (key.isBlank())
                    throw  new IllegalArgumentException("Key is empty");
                arguments.put(key, value);
            } else {
                arguments.put(arg.trim(), null);
            }

        }
    }

    public static Arguments from(String[] args) {
        return new Arguments(args);
    }

    public boolean hasKey(String key) {
        return arguments.containsKey(key);
    }

    public String getValue(String key) {
        if (!hasKey(key))
            throw new NoSuchElementException(key);
        return arguments.get(key);
    }
}
