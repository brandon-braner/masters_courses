package io.collective;

import java.time.Clock;

public class SimpleAgedCache {
    private Node head = null;
    private Clock clock;

    public SimpleAgedCache(Clock clock) {
        this.head = null;
        this.clock = clock;
    }

    private class ExpirableEntry {
        Object key;
        Object value;
        long expirationTime;

        ExpirableEntry(Object key, Object value, long expirationTime) {
            this.key = key;
            this.value = value;
            this.expirationTime = expirationTime;
        }

        boolean isExpired(long currentTimeMillis) {
            return currentTimeMillis > expirationTime;
        }
    }

    private class Node {
        ExpirableEntry entry;
        Node next;

        Node(ExpirableEntry entry) {
            this.entry = entry;
            this.next = null;
        }
    }

    public void put(Object key, Object value, int retentionInMillis) {
        ExpirableEntry newEntry = new ExpirableEntry(key, value, this.clock.millis() + retentionInMillis);
        Node newNode = new Node(newEntry);

        newNode.next = head;
        head = newNode;
    }

    public Object get(Object key) {
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current.entry.key.equals(key)) {
                if (current.entry.isExpired(this.clock.millis())) {
                    // Remove the expired entry and continue searching
                    if (previous == null) {
                        head = current.next; // Removing the head
                    } else {
                        previous.next = current.next; // Removing middle or last element
                    }
                    current = current.next;
                    continue;
                }
                return current.entry.value;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    public int size() {
        int count = 0;
        Node current = head;
        while (current != null) {
            if (!current.entry.isExpired(this.clock.millis())) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    public boolean isEmpty() {
        return head == null;
    }
}