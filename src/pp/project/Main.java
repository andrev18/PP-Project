package pp.project;

import pp.project.model.User;

import java.util.LinkedList;
import java.util.function.Predicate;

public class Main {

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



    }
}
