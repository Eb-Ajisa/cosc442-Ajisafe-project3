import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class VendingMachineItemTest {
    
    @Test
    void testGetName() {
    VendingMachineItem item = new VendingMachineItem("Chips", 1.50);

    assertEquals("Chips", item.getName());



    }

    @Test 
    void testGetPrice() {
        VendingMachineItem item = new VendingMachineItem("Soda", 2.00);
        assertEquals(2.00, item.getPrice(), 0.001);
    }

    @Test 
    void testConstructorWithNegativePrice() {
        try {
            VendingMachineItem item = new VendingMachineItem("Candy", -1.00);
        } catch (VendingMachineException e) {
            assertEquals("Price cannot be less than zero", e.getMessage());
        }
    }
}
