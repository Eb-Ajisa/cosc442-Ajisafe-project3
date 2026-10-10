import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/** Focused JUnit 5 tests for VendingMachineItem. Arrange / Act / Assert (AAA). */
class VendingMachineItemTest {

    @Test
    void storesNameAndPrice() {
        // Arrange / Act
        VendingMachineItem item = new VendingMachineItem("Water", 1.75);
        // Assert
        assertAll(() -> assertEquals("Water", item.getName()),
                  () -> assertEquals(1.75, item.getPrice()));
    }

    @Test
    void acceptsBoundaryPricesAndNames() {
        // Arrange / Act
        VendingMachineItem free = new VendingMachineItem("", 0);
        VendingMachineItem unnamed = new VendingMachineItem(null, 0.01);
        // Assert
        assertAll(() -> assertEquals("", free.getName()),
                  () -> assertEquals(0, free.getPrice()),
                  () -> assertNull(unnamed.getName()),
                  () -> assertEquals(0.01, unnamed.getPrice()));
    }

    @Test
    void rejectsNegativePrices() {
        // Arrange / Act / Assert
        for (double price : new double[] {-0.01, -100, Double.NEGATIVE_INFINITY}) {
            VendingMachineException error = assertThrows(VendingMachineException.class,
                    () -> new VendingMachineItem("Bad", price));
            assertEquals("Price cannot be less than zero", error.getMessage());
        }
    }

    @Test
    void preservesLargeAndSpecialPricesAsCurrentlyImplemented() {
        // These values are not rejected by the class's `price < 0` check.
        assertEquals(Double.MAX_VALUE, new VendingMachineItem("Large", Double.MAX_VALUE).getPrice());
        assertEquals(Double.POSITIVE_INFINITY,
                new VendingMachineItem("Infinity", Double.POSITIVE_INFINITY).getPrice());
        assertTrue(Double.isNaN(new VendingMachineItem("NaN", Double.NaN).getPrice()));
    }
}
