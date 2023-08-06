package com.sadapay.assignment;

import com.sadapay.assignment.exceptions.InvalidCommandProvided;
import com.sadapay.assignment.exceptions.ItemNotFoundInInventory;
import com.sadapay.assignment.util.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Supermarket {
    private static final Map<String, Item> inventory = new HashMap<>(); // using this as the store for inventory
    private static final Cart cart = new Cart(); // simulates a shopping cart

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("Usage: <inventory_file> <commands_file>");
        }
        loadInventory(args[0]); // this loads the inventory from the file.
        if (args.length == 1) {
            runInteractiveMode();
        } else {
            runFileMode(args[1]);
        }
    }


    private static void runFileMode(String commandsFilePath) {
        try {
            List<String> commands = Files.readAllLines(Paths.get(commandsFilePath));
            for (String command : commands) {
                executeCommand(command);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading commands file: " + e.getMessage());
        }
    }

    private static void loadInventory(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                inventory.put(parts[0], new Item(parts[0], new BigDecimal(parts[1]), Integer.parseInt(parts[2])));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading inventory from file: " + e.getMessage());
        }
    }

    private static void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            executeCommand(scanner.nextLine());
        }
    }

    private static void executeCommand(String commandLine) {
        String[] parts = commandLine.trim().split("\\s+");

        switch (parts[0]) {
            case "add":
                if (parts.length < 3) {
                    System.out.println("Usage: add <item_name> <quantity>");
                    throw new InvalidCommandProvided(commandLine);
                }
                String name = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                Item item = inventory.get(name);
                if (item == null) {
                    System.out.println("Item not found: " + name);
                    throw new ItemNotFoundInInventory(commandLine);
                }
                if (item.getQuantity() < quantity) {
                    System.out.println("Not enough stock for " + name + ". Available: " + item.getQuantity());
                    break;
                }
                cart.addItem(item, quantity);
                calculateCart();
                System.out.println("added " + name + " " + quantity);
                break;
            case "checkout":
                checkout();
                break;
            case "bill":
                printBill();
                break;
            case "offer":
                if (parts.length < 3) {
                    System.out.println("Usage: offer <offer_name> <item_name>");
                    throw new InvalidCommandProvided(commandLine);
                }
                String offerName = parts[1];
                String itemName = parts[2];
                Item itemFromInventory = inventory.get(itemName);
                if (itemFromInventory == null || itemFromInventory.getQuantity() == 0) {
                    System.out.println("Item not found: " + itemFromInventory);
                    throw new ItemNotFoundInInventory(commandLine);
                }
                Offer offer = createOffer(offerName);
                if (offer != null) {
                    cart.addOffer(itemFromInventory, offer);
                    calculateCart();
                    System.out.println("offer added");
                }
                break;
            default:
                System.out.println("Invalid command: " + parts[0]);
                break;
        }
    }

    private static Offer createOffer(String offerType) {
        switch (offerType) {
            case "buy_2_get_1_free":
                return new Buy2Get1FreeOffer();
            case "buy_1_get_half_off":
                return new Buy1GetHalfOffOffer();
            default:
                System.out.println("Invalid offer type: " + offerType);
                return null;
        }
    }

    private static void checkout() {
        if (cart.getItems().isEmpty()) {
            System.out.println("empty cart");
            return;
        }
        // update inventory to remove the items
        for (CartItem cartItem : cart.getItems().values()) {
            Item itemInStock = inventory.get(cartItem.getItem().getName());
            itemInStock.setQuantity(itemInStock.getQuantity() - cartItem.getQuantity());
        }
        System.out.println("done");
        cleanUp();
    }

    private static void printBill() {
        System.out.println("subtotal:" + cart.getSubTotal() + ", discount:" + cart.getDiscount() + ", total:" + cart.getTotal().setScale(Constants.SCALE, Constants.ROUNDING_MODE));
    }

    private static void calculateCart() {
        BigDecimal subtotal = Constants.ZERO;
        BigDecimal totalDiscount = Constants.ZERO;

        for (CartItem cartItem : cart.getItems().values()) {
            Item item = cartItem.getItem();
            BigDecimal itemTotal = item.getPrice().multiply(new BigDecimal(cartItem.getQuantity())).setScale(Constants.SCALE, Constants.ROUNDING_MODE);

            // apply offer if available
            Offer offer = cartItem.getOffer();
            if (offer != null) {
                totalDiscount = totalDiscount.add(offer.apply(item, cartItem.getQuantity()));
            }
            subtotal = subtotal.add(itemTotal);
        }
        cart.setSubTotal(subtotal);
        cart.setDiscount(totalDiscount);
        cart.setTotal(subtotal.subtract(totalDiscount));
    }

    private static void cleanUp() {
        inventory.clear();
        cart.clearCart();
    }
}