# Locomotor

Locomotor is a real and fictional vehicles comparator. It is based on predefined criteria that the user can select. It outputs a list of the vehicles that fit the most the user's query. The comparator works in a client/server design, allowing multiple users to use the same dataset at the same time.

The project also offers 2 clients. The first one is an administation interface which allow the user to modify the dataset, the other is an easy to use interface enabling the user to search for a vehicle.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

### Prerequisites

To run this project, you will need :

- JAVA SE 6+ (JavaFX 2.0 required)
- MongoDB installed and running

### Installing

To build this project, simply clone this repository and build the project.

```
git clone https://github.com/gaelfoppolo/locomotor
cd locomotor
make
```

To run the application, you need to start the core first.

```
make run core
```

Then, you can use either the administration or the user interface

```
make run userI
make run adminI
```

To stop the core, use

```
make stop core
```

## Running the tests

No test implemented as of yet

## Authors

* **Bastien Philip** - *Initial work* - [ebatsin](https://github.com/ebatsin)
* **Gaël Foppolo** - *Initial work* - [gaelfoppolo](https://github.com/gaelfoppolo)

## License

This project is licensed under the GPLv3 license
