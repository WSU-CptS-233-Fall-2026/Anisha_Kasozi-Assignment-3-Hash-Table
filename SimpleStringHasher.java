class SimpleStringHasher extends HasherBase<String> {
    public int getHash(String key, int tableSize) {
        int hash = 0;
        for (char ch: key.toCharArray()) {
            hash += ch;
        }
        return hash % tableSize;
    }
}
