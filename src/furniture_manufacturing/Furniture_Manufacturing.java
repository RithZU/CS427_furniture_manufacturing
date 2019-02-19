/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package furniture_manufacturing;

import java.util.Timer;
import java.util.TimerTask;

public class Furniture_Manufacturing {

    public static boolean isRun = false;
    
    // constants
    private final int CHAIR = 1;
    private final int TABLE = 2;

    /**
     * Warehouse
     *
     */
    public static int STOCK_WOOD = 100;
    public static int STOCK_STEEL = 100;
    public static int STOCK_NAIL = 100;

    public static Furniture[] myFurnitures;
    public static Order[] myOrders;
    public static Warehouse myWarehouse;

    public static Inventory myInventory;

    /**
     * index [0: wood , 1: steel, 2: nail]
     */
    public static int[] chairMaterials = { 3, 5, 10 };
    public static int[] tableMaterials = { 5, 3, 15 };
    public static int[] itemDuration = {3, 5};

    public static int simulation_runtime = 0;
    public static int[] intervalOrderTime = { 0, 10 };
    public static int numOfOrder = 50;

    public static void main(String[] args) {
        myOrders = generateOrder(numOfOrder);
        myWarehouse = new Warehouse(STOCK_WOOD, STOCK_STEEL, STOCK_NAIL);
        myInventory = new Inventory(0, 0);

        /*int order_index = 0;
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                addResources();
            }
        }, 0, 10000); // add resources every 10 seconds

        while (order_index <= myOrders.length - 1) {
            Order curr_order = myOrders[order_index];
            if (checkResources(curr_order.furnitures)) {
                buildFurniture(curr_order.furnitures[0].type);
                myInventory.store(curr_order.furnitures[0].type, 1);
                order_index++;
            } else {
                try {
                    Thread.sleep(2000); // pause for 10 seconds
                    System.out.println("check again");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        timer.cancel();
        */
        /**
         * Simulation for animation, adding resources when every 10 simulation time
         * i : simulation time
         * o : order index
         */
        Animation animation  = new Animation();
        animation.createUI();
        
        while(!isRun){
            System.out.print("");
        }
        
        myWarehouse.setWood(STOCK_WOOD);
        myWarehouse.setSteel(STOCK_STEEL);
        myWarehouse.setNail(STOCK_NAIL);
        
        animation = new Animation();
        animation.play();
        animation.setResource(myWarehouse.getWood(), myWarehouse.getSteel(), myWarehouse.getNail());
        for(int i = 0, o = 0; o < myOrders.length; i++){
            // draw top 10 order
            int max = (myOrders.length - o < 10) ? myOrders.length - o : 10 ;
            int[] types = new int[max];
            for(int j = 0; j < max; j++){
                types[j] = myOrders[o+j].furnitures[0].type;
            }
            animation.drawOrder(types);
            
            Order curr_order = myOrders[o];
            if (checkResources(curr_order.furnitures)) {
                buildFurniture(curr_order.furnitures[0].type);
                myInventory.store(curr_order.furnitures[0].type, 1);
                o++;
                
                // animation functions
                animation.drawBuild(curr_order.furnitures[0].type, myWarehouse.getWood(), myWarehouse.getSteel(), myWarehouse.getNail()); // draw current manucfatured item
                animation.setInventory(myInventory.numChair, myInventory.numTable); // set the inventory animation
            }
            
            // add resources very 10 simulation time
            if(i != 0 && i % 10 == 0){
                addResources();
                animation.setResource(myWarehouse.getWood(), myWarehouse.getSteel(), myWarehouse.getNail());
            }
        }

        System.out.println("Simulation Ends");
        System.out.println("nChairs: " + myInventory.numChair);
        System.out.println("nTables: " + myInventory.numTable);
        /**
         * Make a furniture type random 1, 0; 1 is chair 0 is table
         *
         */

        // for (Order order : myOrders) {
        // if (checkResources(order.furnitures)) {
        // /**
        // * Wait 10 min
        // */
        // for (Furniture furniture : order.furnitures) {
        // buildFurniture(furniture.type);
        // }
        // System.out.printf("Order %d Success with %d furniture(s)\n", order.id + 1,
        // order.furnitures.length);
        // } else {
        // addResources();
        // for (Furniture furniture : order.furnitures) {
        // buildFurniture(furniture.type);
        // }
        // System.out.printf("Order %d Success with %d furniture(s)\n", order.id + 1,
        // order.furnitures.length);
        // }
        // }
        // System.out.println("Total Runtime: " + simulation_runtime);
    }

    /**
     * function to generate the furniture
     *
     * @param numOfOrders number of limited orders at runtime
     * @return
     */
    public static Order[] generateOrder(int numOfOrders) {
        Order[] orders = new Order[numOfOrders];

        for (int i = 0; i < numOfOrders; i++) {
            int id = i;
            int time = (int) (Math.random() * intervalOrderTime[1] + intervalOrderTime[0]);
            Furniture[] furnitures = generateFurnitures(1);
            orders[i] = new Order(id, time, furnitures);
        }
        return orders;
    }

    public static Furniture[] generateFurnitures(int numOfFurnitures) {
        Furniture[] furnitures = new Furniture[numOfFurnitures];
        for (int i = 0; i < numOfFurnitures; i++) {
            int type = (int) (Math.random() * 2 + 1);
            furnitures[i] = new Furniture(type);
        }
        return furnitures;
    }

    public static void buildFurniture(int type) {
        System.out.println("building " + type);
        int currWood = 0;
        int currSteel = 0;
        int currNail = 0;
        int processTime = 0;

        switch (type) {
        case 1:
            currWood = myWarehouse.takeWood(chairMaterials[0]);
            currSteel = myWarehouse.takeSteel(chairMaterials[1]);
            currNail = myWarehouse.takeNail(chairMaterials[2]);
            processTime = itemDuration[type - 1];
            break;

        case 2:
            currWood = myWarehouse.takeWood(tableMaterials[0]);
            currSteel = myWarehouse.takeSteel(tableMaterials[1]);
            currNail = myWarehouse.takeNail(tableMaterials[2]);
            processTime = itemDuration[type - 1];
            break;
        }

        simulation_runtime += processTime;

        System.out.println(type + " is built");
    }

    public static boolean checkResources(Furniture[] furnitures) {
        int requireWood = 0;
        int requireSteel = 0;
        int requireNail = 0;
        boolean isEnough = true; // false: not enough materials

        for (Furniture furniture : furnitures) {
            switch (furniture.type) {
            case 1:
                requireWood += chairMaterials[0];
                requireSteel += chairMaterials[1];
                requireNail += chairMaterials[2];
                break;
            case 2:
                requireWood += tableMaterials[0];
                requireSteel += tableMaterials[1];
                requireNail += tableMaterials[2];
                break;
            }
        }
        if (requireWood > myWarehouse.getWood()) {
            isEnough = false;
            System.out.println("Error: Not Enough Wood");
        }
        if (requireSteel > myWarehouse.getSteel()) {
            isEnough = false;
            System.out.println("Error: Not Enough Steel");
        }
        if (requireNail > myWarehouse.getNail()) {
            isEnough = false;
            System.out.println("Error: Not Enough Nail");
        }
        return isEnough;
    }

    public static void addResources() {
        double percentageFrom = 0.4;
        double percentageTo = 0.6;

        int randWood = rand(STOCK_WOOD * percentageFrom, STOCK_WOOD * percentageTo);
        int randSteel = rand(STOCK_STEEL * percentageFrom, STOCK_STEEL * percentageTo);
        int randNail = rand(STOCK_NAIL * percentageFrom, STOCK_NAIL * percentageTo);

        myWarehouse.addWoods(randWood);
        myWarehouse.addSteels(randSteel);
        myWarehouse.addNails(randNail);

        System.out.printf("Restocking Warehouse: Woods-%d | Steels-%d | Nails-%d\n", randWood, randSteel, randNail);
    }

    public static int rand(double from, double to) {
        return (int) (Math.random() * to + from);
    }
}

/**
 * 
 * 
 * public static boolean makeChair() { int currWood = myWarehouse.takeWood(3);
 * int currSteel = myWarehouse.takeSteel(5); int currNail =
 * myWarehouse.takeNail(10);
 * 
 * if (currWood == -1 || currSteel == -1 || currNail == -1) { if (currWood ==
 * -1) { System.out.println("Not Enough Wood"); } if (currSteel == -1) {
 * System.out.println("Not Enough Steel"); } if (currNail == -1) {
 * System.out.println("Not Enough Nail"); } return false; } else {
 * simulation_runtime += 3; return true; } }
 * 
 * public static boolean makeTable() { int currWood = myWarehouse.takeWood(5);
 * int currSteel = myWarehouse.takeSteel(3); int currNail =
 * myWarehouse.takeNail(15);
 * 
 * if (currWood == -1 || currSteel == -1 || currNail == -1) { if (currWood ==
 * -1) { System.out.println("Not Enough Wood"); } if (currSteel == -1) {
 * System.out.println("Not Enough Steel"); } if (currNail == -1) {
 * System.out.println("Not Enough Nail"); } return false; } else {
 * simulation_runtime += 5; return true; } }
 * 
 * 
 */
