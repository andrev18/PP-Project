# PP-Project
Course Project

The main goal of the project is to create a in-memory database.

The database will be tested by implementing a generic database for a grocery.

The base functionalities of the databse will be : insert,delete,update,get.

The whole implementation will be developed around Builder Pattern.

An annotation processor will be implemented. The role of this processor is to configure the database instance.

For the database data structure it will be used a db.HashList.The hash key will be the Collection unique identifier and the List will hold the collection objects.

The database will contain the following components:

- DatabaseConfigurator - define collections types, create the database;

- DatabaseManager - object that handles the queries for the database;

- DatabaseQueryBuilder - basic query builder

# Documentation

https://en.wikipedia.org/wiki/In-memory_database

https://en.wikipedia.org/wiki/Hash_list

https://en.wikipedia.org/wiki/Builder_pattern

https://en.wikipedia.org/wiki/Java_annotation
