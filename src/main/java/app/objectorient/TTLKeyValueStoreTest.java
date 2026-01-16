package app.objectorient;
// Question 3: Key-Value Store with TTL (Time To Live) Support
// Added TTL functionality while maintaining all previous features

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;

// Value wrapper class to store value with optional TTL
class TTLValue<V> {
    private final V value;
    private final Long expirationTime; // null means no TTL
    private final long createdTime;

    public TTLValue(V value) {
        this.value = value;
        this.expirationTime = null;
        this.createdTime = System.currentTimeMillis();
    }

    public TTLValue(V value, long ttlSeconds) {
        this.value = value;
        this.createdTime = System.currentTimeMillis();
        this.expirationTime = this.createdTime + (ttlSeconds * 1000);
    }

    public TTLValue(V value, long ttlSeconds, long currentTimestamp) {
        this.value = value;
        this.createdTime = currentTimestamp;
        this.expirationTime = ttlSeconds > 0 ? currentTimestamp + (ttlSeconds * 1000) : null;
    }

    public V getValue() {
        return value;
    }

    public boolean isExpired(long currentTimestamp) {
        return expirationTime != null && currentTimestamp >= expirationTime;
    }

    public boolean hasTTL() {
        return expirationTime != null;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getRemainingTTL(long currentTimestamp) {
        if (expirationTime == null) {
            return -1; // No TTL
        }
        long remaining = expirationTime - currentTimestamp;
        return Math.max(0, remaining / 1000); // Return in seconds
    }

    @Override
    public String toString() {
        return String.format("TTLValue{value=%s, expirationTime=%s, hasTTL=%s}",
                value, expirationTime, hasTTL());
    }
}

// Time provider interface for dependency injection and testing
interface TimeProvider {
    long getCurrentTimestamp();
}

// Default time provider
class SystemTimeProvider implements TimeProvider {
    @Override
    public long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}

// Test time provider for controlled testing
class MockTimeProvider implements TimeProvider {
    private long currentTime;

    public MockTimeProvider(long initialTime) {
        this.currentTime = initialTime;
    }

    public void setCurrentTime(long time) {
        this.currentTime = time;
    }

    public void advanceTime(long milliseconds) {
        this.currentTime += milliseconds;
    }

    @Override
    public long getCurrentTimestamp() {
        return currentTime;
    }
}

// Base interface for basic operations
interface KeyValueStore<K, V> {
    void set(K key, V value);
    Optional<V> get(K key);
    boolean delete(K key);
    boolean update(K key, V newValue);
    boolean containsKey(K key);
    int size();
}

// Extended interface for search operations
interface SearchableKeyValueStore<K, V> extends KeyValueStore<K, V> {
    List<K> prefixSearch(String prefix);
    List<K> containsSearch(String searchTerm);
    List<V> getValuesByPrefix(String prefix);
    List<V> getValuesByContains(String searchTerm);
}

// Extended interface for TTL operations
interface TTLKeyValueStore<K, V> extends SearchableKeyValueStore<K, V> {
    void set(K key, V value, long ttlSeconds);
    void set(K key, V value, long ttlSeconds, long timestamp);
    boolean update(K key, V newValue, long ttlSeconds);
    Optional<V> getAtTimestamp(K key, long timestamp);
    boolean containsKeyAtTimestamp(K key, long timestamp);
    int sizeAtTimestamp(long timestamp);
    List<K> prefixSearchAtTimestamp(String prefix, long timestamp);
    List<K> containsSearchAtTimestamp(String searchTerm, long timestamp);
    long getRemainingTTL(K key);
    long getRemainingTTLAtTimestamp(K key, long timestamp);
    void cleanupExpired();
    void cleanupExpiredAtTimestamp(long timestamp);
    Map<K, V> getSnapshotAtTimestamp(long timestamp);
}

// Strategy interface for search operations
interface SearchStrategy<K> {
    List<K> search(Collection<K> keys, String searchTerm);
}

// Concrete strategies (unchanged from Question 2)
class PrefixSearchStrategy<K> implements SearchStrategy<K> {
    @Override
    public List<K> search(Collection<K> keys, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return new ArrayList<>();
        }
        return keys.stream()
                .filter(key -> key != null && key.toString().startsWith(searchTerm))
                .collect(Collectors.toList());
    }
}

class ContainsSearchStrategy<K> implements SearchStrategy<K> {
    @Override
    public List<K> search(Collection<K> keys, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return new ArrayList<>();
        }
        return keys.stream()
                .filter(key -> key != null && key.toString().contains(searchTerm))
                .collect(Collectors.toList());
    }
}

// Main implementation with TTL support
class TTLKeyValueStoreImpl<K, V> implements TTLKeyValueStore<K, V> {
    private final Map<K, TTLValue<V>> store;
    private final SearchStrategy<K> prefixSearchStrategy;
    private final SearchStrategy<K> containsSearchStrategy;
    private final TimeProvider timeProvider;

    // Default constructor
    public TTLKeyValueStoreImpl() {
        this.store = new ConcurrentHashMap<>();
        this.prefixSearchStrategy = new PrefixSearchStrategy<>();
        this.containsSearchStrategy = new ContainsSearchStrategy<>();
        this.timeProvider = new SystemTimeProvider();
    }

    // Constructor with custom time provider (for testing)
    public TTLKeyValueStoreImpl(TimeProvider timeProvider) {
        this.store = new ConcurrentHashMap<>();
        this.prefixSearchStrategy = new PrefixSearchStrategy<>();
        this.containsSearchStrategy = new ContainsSearchStrategy<>();
        this.timeProvider = timeProvider;
    }

    // Constructor with all dependencies
    public TTLKeyValueStoreImpl(Map<K, TTLValue<V>> customMap,
                                SearchStrategy<K> prefixStrategy,
                                SearchStrategy<K> containsStrategy,
                                TimeProvider timeProvider) {
        this.store = customMap;
        this.prefixSearchStrategy = prefixStrategy;
        this.containsSearchStrategy = containsStrategy;
        this.timeProvider = timeProvider;
    }

    // === Basic CRUD Operations (with automatic TTL handling) ===

    @Override
    public void set(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        store.put(key, new TTLValue<>(value));
    }

    @Override
    public void set(K key, V value, long ttlSeconds) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        store.put(key, new TTLValue<>(value, ttlSeconds, timeProvider.getCurrentTimestamp()));
    }

    @Override
    public void set(K key, V value, long ttlSeconds, long timestamp) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        store.put(key, new TTLValue<>(value, ttlSeconds, timestamp));
    }

    @Override
    public Optional<V> get(K key) {
        return getAtTimestamp(key, timeProvider.getCurrentTimestamp());
    }

    @Override
    public Optional<V> getAtTimestamp(K key, long timestamp) {
        if (key == null) {
            return Optional.empty();
        }
        TTLValue<V> ttlValue = store.get(key);
        if (ttlValue == null || ttlValue.isExpired(timestamp)) {
            return Optional.empty();
        }
        return Optional.of(ttlValue.getValue());
    }

    @Override
    public boolean delete(K key) {
        if (key == null) {
            return false;
        }
        return store.remove(key) != null;
    }

    @Override
    public boolean update(K key, V newValue) {
        if (key == null) {
            return false;
        }
        TTLValue<V> existing = store.get(key);
        if (existing == null || existing.isExpired(timeProvider.getCurrentTimestamp())) {
            return false;
        }
        // Preserve original TTL settings
        if (existing.hasTTL()) {
            long originalTTL = existing.getRemainingTTL(timeProvider.getCurrentTimestamp());
            store.put(key, new TTLValue<>(newValue, originalTTL, timeProvider.getCurrentTimestamp()));
        } else {
            store.put(key, new TTLValue<>(newValue));
        }
        return true;
    }

    @Override
    public boolean update(K key, V newValue, long ttlSeconds) {
        if (key == null) {
            return false;
        }
        TTLValue<V> existing = store.get(key);
        if (existing == null || existing.isExpired(timeProvider.getCurrentTimestamp())) {
            return false;
        }
        store.put(key, new TTLValue<>(newValue, ttlSeconds, timeProvider.getCurrentTimestamp()));
        return true;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyAtTimestamp(key, timeProvider.getCurrentTimestamp());
    }

    @Override
    public boolean containsKeyAtTimestamp(K key, long timestamp) {
        if (key == null) {
            return false;
        }
        TTLValue<V> ttlValue = store.get(key);
        return ttlValue != null && !ttlValue.isExpired(timestamp);
    }

    @Override
    public int size() {
        return sizeAtTimestamp(timeProvider.getCurrentTimestamp());
    }

    @Override
    public int sizeAtTimestamp(long timestamp) {
        return (int) store.values().stream()
                .filter(ttlValue -> !ttlValue.isExpired(timestamp))
                .count();
    }

    // === Search Operations with TTL handling ===

    @Override
    public List<K> prefixSearch(String prefix) {
        return prefixSearchAtTimestamp(prefix, timeProvider.getCurrentTimestamp());
    }

    @Override
    public List<K> prefixSearchAtTimestamp(String prefix, long timestamp) {
        Set<K> validKeys = getValidKeysAtTimestamp(timestamp);
        return prefixSearchStrategy.search(validKeys, prefix);
    }

    @Override
    public List<K> containsSearch(String searchTerm) {
        return containsSearchAtTimestamp(searchTerm, timeProvider.getCurrentTimestamp());
    }

    @Override
    public List<K> containsSearchAtTimestamp(String searchTerm, long timestamp) {
        Set<K> validKeys = getValidKeysAtTimestamp(timestamp);
        return containsSearchStrategy.search(validKeys, searchTerm);
    }

    @Override
    public List<V> getValuesByPrefix(String prefix) {
        List<K> matchingKeys = prefixSearch(prefix);
        return matchingKeys.stream()
                .map(this::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<V> getValuesByContains(String searchTerm) {
        List<K> matchingKeys = containsSearch(searchTerm);
        return matchingKeys.stream()
                .map(this::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // === TTL-specific Operations ===

    @Override
    public long getRemainingTTL(K key) {
        return getRemainingTTLAtTimestamp(key, timeProvider.getCurrentTimestamp());
    }

    @Override
    public long getRemainingTTLAtTimestamp(K key, long timestamp) {
        if (key == null) {
            return -2; // Key doesn't exist
        }
        TTLValue<V> ttlValue = store.get(key);
        if (ttlValue == null) {
            return -2; // Key doesn't exist
        }
        if (ttlValue.isExpired(timestamp)) {
            return 0; // Expired
        }
        return ttlValue.getRemainingTTL(timestamp);
    }

    @Override
    public void cleanupExpired() {
        cleanupExpiredAtTimestamp(timeProvider.getCurrentTimestamp());
    }

    @Override
    public void cleanupExpiredAtTimestamp(long timestamp) {
        store.entrySet().removeIf(entry -> entry.getValue().isExpired(timestamp));
    }

    @Override
    public Map<K, V> getSnapshotAtTimestamp(long timestamp) {
        return store.entrySet().stream()
                .filter(entry -> !entry.getValue().isExpired(timestamp))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getValue()
                ));
    }

    // === Helper Methods ===

    private Set<K> getValidKeysAtTimestamp(long timestamp) {
        return store.entrySet().stream()
                .filter(entry -> !entry.getValue().isExpired(timestamp))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    public void clear() {
        store.clear();
    }

    public Set<K> getAllKeys() {
        return getValidKeysAtTimestamp(timeProvider.getCurrentTimestamp());
    }

    @Override
    public String toString() {
        long currentTime = timeProvider.getCurrentTimestamp();
        Map<K, V> snapshot = getSnapshotAtTimestamp(currentTime);
        return snapshot.toString();
    }
}

// Enhanced factory with TTL support
class TTLKeyValueStoreFactory {
    public static <K, V> TTLKeyValueStore<K, V> createDefault() {
        return new TTLKeyValueStoreImpl<>();
    }

    public static <K, V> TTLKeyValueStore<K, V> createWithTimeProvider(TimeProvider timeProvider) {
        return new TTLKeyValueStoreImpl<>(timeProvider);
    }

    public static <K, V> TTLKeyValueStore<K, V> createConcurrent() {
        return new TTLKeyValueStoreImpl<>(new ConcurrentHashMap<>(),
                new PrefixSearchStrategy<>(),
                new ContainsSearchStrategy<>(),
                new SystemTimeProvider());
    }
}

// Comprehensive test class
class TTLKeyValueStoreTest {
    public static void main(String[] args) {
        System.out.println("=== TTL Key-Value Store Test ===");

        // Test with mock time provider for controlled testing
        MockTimeProvider timeProvider = new MockTimeProvider(1000000L); // Start at 1000 seconds
        TTLKeyValueStoreImpl<String, String> kvStore = new TTLKeyValueStoreImpl<>(timeProvider);

        // === Test Basic Operations (Regression) ===
        System.out.println("\n=== Basic Operations Test (Regression) ===");
        kvStore.set("permanent", "This value has no TTL");
        kvStore.set("temporary", "This will expire", 5); // 5 seconds TTL
        kvStore.set("short_lived", "Very short", 1); // 1 second TTL

        System.out.println("Initial state: " + kvStore);
        System.out.println("Size: " + kvStore.size());

        // Test get operations
        System.out.println("Get permanent: " + kvStore.get("permanent").orElse("Not found"));
        System.out.println("Get temporary: " + kvStore.get("temporary").orElse("Not found"));
        System.out.println("Get short_lived: " + kvStore.get("short_lived").orElse("Not found"));

        // === Test TTL Functionality ===
        System.out.println("\n=== TTL Functionality Test ===");
        System.out.println("Remaining TTL for permanent: " + kvStore.getRemainingTTL("permanent"));
        System.out.println("Remaining TTL for temporary: " + kvStore.getRemainingTTL("temporary"));
        System.out.println("Remaining TTL for short_lived: " + kvStore.getRemainingTTL("short_lived"));

        // Advance time by 2 seconds
        timeProvider.advanceTime(2000);
        System.out.println("\n--- After 2 seconds ---");
        System.out.println("Current state: " + kvStore);
        System.out.println("Size: " + kvStore.size());
        System.out.println("Get short_lived: " + kvStore.get("short_lived").orElse("EXPIRED"));
        System.out.println("Get temporary: " + kvStore.get("temporary").orElse("EXPIRED"));

        // Advance time by 5 more seconds (7 total)
        timeProvider.advanceTime(5000);
        System.out.println("\n--- After 7 seconds total ---");
        System.out.println("Current state: " + kvStore);
        System.out.println("Size: " + kvStore.size());
        System.out.println("Get temporary: " + kvStore.get("temporary").orElse("EXPIRED"));
        System.out.println("Get permanent: " + kvStore.get("permanent").orElse("Not found"));

        // === Test Timestamp-based Operations ===
        System.out.println("\n=== Timestamp-based Operations Test ===");
        long initialTime = 1000000L;
        long afterOneSecond = initialTime + 1000;
        long afterSixSeconds = initialTime + 6000;

        System.out.println("Snapshot at initial time: " + kvStore.getSnapshotAtTimestamp(initialTime));
        System.out.println("Snapshot after 1 second: " + kvStore.getSnapshotAtTimestamp(afterOneSecond));
        System.out.println("Snapshot after 6 seconds: " + kvStore.getSnapshotAtTimestamp(afterSixSeconds));

        System.out.println("Size at initial time: " + kvStore.sizeAtTimestamp(initialTime));
        System.out.println("Size after 1 second: " + kvStore.sizeAtTimestamp(afterOneSecond));
        System.out.println("Size after 6 seconds: " + kvStore.sizeAtTimestamp(afterSixSeconds));

        // === Test Search with TTL ===
        System.out.println("\n=== Search with TTL Test ===");
        // Reset and add test data
        kvStore.clear();
        timeProvider.setCurrentTime(2000000L);

        kvStore.set("user_name", "John", 10);
        kvStore.set("user_email", "john@example.com"); // No TTL
        kvStore.set("admin_name", "Admin", 2);
        kvStore.set("temp_token", "abc123", 1);

        System.out.println("Initial search state:");
        System.out.println("Prefix search 'user_': " + kvStore.prefixSearch("user_"));
        System.out.println("Contains search 'name': " + kvStore.containsSearch("name"));

        // Advance time to expire some items
        timeProvider.advanceTime(3000); // 3 seconds
        System.out.println("\nAfter 3 seconds:");
        System.out.println("Prefix search 'user_': " + kvStore.prefixSearch("user_"));
        System.out.println("Contains search 'name': " + kvStore.containsSearch("name"));

        // Test search at specific timestamps
        long searchTime1 = 2000000L; // Initial time
        long searchTime2 = 2000000L + 1500; // 1.5 seconds later
        long searchTime3 = 2000000L + 3000; // 3 seconds later

        System.out.println("\nSearch at different timestamps:");
        System.out.println("At initial time - prefix 'user_': " + kvStore.prefixSearchAtTimestamp("user_", searchTime1));
        System.out.println("At 1.5 seconds - prefix 'user_': " + kvStore.prefixSearchAtTimestamp("user_", searchTime2));
        System.out.println("At 3 seconds - prefix 'user_': " + kvStore.prefixSearchAtTimestamp("user_", searchTime3));

        // === Test Update with TTL ===
        System.out.println("\n=== Update with TTL Test ===");
        kvStore.clear();
        timeProvider.setCurrentTime(3000000L);

        kvStore.set("test_key", "original", 5);
        System.out.println("Original: " + kvStore.get("test_key").orElse("Not found"));
        System.out.println("TTL: " + kvStore.getRemainingTTL("test_key"));

        // Update without changing TTL
        boolean updated1 = kvStore.update("test_key", "updated");
        System.out.println("Updated without TTL change: " + updated1);
        System.out.println("Value: " + kvStore.get("test_key").orElse("Not found"));
        System.out.println("TTL: " + kvStore.getRemainingTTL("test_key"));

        // Update with new TTL
        boolean updated2 = kvStore.update("test_key", "updated_with_new_ttl", 10);
        System.out.println("Updated with new TTL: " + updated2);
        System.out.println("Value: " + kvStore.get("test_key").orElse("Not found"));
        System.out.println("TTL: " + kvStore.getRemainingTTL("test_key"));

        // === Test Cleanup ===
        System.out.println("\n=== Cleanup Test ===");
        kvStore.clear();
        timeProvider.setCurrentTime(4000000L);

        kvStore.set("keep1", "permanent"); // No TTL
        kvStore.set("keep2", "long_term", 100); // Long TTL
        kvStore.set("expire1", "short1", 1); // Will expire
        kvStore.set("expire2", "short2", 2); // Will expire

        System.out.println("Before cleanup - size: " + kvStore.size());

        timeProvider.advanceTime(3000); // 3 seconds
        System.out.println("After time advance - size (with expired): " + kvStore.size());
        System.out.println("After time advance - effective size: " + kvStore.size());

        kvStore.cleanupExpired();
        System.out.println("After cleanup - actual size: " + kvStore.size());

        System.out.println("\n=== Test Complete ===");
    }
}