# OOP_Ex1

This project centered around the ComplexFunction implementation for the interface complex_function, that extend the function interface.
After creating the function implementation Monom and Polynom, 
this project explores the usefulnes and limitation of creating a function of functions.

The ComplexFunction represents variations of the following: 

plus(f(x),g(x))=f(x)+g(x)

mul(f(x),g(x))=f(x)*g(x)

div(f(x),g(x))=f(x)/g(x)

min(f(x),g(x))

max(f(x),g(x))

comp(f(x),g(x))=f(g(x))

note that every function can be in it self a ComplexFunction.
To create a ComplexFunction you may use:
*a single function

** a string representing the function operation (as shown above..) and two functions (f(x),g(x))

***an Operation (enum) and tow functions

enum Operation:
Plus is "plus", Times is "mul", Divid is "div", Max is "max", Min is "min", Comp is "comp" , None, Error

The last two are for monitoring and structural purposes.

**** initialize a ComplexFunction from string representation,
the string must be of the format shown above, so that f and g are of Polynom format(or Monom), 
or ComplexFunctions with base functions of Polynom format.
If you wish to use other functions you may do that via one of the constractors*. In this case make sure 
the function is properly implements the function interface.
Changes to a ComplexFunction c1 after being added to another ComplexFunction c2, will not change c2. 
However, Changes to a base function f will! cause changes to every ComplexFunction, that use f in its structure.
This may be used to create templet ComplexFunctions that are easy to manipulate.
If you wish to protect from such changes use the copy() method.
This will grantee that any changes made to one or more of the base function after the cloning, will not affect the copy.
note that every new function added is again exposed to changes.
The equal method is tricky, it will not detect singular points, and in cases of difference in very small section
it may not detect this difference. If the one of the function being compared is not a Polynom or Monom 
or Complexfunction (excluding complex function of single base function),
The results of equals will be of this function implementation if it can compare the two, otherwise
The answer will be the partial equality of the two functions, if equals return false the two functions
Are most certainly not equals. If equals return true the cases of Complex function containing min and max,
are the most difficult to ascertain  equality, since they may cause small sections of inequality.

Functions_GUI:
This class extends collection<function> and represent a collection of functions.
This class also offers the ability to read and save the collection to file, and have the option to draw
all the functions in the collection using stdDraw class. 
 

 








