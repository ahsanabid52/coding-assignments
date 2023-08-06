package com.sadapay.assignment;

import com.sadapay.assignment.exceptions.InvalidCommandProvided;
import com.sadapay.assignment.exceptions.ItemNotFoundInInventory;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;

public class SupermarketTest {

    @Test
    public void shouldGetAnExceptionWhenCallingWithoutArguments() {
        String[] args = {};
        assertThrows(RuntimeException.class, () -> Supermarket.main(args));
    }

    @Test
    public void shouldThrowAnExceptionForAnInvalidFile() {
        String[] args = {"sample.csv"};
        assertThrows(RuntimeException.class, () -> Supermarket.main(args));
    }

    @Test
    public void shouldThrowAnExceptionForAnInvalidCommandFile() {
        String[] args = {"sample.csv", "someFile.csv"};
        assertThrows(RuntimeException.class, () -> Supermarket.main(args));
    }

    @Test
    public void shouldBeAbleToLoadAValidFileAndExecuteCommands() {
        String[] args = {"src/main/resources/inventory.csv", "src/main/resources/file_input.txt"};
        Supermarket.main(args);
    }

    @Test
    public void shouldThrowItemNotFoundExceptionInCaseAnInvalidItemIsProvided() {
        String[] args = {"src/main/resources/inventory.csv", "src/main/resources/file_input_invalid_product.txt"};
        assertThrows(ItemNotFoundInInventory.class, () -> Supermarket.main(args));
    }

    @Test
    public void shouldThrowInvalidCommandExceptionInCaseAnInvalidCommandIsProvided() {
        String[] args = {"src/main/resources/inventory.csv", "src/main/resources/file_input_invalid_command.txt"};
        assertThrows(InvalidCommandProvided.class, () -> Supermarket.main(args));
    }

    @Test
    public void shouldSkipInvalidOfferIfProvided() {
        String[] args = {"src/main/resources/inventory.csv", "src/main/resources/file_input_invalid_offer.txt"};
        Supermarket.main(args);
    }

    @Test
    public void shouldHandleWhenInventoryIsEmpty() {
        String[] args = {"src/main/resources/inventory-empty.csv", "src/main/resources/file_input.txt"};
        assertThrows(ItemNotFoundInInventory.class, () -> Supermarket.main(args));
    }

    @Test
    public void shouldApplyTheMoreRecentOfferOnTheSameProduct() {
        String[] args = {"src/main/resources/inventory.csv", "src/main/resources/file_input_multiple_offers.txt"};
        Supermarket.main(args);
    }
}