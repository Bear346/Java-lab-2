import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Automata {
    public static enum STATES {
        ON, OFF, WAIT, ACCEPT, CHECK, COOK
    }
    private int cash;
    private JSONObject menu;
    private int price;
    private STATES state;

    public Automata(){
        cash = 0;
        state = STATES.OFF;
    }

    protected void readMenu() {
        try {
            File file = new File("C:\\Users\\Mark\\javalabs\\Java-lab-2\\src\\main\\resources\\DrinksAndPrices.json");
            String stringOfMenu = FileUtils.readFileToString(file, "utf-8");
            menu = new JSONObject(stringOfMenu);
            System.out.println(menu.toString(4));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public ArrayList<String> getDrinks(){
        ArrayList<String> drinks = new ArrayList<String>();
        Iterator<String> menu_iterator = menu.keys();
        while (menu_iterator.hasNext()){
            drinks.add(menu_iterator.next());
        }
        return drinks;
    }

    protected STATES on() {
        if (state == STATES.OFF) {
            state = STATES.WAIT;
        }
        return state;
    }
    protected STATES off() {
        if (state == STATES.WAIT) {
            state = STATES.OFF;
        }
        return state;
    }

    public int coin(int buyersCoin) {
        if (state == STATES.WAIT) {
            state = STATES.ACCEPT;
        }
        if (state == STATES.ACCEPT && buyersCoin > 0) {
            cash = cash + buyersCoin;
        } else {
            cancel();
        }
        return cash;
    }

    public int choice(String drinkName) {
        price = menu.getInt(drinkName);
        state = STATES.CHECK;
        System.out.println("The price of your " + drinkName + " is: " + price + " rubles");
        return price;
    }

    protected int check() {
        int change = 0;
        if (price <= cash && state == STATES.CHECK) {
            change = cash - price;
            System.out.println("Your have paid " + cash + " rubles. Take your change, please: " + change + " rubles");
            return change;
        }
        if (price >= cash && state == STATES.CHECK) {
           cancel();
        }
        //System.out.println("Your have paid " + cash + " rubles");
        return cash;
    }

    protected STATES getState() {
        return state;
    }

    public void setState(STATES state) {
        this.state = state;
    }

    protected STATES cancel() {
        if (price >= cash && state == STATES.CHECK) {
            state = STATES.WAIT;
            System.out.println("Sorry, this amount isn`t enought, take your coins back: " + cash + " rubles");
        }
        return state;
    }

    protected void cook() {
        if (state == STATES.CHECK) {
            state = STATES.COOK;
            System.out.println("Your drink is cooking, wait please...");
            state = STATES.WAIT;
            System.out.println("Take your drinks and enjoy it! I hope to see you again!");
        }
        finish();
    }

    protected void finish() {
        if (state == STATES.COOK && cash == 0) {
            state = STATES.WAIT;
        }
    }
}
