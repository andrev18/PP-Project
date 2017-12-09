package main;

import annotations.BucketConfig;

import buckets.AppBucketImpl;
import db.BucketDatabase;
import db.BucketDatabaseManager;
import model.User;
import processor.BucketProcessor;


import java.util.LinkedList;


public class Main {

    private static AppBucketImpl bucket;

    public static void main(String[] args) {
        // write your code here


        bucket = AppBucketImpl.getInstance();


        System.out.println(bucket.userBao.insert(new User("Andrei", 20)));

        System.out.println(bucket.userBao.insert(new User("Daniel", 25)));

        System.out.println(bucket.userBao.insert(new User("Alexandru", 30)));


        System.out.println(bucket.userBao.insert(new User("Andrei", 28)));


        LinkedList<User> usersWithAndreiName = bucket.userBao
                .query()
                .equalsTo("name", "Andrei")
                .equalsTo("age", 28)
                .find();


        for (User user :
                usersWithAndreiName) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }


        LinkedList<User> userThatStartsWithA = bucket.userBao.query()
                .filter(user -> user.getName().startsWith("A"))
                .find();


        for (User user :
                userThatStartsWithA) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }


        LinkedList<User> userWithAgeLessThan = bucket.userBao.query()
                .lesserThan("age", 26)
                .find();


        System.out.println("Users with age less than " + 26);


        for (User user :
                userWithAgeLessThan) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }

        LinkedList<User> userWithAgeGreatThan = bucket.userBao.query()
                .greaterThan("age", 26)
                .find();


        System.out.println("Users with age great than " + 26);


        for (User user :
                userWithAgeGreatThan) {
            System.out.println(user.getName() + " is " + user.getAge() + " old");
        }


    }
}
