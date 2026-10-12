package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.awt.Color;

public class InventoryTest {

    // Inventory checks object identity, not Item properties.
    private Item newItem() {
        return new Item('!', Color.WHITE, "Test item", "Test item");
    }

    @Test
    void testAdd() {
        // Arrange
        Inventory inventory = new Inventory(2);
        Item first = newItem();
        Item second = newItem();
        Item extra = newItem();

        // Act & Assert: fill consecutive slots.
        inventory.add(first);
        inventory.add(second);
        assertSame(first, inventory.get(0));
        assertSame(second, inventory.get(1));

        // Adding to a full inventory must not overwrite existing items.
        inventory.add(extra);
        assertSame(first, inventory.get(0));
        assertSame(second, inventory.get(1));

        // A freed earlier slot should be reused before later occupied slots.
        inventory.remove(first);
        inventory.add(extra);
        assertSame(extra, inventory.get(0));
        assertSame(second, inventory.get(1));
    }

    @Test
    void testContains() {
        Inventory inventory = new Inventory(2);
        Item stored = newItem();
        Item absent = newItem();

        assertFalse(inventory.contains(stored));
        inventory.add(stored);
        assertTrue(inventory.contains(stored));
        assertFalse(inventory.contains(absent));
        inventory.remove(stored);
        assertFalse(inventory.contains(stored));
    }

    @Test
    void testGet() {
        Inventory inventory = new Inventory(2);
        Item item = newItem();

        assertNull(inventory.get(1));
        inventory.add(item);
        assertSame(item, inventory.get(0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> inventory.get(2));
    }

    @Test
    void testGetItems() {
        Inventory inventory = new Inventory(2);
        Item item = newItem();
        Item[] backingArray = inventory.getItems();

        assertEquals(2, backingArray.length);
        inventory.add(item);
        assertSame(backingArray, inventory.getItems());
        assertSame(item, backingArray[0]);
    }

    @Test
    void testIsFull() {
        Inventory inventory = new Inventory(2);
        Item first = newItem();
        Item second = newItem();

        assertFalse(inventory.isFull());
        inventory.add(first);
        assertFalse(inventory.isFull());
        inventory.add(second);
        assertTrue(inventory.isFull());
        inventory.remove(first);
        assertFalse(inventory.isFull());
    }

    @Test
    void testRemove() {
        Inventory inventory = new Inventory(3);
        Item first = newItem();
        Item middle = newItem();
        Item last = newItem();
        Item absent = newItem();
        inventory.add(first);
        inventory.add(middle);
        inventory.add(last);

        inventory.remove(middle);
        assertNull(inventory.get(1));
        assertSame(first, inventory.get(0));
        assertSame(last, inventory.get(2));

        // An absent item must not change the inventory.
        inventory.remove(absent);
        assertSame(first, inventory.get(0));
        assertNull(inventory.get(1));
        assertSame(last, inventory.get(2));
    }
}
