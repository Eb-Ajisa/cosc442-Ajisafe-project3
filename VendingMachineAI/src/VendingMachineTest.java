import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Compact JUnit 5 coverage of VendingMachine's public/protected behavior.
 * Arrange: create/stock a machine; Act: invoke a method; Assert: check outcomes/state.
 *
 * Known production defects deliberately exposed by these tests:
 * 1. Constructor uses i <= NUM_SLOTS (must be i < NUM_SLOTS).
 * 2. insertMoney rejects values < 1, although its contract permits values >= 0.
 * Fix those production issues for all tests to pass; don't weaken the assertions.
 */
class VendingMachineTest {
    private VendingMachine machine;
    private VendingMachineItem water;

    @BeforeEach
    void setUp() {
        machine = new VendingMachine();
        water = new VendingMachineItem("Water", 2);
    }

    @Test
    void newMachineHasFourEmptySlotsAndNoMoney() {
        // Arrange: setUp; Act / Assert
        assertEquals(4, VendingMachine.NUM_SLOTS);
        String[] codes = {VendingMachine.A_CODE, VendingMachine.B_CODE,
                VendingMachine.C_CODE, VendingMachine.D_CODE};
        assertArrayEquals(new String[] {"A", "B", "C", "D"}, codes);
        for (String code : codes) assertNull(machine.getItem(code));
        assertEquals(0, machine.getBalance());
        assertEquals(0, machine.returnChange());
    }

    @Test
    void stocksEachSlotIndependentlyAndAllowsRemovalAndReplacement() {
        // Arrange
        String[] codes = {"A", "B", "C", "D"};
        VendingMachineItem[] items = {
                water, new VendingMachineItem("B", 1),
                new VendingMachineItem("C", 3), new VendingMachineItem("D", 4)};
        // Act / Assert
        for (int i = 0; i < codes.length; i++) machine.addItem(items[i], codes[i]);
        for (int i = 0; i < codes.length; i++) assertSame(items[i], machine.getItem(codes[i]));
        assertSame(items[2], machine.removeItem("C"));
        assertNull(machine.getItem("C"));
        VendingMachineItem replacement = new VendingMachineItem("Replacement", 5);
        machine.addItem(replacement, "C");
        assertSame(replacement, machine.getItem("C"));
        assertSame(items[0], machine.getItem("A"));
    }

    @Test
    void occupiedAndEmptySlotOperationsThrowWithoutDamagingStock() {
        // Arrange
        machine.addItem(water, "B");
        // Act / Assert
        assertEquals("Slot B already occupied", assertThrows(VendingMachineException.class,
                () -> machine.addItem(new VendingMachineItem("Other", 3), "B")).getMessage());
        assertSame(water, machine.getItem("B"));
        assertEquals("Slot D is empty -- cannot remove item",
                assertThrows(VendingMachineException.class, () -> machine.removeItem("D")).getMessage());
        assertNull(machine.getItem("D"));
    }

    @Test
    void invalidCodesAreRejectedAcrossAllSlotOperations() {
        // Arrange / Act / Assert
        for (String code : new String[] {"", "E", "a", "AA", " A ", "1"}) {
            assertEquals("Invalid code for vending machine item",
                    assertThrows(VendingMachineException.class,
                            () -> machine.addItem(water, code)).getMessage());
            assertThrows(VendingMachineException.class, () -> machine.getItem(code));
            assertThrows(VendingMachineException.class, () -> machine.removeItem(code));
            assertThrows(VendingMachineException.class, () -> machine.makePurchase(code));
        }
        // Documents current null behavior (rather than guessing a different contract).
        assertThrows(NullPointerException.class, () -> machine.getItem(null));
    }

    @Test
    void moneyAccumulatesAndRefundResetsBalanceWithoutTouchingItems() {
        // Arrange
        machine.addItem(water, "A");
        // Act
        machine.insertMoney(1.25);
        machine.insertMoney(3.75);
        // Assert
        assertEquals(5, machine.getBalance(), 1e-9);
        assertEquals(5, machine.returnChange(), 1e-9);
        assertEquals(0, machine.getBalance());
        assertEquals(0, machine.returnChange());
        assertSame(water, machine.getItem("A"));
    }

    @Test
    void negativeMoneyIsRejectedAndNonnegativeFractionsAreAllowed() {
        // Arrange / Act / Assert
        machine.insertMoney(2);
        assertEquals("Invalid amount.  Amount must be >= 0",
                assertThrows(VendingMachineException.class, () -> machine.insertMoney(-0.01)).getMessage());
        assertEquals(2, machine.getBalance());
        // Contract regression: current implementation rejects both of these.
        machine.insertMoney(0);
        machine.insertMoney(0.50);
        assertEquals(2.50, machine.getBalance(), 1e-9);
    }

    @Test
    void purchaseWithExactOrExtraMoneyDebitsBalanceAndEmptiesOnlyBoughtSlot() {
        // Arrange
        VendingMachineItem snack = new VendingMachineItem("Snack", 3);
        machine.addItem(water, "A");
        machine.addItem(snack, "B");
        machine.insertMoney(2);
        // Act / Assert: exact money
        assertTrue(machine.makePurchase("A"));
        assertEquals(0, machine.getBalance());
        assertNull(machine.getItem("A"));
        assertSame(snack, machine.getItem("B"));
        // Act / Assert: extra money; same item cannot be purchased twice
        machine.insertMoney(5);
        assertTrue(machine.makePurchase("B"));
        assertEquals(2, machine.getBalance());
        assertFalse(machine.makePurchase("B"));
        assertEquals(2, machine.returnChange());
        assertEquals(0, machine.getBalance());
    }

    @Test
    void insufficientFundsAndEmptySlotsFailWithoutChangingMoneyOrItems() {
        // Arrange
        machine.addItem(water, "D");
        machine.insertMoney(1);
        // Act / Assert
        assertFalse(machine.makePurchase("D"));
        assertFalse(machine.makePurchase("C"));
        assertSame(water, machine.getItem("D"));
        assertEquals(1, machine.getBalance());
    }

    @Test
    void freeItemCanBePurchasedWithZeroBalance() {
        // Arrange
        machine.addItem(new VendingMachineItem("Free", 0), "A");
        // Act / Assert
        assertTrue(machine.makePurchase("A"));
        assertNull(machine.getItem("A"));
        assertEquals(0, machine.getBalance());
    }
}
