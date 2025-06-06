package com.example.taskservice.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

@Component
public class LRUCache <K,V>{


    private int capacity;
    private Map<K,V> cache;
    private LinkedList<K> orderList;

    public LRUCache(  @Value("${cache.task.maximum-size:5}") String capacity) {
        this.capacity = Integer.parseInt(capacity);
        this.cache = new HashMap<>();
        this.orderList = new LinkedList<>();
    }

    public synchronized Optional<V> get(K key){
        if(!cache.containsKey(key)) return Optional.empty();

        orderList.remove(key);
        orderList.addFirst(key);

        return Optional.of(cache.get(key));
    }

    public synchronized void put(K key, V val){
        if(cache.containsKey(key)){
            orderList.remove(key);
        } else if( cache.size() >= capacity){
           K lruKey =  orderList.removeLast();
           cache.remove(lruKey);
        }
        cache.put(key, val);
        orderList.addFirst(key);

    }
}