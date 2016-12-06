## Application architecture

*Locomotor* is composed of 3 distinct systems:

### Apps

The system is composed of 3 apps : the **core**, the **user interface** and the **administrator interface**.

- **core**: the core is the main app which offers an interface to the DB. It acts as a server and answers the user requests. It can be found in *src/locomotor/core*
- **user interface**: the user interface is a client that allows the user to connect to the core, send requests and display the results. It can be found in *src/locomotor/front/user*
- **administrator interface**: the administrator interface is a client that allows the user to connect to the core and manage the DB. It can be found in *src/locomotor/front/administration*

### Components

A collection of reusable components which can be used by more than one app. The components can be found in the directory *src/locomotor/components* for the elements shared between the 3 apps.

For the elements shared only between the 2 interfaces, the components can be found in the directory *src/locomotor/front/components*.

### Librairies

A collection of libraries used by the project. The libraries can be found in the directory *libs*

**Note**: not all libraries used by the project are stored on the repository.

### Data

A collection of JSON files, each file representing an export of a collection of the database. Can be found in the directory *data*. See README for further explanations.