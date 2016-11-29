# Conventions de codage

## Dependency policy

*Locomotor* is composed of 3 distinct systems :

### Apps

The system is composed of 3 apps : the **core**, the **user interface** and the **administrator interface**. Each of this can be found in the directory *src/apps*.

* **core** : The core is the main app which offers an interface to the DB. It acts as a server and answers the user requests;
* **user interface** : The user interface is a client that allows the user to connect to the core, send requests and display the results.
* **administrator interface** : The administrator interface is a client that allows the user to connect to the core and manage the DB.

### Components

A collection of reusable componants which can be used by more than one app. The components can be found in the directory *src/components*.

### Libs 

A collection of libraries used by the project.

**note** : not all libraries used by the project are stored on the repository

The libraries can be found in the directory *libs*



## Naming convention

### Files

Each file must be named after the case sensitive name of the class it contains.

Each file can contain at most **one** outer class.

### Classes

Each class name must start by an upper case character. The rest of the name is written in camelcase.

Example : `ThisIsAClassName`

### Methods

Each method must start by a lower case character. The rest of the name is written in camelcase.

Example : `thisIsAMethodName()`

### Attributes

Each attribute must start by an underscore (_). The reste of the name is written in camelcase, starting with a lower case character.

Example : `_thisIsAnAttributeName`

### Local variables & parameters

The name must be written in camelcase starting with a lower case character.

Example : `thisIsAVariableName`

### Enumerations

The class containing the enumeration must respect the class naming convention. The item in the class must be all upper case separated by underscores (_)

Example : `TypePrefix.THIS_IS_A_VALUE`

### Acronymes

Acronymes in names must respect the naming convention in that file, even if it is not the way it is normally written

Example : `HttpCollection` (instead of `HTTPCollection`)



## Code formatting

### Layout and indentation

Indentation must use tabs.

This is the design prefered :

```java
class MyClass {
	int _myAttribute;
  
	public void myFunction(int parameter) {
		switch(parameter) {
			case 0:
				_myAttribute = 10;
				break;
            case 1:
				_myAttribute = 15;
				break;
			default:
				_myAttribute = 0;
				break;
		}
	}
}
```

### Javadoc comments

The *@param* **must** be filled when a parameter is given to a method. The *@return* **must** be filled when a method returns a value. Each class, enumeration and method must have at least a brief explanation of their purpose.

Example:

```java
/**
* This class contains a collection of ordered integers
*/
public class OrderedSet {
	/**
	* Check if a given value exists in the set
	* @param value The value to search for in the set
	* @return True if the value have been found, false otherwise
	*/
	public boolean exists(int value) {
		// ...
	}
}
```



### Spaces

Never use spaces between a method's or language construction's name and the parenthesis

Example : `if(value && otherValue)`

Always use spaces between an operator and its operands (except for unary operators)

Example : `int value = anotherValue && !check`