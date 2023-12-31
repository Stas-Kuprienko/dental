package edu.dental.beans;

import java.util.*;

public class ProductMap {

    private List<Item> items;

    public ProductMap(List<Item> items) {
        this.items = new ArrayList<>(items);
    }
    public ProductMap() {
        this.items = new ArrayList<>();
    }


    public String[] getKeys() {
        String[] result = new String[items.size()];
        return items.stream().map(e -> e.key).toList().toArray(result);
    }

    public boolean add(Item item) {
        return this.items.add(item);
    }

    public void update(String key, int value) {
        Item item = items.stream().filter(e -> e.key.equals(key)).findAny().orElse(null);
        if (item != null) {
            int id = item.id();
            items.remove(item);
            items.add(new Item(id, key, value));
        }
    }

    public void remove(String key) {
        items.stream().filter(e -> e.key.equals(key)).findAny().ifPresent(items::remove);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Item[] getItems() {
        Item[] result = new Item[items.size()];
        return items.toArray(result);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductMap that = (ProductMap) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return "ProductMap{" +
                "items=" + items +
                '}';
    }

    public record Item(int id, String key, int value) {

        @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Item itemDTO = (Item) o;
                return id == itemDTO.id && value == itemDTO.value && Objects.equals(key, itemDTO.key);
            }

        @Override
            public String toString() {
                return "Item{" +
                        "id=" + id +
                        ", key='" + key + '\'' +
                        ", value=" + value +
                        '}';
            }
        }
}
