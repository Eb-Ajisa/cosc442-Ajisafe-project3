import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class VendingMachineTest {

    public VendingMachine vendingMachine;
    public VendingMachineItem vendingMachineItem1, vendingMachineItem2, vendingMachineItem3, vendingMachineItem4, vendingMachineItem5, vendingMachineItem6;

    @BeforeEach 
    void setUp() {
        vendingMachine = new VendingMachine();
        vendingMachineItem1 = new VendingMachineItem("chips", 1 );
        vendingMachineItem2 = new VendingMachineItem("eggs",2 );
        vendingMachineItem3 = new VendingMachineItem("candy", 3 );
        vendingMachineItem4 = new VendingMachineItem("apples", 4 );
        vendingMachineItem5 = new VendingMachineItem("potatoes", 5 );
        vendingMachineItem6 = new VendingMachineItem("water", 6 );



    }


    @AfterEach 
    void tearDown() {
        vendingMachine = null;
        vendingMachineItem1 = null;
        vendingMachineItem2 = null;
        vendingMachineItem3 = null;
        vendingMachineItem4 = null;
        vendingMachineItem5 = null;
        vendingMachineItem6 = null;
    }




    @Test
    void testAddItem(){
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.addItem(vendingMachineItem2, "B");
        vendingMachine.addItem(vendingMachineItem3, "C");

        assertEquals(vendingMachineItem1, vendingMachine.getItem("A"));
        assertEquals(vendingMachineItem2, vendingMachine.getItem("B"));
        assertEquals(vendingMachineItem3, vendingMachine.getItem("C"));

    }

    @Test 
    void OccupiedSlotErrorAddItem(){
        vendingMachine.addItem(vendingMachineItem1, "A");
        assertThrows(VendingMachineException.class, () -> {
            vendingMachine.addItem(vendingMachineItem2, "A");
        });

    }

    @Test
    void InvalidCodeErrorAddItem(){
        assertThrows(VendingMachineException.class, () -> {
            vendingMachine.addItem(vendingMachineItem1, "E");
        });
    }

    @Test
    void testGetBalance() {
        vendingMachine.insertMoney(5.0);

        assertEquals(5.0, vendingMachine.getBalance(), 0.01);
    }

    @Test
    void testGetItem() {
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.addItem(vendingMachineItem2, "B");
        vendingMachine.addItem(vendingMachineItem3, "C");
        vendingMachine.addItem(vendingMachineItem4, "D");

        assertEquals(vendingMachineItem1, vendingMachine.getItem("A"));
        assertEquals(vendingMachineItem2, vendingMachine.getItem("B"));
        assertEquals(vendingMachineItem3, vendingMachine.getItem("C"));
        assertEquals(vendingMachineItem4, vendingMachine.getItem("D"));

    }

    @Test 
    void InvalidCodeErrorGetItem() {
        assertThrows(VendingMachineException.class, () -> {
            vendingMachine.getItem("E");
        });
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.01, 1.0, 100.0, 0.0, 0.0001, 100000})
    void testInsertMoney(double doubles) {
        vendingMachine.insertMoney(doubles);

        assertEquals(doubles, vendingMachine.getBalance(), 0.01);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, -1.0, -100.0})
    void InvalidMoneyErrorInsertMoney(double doubles) {
        assertThrows(VendingMachineException.class, () -> {
            vendingMachine.insertMoney(doubles);
        });
    }

    @Test
    void testMakePurchase() {
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.addItem(vendingMachineItem2, "B");
        vendingMachine.insertMoney(5.0);

        vendingMachine.makePurchase("A");

        assertEquals(null, vendingMachine.getItem("A"));
        assertEquals(4.0, vendingMachine.getBalance(), 0.01);

    }

    @Test 
    void EmptySlotErrorMakePurchase() {
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.addItem(vendingMachineItem2, "B");
        vendingMachine.insertMoney(5.0);

        vendingMachine.makePurchase("A");


        assertFalse(vendingMachine.makePurchase("A"));
    }

    @Test 
    void InsufficientFundsErrorMakePurchase() {
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.addItem(vendingMachineItem6, "D");
        vendingMachine.insertMoney(5);
        
        assertTrue(vendingMachine.makePurchase("A"));
        assertFalse(vendingMachine.makePurchase("D"));
        assertEquals(4, vendingMachine.getBalance(), 0.01);
    }

    @Test
    void testRemoveItem() {
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.removeItem("A");

        assertEquals(null, vendingMachine.getItem("A"));

    }

    @Test 
    void EmptySlotErrorRemoveItem() {
        vendingMachine.addItem(vendingMachineItem1, "A");
        vendingMachine.removeItem("A");

        assertThrows(VendingMachineException.class, () -> {
            vendingMachine.removeItem("A");
        });
    }

    @Test 
    void InvalidCodeErrorRemoveItem() {
        assertThrows(VendingMachineException.class, () -> {
            vendingMachine.removeItem("E");
        });
    }

    @Test
    void testReturnChange() {
        vendingMachine.insertMoney(5.0);

        assertEquals(5.0, vendingMachine.returnChange(), 0.01);
        assertEquals(0.0, vendingMachine.getBalance(), 0.01);

    }

}
