# Locomotor

Locomotor is a real and fictional vehicles comparator. It is based on predefined criterias that the user can select. It outputs a list of the vehicles that fit "the most" the user's query. The comparator works in a client/server design, allowing multiple users to use the same dataset at the same time.

The project offers 2 clients: 

- the first one is an administation interface which allow the user to modify the dataset.
- the other is an easy to use interface enabling the user to search for a vehicle and retrieve his booking.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

To run this project, you will need :

- JAVA SE 6+ (JavaFX 2.0 required)
- MongoDB installed and running

### Installing

To build this project, simply clone this repository and build the project.

```shell
git clone https://github.com/gaelfoppolo/locomotor
cd locomotor
make
```

To run the application, you need to start the core first.

```shell
make run-core
```

Then, you can use either the administration or the user interface

```shell
make run-user-interface
make run-admin-interface
```

To stop the core, use

```shell
make stop-core
```

### Fill the database

If you want to use a prefilled database, use the ones in the `data` directory.

```shell
for i in data/*.json; do mongoimport -d locomotor -c $(basename $i .json) $i; done
```

## Running the tests

To check that your code is compliant with our coding convention without having to compile the project, use:

```shell
make linter
```

## Authors

* **Bastien Philip** - *Initial work* - [ebatsin](https://github.com/ebatsin)
* **Gaël Foppolo** - *Initial work* - [gaelfoppolo](https://github.com/gaelfoppolo)

## License

This project is licensed under the GPLv3 license
