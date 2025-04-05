# Delphi Interpreter Project

Team 74: Sreenadh Singamaneni(UFID : 19481205) , Srivathsav Kommineni (UFID : 23640441 )

--------------------
Introduction:
The Delphi Interpreter Project extends Pascal by incorporating Object-Oriented Programming (OOP) features like classes, constructors, destructors, and encapsulation. Inspired by Delphi, which is an OOP extension of Pascal (similar to how C++ extends C), this project uses ANTLR 4 to generate a parser and lexer, and a Java-based interpreter to process and execute Pascal-like code.

It includes an ANTLR grammar (`Delphi.g4`), a Java-based interpreter (`DelphiInterpreter.java`), and test cases (`integer1.pas`, `integer2.pas`, etc.) to validate functionality.

---------------------

##  Project Structure (File Name - info)

Delphi.g4 - ANTLR Grammar for Delphi-like Pascal
DelphiInterpreter.java - Java Interpreter
integer1.pas -  Basic Output Test
integer2.pas - Basic Output Test
cons-dest.pas - Class with Constructor & Destructor
encapsulation_test.pas - Encapsulation Test
inheritance_test.pas - Inheritance Test
interface_test.pas  - Interface Implementation Test
README.md - Project Documentation


-----------------------

1️⃣ Setup

### Use the following commands to install ANTLR 4 (First-Time Setup)

#### For Linux/Mac:

curl -O https://www.antlr.org/download/antlr-4.9.2-complete.jar

export CLASSPATH=".:antlr-4.9.2-complete.jar:$CLASSPATH"

alias antlr4='java -jar antlr-4.9.2-complete.jar'

alias grun='java org.antlr.v4.gui.TestRig'


#### On Windows (PowerShell)

Invoke-WebRequest -Uri "https://www.antlr.org/download/antlr-4.9.2-complete.jar" -OutFile "antlr-4.9.2-complete.jar"

$env:CLASSPATH = ".;antlr-4.9.2-complete.jar;$env:CLASSPATH"


---

2️⃣ Generate the Lexer & Parser

#### For Linux/Mac:

antlr4 -Dlanguage=Java Delphi.g4

javac *.java

#### On Windows (PowerShell)

java -jar antlr-4.9.2-complete.jar -Dlanguage=Java Delphi.g4

javac *.java


---

3️⃣ Running the Interpreter 

To run the interpreter with a `.pas` test file:

java DelphiInterpreter test1.pas

📌 Replace `test1.pas` with any test file you want to run.

---

##  Running Test Cases

java DelphiInterpreter <test-file.pas>

Example:

java DelphiInterpreter integer1.pas

------------------------



## 📝 Test Cases & Expected Output

1. Test Case: integer1.pas

Description: This test case checks the basic integer output functionality using the print() statement. It ensures that the interpreter correctly processes and prints a single integer value.

Expected Output: 45

2. Test Case: integer2.pas

Description: Similar to the previous test, this case verifies that the interpreter correctly prints another integer value. It helps confirm that the printing function works for different numeric inputs.

Expected Output: 123

3. Test Case: cons-dest.pas

Description: 
This test evaluates the proper implementation of constructors and destructors within a class. It defines a Person class and checks whether the interpreter correctly identifies its constructor (Create) and destructor (Destroy). This test ensures that objects can be properly initialized and cleaned up.

Expected Output:
Class defined: Person  
Constructor found 'Create' of class: Person  
Destructor found 'Destroy' of class: Person  

4. Test Case: 
encapsulation_test.pas

Description: This test verifies encapsulation rules, ensuring that private, public, and protected variables and methods function correctly. It defines a Person class with a private variable age and provides getter and setter methods. Then, a Student class attempts to extend Person but fails due to missing inherited methods, testing whether the interpreter enforces proper encapsulation constraints.

Expected Output:
Class defined: Person  
Variable age of type integer is private in Person  
Method 'SetAge' declared in class: Person  
Method 'GetAge' declared in class: Person  
Class defined: Student  
Class Student extends Person  
Variable id of type integer is private in Student  
Error: Class Student does not fully inherit from Person  
Missing inherited methods: [SetAge, GetAge]  
Encapsulation Warning: id in Student is private.  
Encapsulation Warning: age in Person is private.  

5. Test Case: inheritance_test.pas

Description: 
This test ensures that class inheritance works correctly. It defines a Parent class with a method Show and a Child class that extends Parent, adds a new method Draw, and overrides Show. The test checks whether inheritance relationships are properly established and whether overridden methods function as expected.

Expected Output:
Class defined: Parent  
Method 'Show' declared in class: Parent  
Class defined: Child  
Class Child extends Parent  
Method 'Draw' declared in class: Child  
Method 'Show' declared in class: Child  
Class Child successfully inherits from Parent  

6. Test Case: interface_test.pas

Description: 
This test validates interface implementation. It defines an interface IShape with a required method Draw, then implements IShape in the Circle class. The test checks whether the interpreter correctly enforces interface implementation and ensures that all required methods are declared in the implementing class.

Expected Output:
Interface defined: IShape with methods: [Draw]  
Class defined: Circle  
Class Circle implements interface: IShape  
Method 'Draw' declared in class: Circle  
Class Circle fully implements interface IShape  

----------------------

## 📌 Notes
- Make sure you compile and run the interpreter after any grammar changes.
- If you get an ANTLR error, regenerate the parser:
  
  antlr4 -Dlanguage=Java Delphi.g4
  javac *.java
  
-----------------------


