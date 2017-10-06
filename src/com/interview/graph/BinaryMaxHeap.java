package com.interview.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryMaxHeap<T> {
    private List<Node> allNodes = new ArrayList<Node>();
    private Map<T, Integer> nodePosition = new HashMap<>();

    class Node {

        int weight;
        T key;
    }

    /**
     * Checks where the key exists in heap or not
     */
    public boolean containsData(T key) {
        return nodePosition.containsKey(key);
    }
    
    public int getWeight (T key) {
        Integer i = nodePosition.get(key);
        if (i != null) {
            return allNodes.get(i).weight;
        }        
        return -1;
    }

    /**
     * Add key and its weight to the heap
     */
    public void add(int weight, T key) {

        Node node = new Node();
        node.weight = weight;
        node.key = key;
        allNodes.add(node);
        int size = allNodes.size();
        int current = size - 1;
        int parentIndex = (current - 1) / 2;
        nodePosition.put(key, current);

        bubble(parentIndex, current);

    }

    private void bubble (int parentIndex, int current) {
        while (parentIndex >= 0) {
            Node parentNode = allNodes.get(parentIndex);
            Node currentNode = allNodes.get(current);
            if (parentNode.weight < currentNode.weight) {
                swap(parentNode, currentNode);
                updatePositionMap(parentNode.key, currentNode.key, parentIndex, current);
                current = parentIndex;
                parentIndex = (parentIndex - 1) / 2;
            } else {
                return;
            }
        }
    }
    
    private void sink(int current) {
        int size = allNodes.size();
        int left = 2 * current + 1;
        int right = 2 * current + 2;
        if (right >= size) {
            return;
        }        
        Node leftChild = allNodes.get(left);
        Node currentNode = allNodes.get(current);
        boolean leftChildLarger = false;
        boolean ignoreRight = right >= size;
        Node rightChild = ignoreRight ? null : allNodes.get(right);
        if (leftChild.weight > currentNode.weight) {
            leftChildLarger = true;
        }   
        if (!ignoreRight && rightChild.weight > currentNode.weight 
                && rightChild.weight > leftChild.weight) {       
            swap(currentNode, rightChild);
            updatePositionMap(currentNode.key, rightChild.key, current, right);
            sink(right);
            return;
        }        
        if (leftChildLarger) {
            swap(currentNode, leftChild);
            updatePositionMap(currentNode.key, leftChild.key, current, left);
            if (left == size - 1) {
                return;
            } else {
                sink(left);
            }           
            
        }       

    }

    private void swap(Node node1, Node node2) {
        int weight = node1.weight;
        T key = node1.key;

        node1.key = node2.key;
        node1.weight = node2.weight;

        node2.key = key;
        node2.weight = weight;
    }

    /**
     * Get the heap max without extracting the key
     */
    public T max() {
        return allNodes.get(0).key;
    }

    /**
     * check if heap is empty or not
     */
    public boolean empty() {
        return allNodes.size() == 0;
    }

    /**
     * Decreases the weight of given key to newWeight
     */
    public void update(T data, int newWeight) {        
        Integer current = nodePosition.get(data);
        if (current == null || allNodes.get(current).weight == newWeight) {
            return;
        }      
        int oldWeight = allNodes.get(current).weight;
        allNodes.get(current).weight = newWeight;
        int parentIndex = (current - 1) / 2;
        if (oldWeight > newWeight) {
            sink(current);
        } else {
            bubble(parentIndex, current);
        }       
    }

    private void updatePositionMap(T data1, T data2, int pos1, int pos2) {
        nodePosition.remove(data1);
        nodePosition.remove(data2);
        nodePosition.put(data1, pos1);
        nodePosition.put(data2, pos2);
    }

    public T extractMap() {
        int size = allNodes.size() - 1;
        T max = allNodes.get(0).key;
        int lastNodeWeight = allNodes.get(size).weight;
        allNodes.get(0).weight = lastNodeWeight;
        allNodes.get(0).key = allNodes.get(size).key;
        allNodes.remove(size);

        int currentIndex = 0;
        size--;
        while (true) {
            int left = 2 * currentIndex + 1;
            int right = 2 * currentIndex + 2;
            if (left > size) {
                break;
            }
            if (right > size) {
                right = left;
            }
            int largerIndex = allNodes.get(left).weight >= allNodes.get(right).weight ? left : right;
            if (allNodes.get(currentIndex).weight < allNodes.get(largerIndex).weight) {
                swap(allNodes.get(currentIndex), allNodes.get(largerIndex));
                currentIndex = largerIndex;
            } else {
                break;
            }
        }
        return max;
    }

    public void printHeap() {
        for (Node n : allNodes) {
            System.out.println(n.weight + " " + n.key);
        }
    }

}

