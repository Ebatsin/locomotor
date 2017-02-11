# Locomotor

Locomotor is a real and fictional vehicles comparator. It is based on predefined criterias that the user can select. It outputs a list of the vehicles that fit "the most" the user's query. The comparator works in a client/server design, allowing multiple users to use the same dataset at the same time.

![Search results](https://raw.githubusercontent.com/gaelfoppolo/locomotor/master/screenshot_results.png)

![Criterias selection](https://raw.githubusercontent.com/gaelfoppolo/locomotor/master/screenshot_selection.png)

The project offers 2 clients: 

- the first one is an administation interface which allow the user to modify the dataset.
- the other is an easy to use interface enabling the user to search for a vehicle and retrieve his booking.

#### A presentation of the project is available in [French](http://gaelfoppolo.github.io/locomotor/presentation/).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine.

#### [A wiki is also available, and give and more technical insight of the project: architecture, API, comparison algorithms, etc.](https://github.com/gaelfoppolo/locomotor/wiki)

*Note: this project does not compile under Windows environment.*

### Prerequisites

To run this project, you will need :

- [OpenJDK](http://openjdk.java.net/) 8+ (JDK and JRE)
- [OpenJFX](https://wiki.openjdk.java.net/display/OpenJFX/Main) 8+
- [MongoDB](https://www.mongodb.com/) 3.4+ installed and running

### Installing

To build this project, simply clone this repository and build the project.

```shell
git clone https://github.com/gaelfoppolo/locomotor
cd locomotor
make all
```

To run the application, you need to start the core first.

```shell
make run-core
```

Then, you can choose the interface to use:

|         User          |            Admin             |
| :-------------------: | :--------------------------: |
| `make run-front-user` | `make run-front-user —admin` |

### Fill the database

If you want to use a prefilled database, use the ones in the `data` directory.

To import use:

```shell
make import-database
```

The images of the vehicles are not provided, but you can easily find them on the Internet.

## Running the tests

To check that your code is compliant with our coding convention without having to compile the project, use:

```shell
make linter
```

## Building documentation

A [documentation](http://gaelfoppolo.github.io/locomotor/) is also available, with graph to a better understanding of our process.

You can also build it on your own, just make sure you have installed [Doxygen](http://www.stack.nl/%7Edimitri/doxygen/) and [Graphviz](http://www.graphviz.org/).

Then browse to the root of the project folder and run:

```shell
make doc
```

#### See [makefile](https://github.com/gaelfoppolo/locomotor/blob/master/makefile) for further explanations.

## Authors

* **Bastien Philip** - *Initial work* - [ebatsin](https://github.com/ebatsin)
* **Gaël Foppolo** - *Initial work* - [gaelfoppolo](https://github.com/gaelfoppolo)

## License

This project is licensed under the GPLv3 license, see [LICENSE.md](https://raw.githubusercontent.com/gaelfoppolo/locomotor/master/LICENSE.md).