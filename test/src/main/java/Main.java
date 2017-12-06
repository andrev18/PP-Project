

import model.User;

import java.util.LinkedList;


public class Main {

    private BucketProcessor processor;

    public static void main(String[] args) {
	// write your code here

        BucketDatabaseManager dbManager = BucketDatabase
                .newInstance()
                .declare()
                .createCollection(User.class)
                .end();



        System.out.println(dbManager.where(User.class)
                .insert(new User("Andrei", 20)));

        System.out.println(dbManager.where(User.class)
                .insert(new User("Daniel", 25)));

        System.out.println(dbManager.where(User.class)
                .insert(new User("Alexandru", 30)));


        System.out.println(dbManager.where(User.class)
                .insert(new User("Andrei", 28)));


        LinkedList<User> usersWithAndreiName = dbManager.where(User.class)
                .equalsTo("name","Andrei")
                .equalsTo("age",28)
                .find();


        for (User user :
                usersWithAndreiName) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }


        LinkedList<User> userThatStartsWithA = dbManager.where(User.class)
                .filter(user -> user.getName().startsWith("A"))
                .find();


        for (User user :
                userThatStartsWithA) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }



        LinkedList<User> userWithAgeLessThan = dbManager.where(User.class)
                .lesserThan("age",26)
                .find();


        System.out.println("Users with age less than "+ 26);


        for (User user :
                userWithAgeLessThan) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }

        LinkedList<User> userWithAgeGreatThan = dbManager.where(User.class)
                .greaterThan("age",26)
                .find();


        System.out.println("Users with age great than "+ 26);


        for (User user :
                userWithAgeGreatThan) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }



    }
}
