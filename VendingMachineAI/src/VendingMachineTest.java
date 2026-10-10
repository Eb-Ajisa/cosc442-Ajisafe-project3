import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 behavior tests for VendingMachine only.
 * Arrange: @BeforeEach supplies a fresh machine; Act: invoke a method;
 * Assert: verify its return value, changed state and/or thrown exception.
 *
 * IMPORTANT: VendingMachine's original constructor accesses itemArray[4]
 * although the array only has indices 0..3. Until the loop condition is fixed
 * from i <= NUM_SLOTS to i < NUM_SLOTS, tests using @BeforeEach will fail.
 */
class VendingMachineTest {
    private VendingMachine machine;
    private VendingMachineItem water;

    @BeforeEach
    void setUp() {
        machine = new VendingMachine();
        water = new VendingMachineItem("Water", 2.00);
    }

    @Test
    @DisplayName("New machine has four slots, correct codes and zero credit")
    void newMachineStartsEmpty() {
        assertAll(
                () -> assertEquals(4, VendingMachine.NUM_SLOTS),
                () -> assertEquals("A", VendingMachine.A_CODE),
                () -> assertEquals("B", VendingMachine.B_CODE),
                () -> assertEquals("C", VendingMachine.C_CODE),
                () -> assertEquals("D", VendingMachine.D_CODE),
                () -> assertEquals(0.0, machine.getBalance()),
                () -> assertNull(machine.getItem("A")),
                () -> assertNull(machine.getItem("B")),
                () -> assertNull(machine.getItem("C")),
                () -> assertNull(machine.getItem("D")));
    }

    @Test
    void addsItemsToAllFourSlotsWithoutMixingThemUp() {
        VendingMachineItem b = new VendingMachineItem("B", 1);
        VendingMachineItem c = new VendingMachineItem("C", 2);
        VendingMachineItem d = new VendingMachineItem("D", 3);
        machine.addItem(water, "A");
        machine.addItem(b, "B");
        machine.addItem(c, "C");
        machine.addItem(d, "D");
        assertAll(
                () -> assertSame(water, machine.getItem("A")),
                () -> assertSame(b, machine.getItem("B")),
                () -> assertSame(c, machine.getItem("C")),
                () -> assertSame(d, machine.getItem("D")));
    }

    @Test
    void addingToOccupiedSlotThrowsAndPreservesFirstItem() {
        machine.addItem(water, "B");
        VendingMachineException error = assertThrows(VendingMachineException.class,
                () -> machine.addItem(new VendingMachineItem("Other", 4), "B"));
        assertAll(
                () -> assertEquals("Slot B already occupied", error.getMessage()),
                () -> assertSame(water, machine.getItem("B")));
    }

    @Test
    void removeReturnsExactItemAndEmptiesSlot() {
        machine.addItem(water, "C");
        VendingMachineItem removed = machine.removeItem("C");
        assertAll(() -> assertSame(water, removed),
                () -> assertNull(machine.getItem("C")));
    }

    @Test
    void removingEmptySlotThrowsInformativeException() {
        VendingMachineException error = assertThrows(VendingMachineException.class,
                () -> machine.removeItem("D"));
        assertAll(() -> assertEquals("Slot D is empty -- cannot remove item", error.getMessage()),
                () -> assertNull(machine.getItem("D")));
    }

    @Test
    void removedSlotCanBeRefilled() {
        machine.addItem(water, "A");
        machine.removeItem("A");
        VendingMachineItem replacement = new VendingMachineItem("Juice", 3);
        machine.addItem(replacement, "A");
        assertSame(replacement, machine.getItem("A"));
    }

    @Test
    void badCodesAreRejectedByAllSlotOperations() {
        String[] badCodes = {"", "E", "a", "AA", " A ", "1"};
        for (String code : badCodes) {
            assertAll("invalid slot: " + code,
                    () -> assertEquals("Invalid code for vending machine item",
                            assertThrows(VendingMachineException.class,
                                    () -> machine.addItem(water, code)).getMessage()),
                    () -> assertThrows(VendingMachineException.class, () -> machine.getItem(code)),
                    () -> assertThrows(VendingMachineException.class, () -> machine.removeItem(code)),
                    () -> assertThrows(VendingMachineException.class, () -> machine.makePurchase(code)));
        }
    }

    @Test
    void nullSlotCodeThrowsNullPointerExceptionInCurrentImplementation() {
        assertAll(
                () -> assertThrows(NullPointerException.class, () -> machine.getItem(null)),
                () -> assertThrows(NullPointerException.class, () -> machine.addItem(water, null)),
                () -> assertThrows(NullPointerException.class, () -> machine.removeItem(null)),
                () -> assertThrows(NullPointerException.class, () -> machine.makePurchase(null)));
    }

    @Test
    void insertingMoneyIncreasesBalanceAndAccumulates() {
        machine.insertMoney(1.25);
        machine.insertMoney(3.75);
        assertEquals(5.00, machine.getBalance(), 0.000001);
    }

    @Test
    void insertingNegativeMoneyThrowsWithoutChangingBalance() {
        machine.insertMoney(2);
        VendingMachineException error = assertThrows(VendingMachineException.class,
                () -> machine.insertMoney(-1));
        assertAll(() -> assertEquals("Invalid amount.  Amount must be >= 0", error.getMessage()),
                () -> assertEquals(2.0, machine.getBalance()));
    }

    @Test
    void insertingZeroIsAllowedByDocumentedPrecondition() {
        // Regression test: documentation says amount >= 0; source currently rejects < 1.
        machine.insertMoney(0);
        assertEquals(0, machine.getBalance());
    }

    @Test
    void insertingPositiveFractionUnderOneIsAllowedByDocumentedPrecondition() {
        // Regression test for the same < 1 vs < 0 implementation mismatch.
        machine.insertMoney(0.50);
        assertEquals(0.50, machine.getBalance(), 0.000001);
    }

    @Test
    void purchaseWithExactMoneySucceedsAndEmptiesSlot() {
        machine.addItem(water, "A");
        machine.insertMoney(2);
        boolean purchased = machine.makePurchase("A");
        assertAll(() -> assertTrue(purchased),
                () -> assertEquals(0, machine.getBalance()),
                () -> assertNull(machine.getItem("A")));
    }

    @Test
    void purchaseWithExtraMoneyLeavesCorrectChange() {
        machine.addItem(water, "D");
        machine.insertMoney(5);
        assertTrue(machine.makePurchase("D"));
        assertEquals(3, machine.getBalance());
    }

    @Test
    void purchaseWithoutEnoughMoneyFailsAndKeepsItemAndBalance() {
        machine.addItem(water, "B");
        machine.insertMoney(1);
        boolean purchased = machine.makePurchase("B");
        assertAll(() -> assertFalse(purchased),
                () -> assertSame(water, machine.getItem("B")),
                () -> assertEquals(1, machine.getBalance()));
    }

    @Test
    void purchaseOfEmptySlotFailsWithoutChangingBalance() {
        machine.insertMoney(5);
        assertFalse(machine.makePurchase("C"));
        assertEquals(5, machine.getBalance());
    }

    @Test
    void purchaseOfFreeItemSucceedsWithZeroBalance() {
        VendingMachineItem free = new VendingMachineItem("Free", 0);
        machine.addItem(free, "A");
        assertTrue(machine.makePurchase("A"));
        assertAll(() -> assertNull(machine.getItem("A")),
                () -> assertEquals(0, machine.getBalance()));
    }

    @Test
    void cannotBuySameItemTwice() {
        machine.addItem(water, "A");
        machine.insertMoney(5);
        assertTrue(machine.makePurchase("A"));
        assertFalse(machine.makePurchase("A"));
        assertEquals(3, machine.getBalance());
    }

    @Test
    void multiplePurchasesOnlyAffectPurchasedSlots() {
        VendingMachineItem snack = new VendingMachineItem("Snack", 3);
        machine.addItem(water, "A");
        machine.addItem(snack, "B");
        machine.insertMoney(4);
        assertTrue(machine.makePurchase("A"));
        assertFalse(machine.makePurchase("B"));
        assertAll(() -> assertNull(machine.getItem("A")),
                () -> assertSame(snack, machine.getItem("B")),
                () -> assertEquals(2, machine.getBalance()));
    }

    @Test
    void returningChangePaysFullBalanceAndResetsToZero() {
        machine.insertMoney(4.50);
        double change = machine.returnChange();
        assertAll(() -> assertEquals(4.50, change, 0.000001),
                () -> assertEquals(0, machine.getBalance()));
    }

    @Test
    void returningChangeTwiceDoesNotDuplicateMoney() {
        machine.insertMoney(2);
        assertEquals(2, machine.returnChange());
        assertEquals(0, machine.returnChange());
    }

    @Test
    void returningChangeOnNewMachineReturnsZero() {
        assertEquals(0, machine.returnChange());
        assertEquals(0, machine.getBalance());
    }

    @Test
    void refundDoesNotRemoveStock() {
        machine.addItem(water, "C");
        machine.insertMoney(3);
        machine.returnChange();
        assertSame(water, machine.getItem("C"));
    }

    @Test
    void balanceCanBeReusedAfterPurchaseAndRefund() {
        machine.addItem(water, "A");
        machine.insertMoney(5);
        assertTrue(machine.makePurchase("A"));
        assertEquals(3, machine.returnChange());
        machine.insertMoney(2);
        assertEquals(2, machine.getBalance());
    }
}
