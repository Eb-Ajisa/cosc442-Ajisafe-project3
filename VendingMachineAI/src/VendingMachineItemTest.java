import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 tests for VendingMachineItem.
 * Each test uses Arrange / Act / Assert (combined where a single assertion is clearer).
 */
class VendingMachineItemTest {

    @Test
    @DisplayName("Constructor stores a normal name and price")
    void constructorStoresNameAndPrice() {
        // Arrange / Act
        VendingMachineItem item = new VendingMachineItem("Water", 1.75);
        // Assert
        assertAll("item properties",
                () -> assertEquals("Water", item.getName()),
                () -> assertEquals(1.75, item.getPrice(), 0.000001));
    }

    @Test
    void zeroPriceIsAllowed() {
        VendingMachineItem item = new VendingMachineItem("Free sample", 0.0);
        assertEquals(0.0, item.getPrice());
    }

    @Test
    void negativePriceThrowsExpectedException() {
        VendingMachineException error = assertThrows(VendingMachineException.class,
                () -> new VendingMachineItem("Water", -0.01));
        assertEquals("Price cannot be less than zero", error.getMessage());
    }

    @Test
    void veryNegativePriceAlsoThrows() {
        assertThrows(VendingMachineException.class,
                () -> new VendingMachineItem("Water", -Double.MAX_VALUE));
    }

    @Test
    void emptyNameIsPreserved() {
        VendingMachineItem item = new VendingMachineItem("", 2.0);
        assertEquals("", item.getName());
    }

    @Test
    void nullNameIsPreserved() {
        VendingMachineItem item = new VendingMachineItem(null, 2.0);
        assertNull(item.getName());
    }

    @Test
    void fractionalPriceIsPreserved() {
        VendingMachineItem item = new VendingMachineItem("Snack", 0.01);
        assertEquals(0.01, item.getPrice(), 0.000001);
    }

    @Test
    void largePositivePriceIsPreserved() {
        VendingMachineItem item = new VendingMachineItem("Expensive", Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, item.getPrice());
    }

    @Test
    void negativeInfinityIsRejected() {
        assertThrows(VendingMachineException.class,
                () -> new VendingMachineItem("Bad", Double.NEGATIVE_INFINITY));
    }

    @Test
    void positiveInfinityIsAcceptedByCurrentContract() {
        VendingMachineItem item = new VendingMachineItem("Unlimited", Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, item.getPrice());
    }

    @Test
    void nanPriceIsAcceptedByCurrentImplementation() {
        // Documents an edge case of the existing price < 0 validation.
        VendingMachineItem item = new VendingMachineItem("Unknown", Double.NaN);
        assertTrue(Double.isNaN(item.getPrice()));
    }
}
