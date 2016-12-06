# Coding conventions

## Naming convention

### Files

Each file must be named after the case sensitive name of the class it contains.

Each file can contain at most **one** outer class, interface of enumeration.

### Classes

Each class name must start with an upper case character. The rest of the name is written in **camelCase**.

example: `ThisIsAClassName`

### Methods

Each method must start with a lower case character. The rest of the name is written in **camelCase**.

example: `thisIsAMethodName()`

### Attributes

Each attribute must start with an underscore (`_`). The rest of the name is written in **camelCase**, starting with a lower case character.

example: `_thisIsAnAttributeName`

### Local variables & parameters

The name must be written in **camelCase** starting with a lower case character.

example: `thisIsAVariableName`

### Enumerations

The class containing the enumeration must respect the class naming convention. The item in the class must be all upper case separated by underscores (`_`).

example: `TypePrefix.THIS_IS_A_VALUE`

### Acronymes

Acronymes in names must respect the naming convention defined here, even if it is not the way it is normally written.

example: `HttpCollection` (instead of `HTTPCollection`)

## Code formatting

### Layout and indentation

Indentation **must use tabs**.

This is the design prefered:

```java
class MyClass {
	int _myAttribute;
  
	public void myFunction(int parameter) {
		switch(parameter) {
			case 0: {
				_myAttribute = 10;
            	}
				break;
            case 1: {
				_myAttribute = 15;
            	}
				break;
			default: {
				_myAttribute = 0;
            	}
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