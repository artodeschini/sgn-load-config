package br.com.bb.sgn.config;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InMemoryConfigSource implements ConfigSource {

    private final Map<String, String> values = new HashMap<>();
    private int ordinal;
    private String name;

    public InMemoryConfigSource() {}

    public void add(String key, String value) {
        values.put(key, value);
    }

    @Override
    public Map<String, String> getProperties() {
        return values;
    }

    @Override
    public Set<String> getPropertyNames() {
        return values.keySet();
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String getValue(String propertyName) {
        return values.get(propertyName);
    }

    @Override
    public String getName() {
        return name;
    }
}
