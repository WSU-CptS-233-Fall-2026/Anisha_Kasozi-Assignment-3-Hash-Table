/**
 * MA3Main - Main test runner for BucketHashTable and LinearHashTable.
 * Demonstrates insertion, retrieval, update, collision handling, and lazy deletion.
 */
public class MA3Main {

    public static void main(String[] args) {
        System.out.println("=== Testing Linear HashTable ===");
        LinearHashTable<String, String> lht = new LinearHashTable<>(new SimpleStringHasher());
        MA3Main.linearHashTableTest(lht);

        System.out.println("\n=== Testing Bucket HashTable ===");
        BucketHashTable<String, String> bht = new BucketHashTable<>(new SimpleStringHasher());
        MA3Main.bucketHashTableTest(bht);
    }

    public static void linearHashTableTest(LinearHashTable<String, String> ht) {
        // Test 1: Insertion and Basic Search
        // Expected Output: contains "apple" -> true, get "apple" -> "fruit"
        ht.addElement("apple", "fruit");
        System.out.println("Contains 'apple': " + ht.containsElement("apple") + " (Expected: true)");
        System.out.println("Get 'apple': " + ht.getElement("apple") + " (Expected: fruit)");

        // Test 2: Value Updating
        // Expected Output: get "apple" -> "red fruit"
        ht.addElement("apple", "red fruit");
        System.out.println("Updated 'apple': " + ht.getElement("apple") + " (Expected: red fruit)");

        // Test 3: Collision Handling & Probing
        // "abc" (97+98+99 = 294 % 11 = 8) and "cba" (99+98+97 = 294 % 11 = 8) cause collision
        ht.addElement("abc", "val1");
        ht.addElement("cba", "val2");
        System.out.println("Contains 'abc': " + ht.containsElement("abc") + " (Expected: true)");
        System.out.println("Contains 'cba': " + ht.containsElement("cba") + " (Expected: true)");

        // Test 4: Lazy Deletion and Probing past Tombstone
        // Deleting "abc" creates tombstone at index 8. "cba" probe must still find value at index 9.
        ht.removeElement("abc");
        System.out.println("After removing 'abc':");
        System.out.println("Contains 'abc': " + ht.containsElement("abc") + " (Expected: false)");
        System.out.println("Contains 'cba' (probe past tombstone): " + ht.containsElement("cba") + " (Expected: true)");
        System.out.println("Get 'cba': " + ht.getElement("cba") + " (Expected: val2)");
    }

    public static void bucketHashTableTest(BucketHashTable<String, String> ht) {
        // Test 1: Insertion and Basic Search
        // Expected Output: contains "cat" -> true, get "cat" -> "meow"
        ht.addElement("cat", "meow");
        System.out.println("Contains 'cat': " + ht.containsElement("cat") + " (Expected: true)");
        System.out.println("Get 'cat': " + ht.getElement("cat") + " (Expected: meow)");

        // Test 2: Separate Chaining Collision Handling
        // "act" and "cat" both hash to the same bucket
        ht.addElement("act", "play");
        System.out.println("Contains 'act' (chained in same bucket): " + ht.containsElement("act") + " (Expected: true)");
        System.out.println("Get 'act': " + ht.getElement("act") + " (Expected: play)");

        // Test 3: Value Updating
        ht.addElement("cat", "feline");
        System.out.println("Updated 'cat': " + ht.getElement("cat") + " (Expected: feline)");

        // Test 4: Deletion
        ht.removeElement("cat");
        System.out.println("After removing 'cat':");
        System.out.println("Contains 'cat': " + ht.containsElement("cat") + " (Expected: false)");
        System.out.println("Contains 'act' (sibling in chain): " + ht.containsElement("act") + " (Expected: true)");
    }
}
