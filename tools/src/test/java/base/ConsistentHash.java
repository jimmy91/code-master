package base;

import java.util.*;
public class ConsistentHash {
    private SortedMap<Integer, String> hashCircle = new TreeMap<>();
    private int numberOfReplicas;
    public ConsistentHash(int numberOfReplicas, List<String> nodes) {
        this.numberOfReplicas = numberOfReplicas;
        for (String node: nodes) {
            addNode(node);
        }
    }
    public void addNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            String virtualNode = node + "_" + i;
            int hashValue = hash(virtualNode);
            hashCircle.put(hashValue, node);
        }
    }
    public void removeNode(String node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            String virtualNode = node + "_" + i;
            int hashValue = hash(virtualNode);
            hashCircle.remove(hashValue);
        }
    }
    public String getNode(String key) {
        if (hashCircle.isEmpty()) {
            return null;
        }
        int hashValue = hash(key);
        if (!hashCircle.containsKey(hashValue)) {
            SortedMap<Integer, String> tailMap = hashCircle.tailMap(hashValue);
            hashValue = tailMap.isEmpty() ? hashCircle.firstKey() : tailMap.firstKey();
        }
        return hashCircle.get(hashValue);
    }
    private int hash(String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash = hash ^ hash >>> 16;
        return hash;
    }
    public static void main(String[] args) {
        List<String> nodes = new ArrayList<>();
        nodes.add("192.168.0.100");
        nodes.add("192.168.0.101");
        nodes.add("192.168.0.102");
        ConsistentHash hash = new ConsistentHash(10, nodes);
        System.out.println(hash.getNode("hello")); // 192.168.0.101
        System.out.println(hash.getNode("world")); // 192.168.0.100
        hash.addNode("192.168.0.103");
        System.out.println(hash.getNode("hello")); // 192.168.0.101
        System.out.println(hash.getNode("world")); // 192.168.0.103
        hash.removeNode("192.168.0.100");
        System.out.println(hash.getNode("hello")); // 192.168.0.101
        System.out.println(hash.getNode("world")); // 192.168.0.103
    }
}