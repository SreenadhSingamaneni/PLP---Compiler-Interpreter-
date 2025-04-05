import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.io.IOException;

public class DelphiInterpreter {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java DelphiInterpreter <input file>");
            return;
        }

        try {
            CharStream input = CharStreams.fromFileName(args[0]);
            DelphiLexer lexer = new DelphiLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            DelphiParser parser = new DelphiParser(tokens);
            ParseTree tree = parser.program();

            ParseTreeWalker walker = new ParseTreeWalker();
            Interpreter interpreter = new Interpreter();
            walker.walk(interpreter, tree);

            interpreter.validateInheritance();
            interpreter.validateInterfaces();
            interpreter.validateEncapsulation();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}

class Interpreter extends DelphiBaseListener {
    private Map<String, List<String>> interfaceDefinitions = new HashMap<>(); // Interface -> Methods
    private Map<String, List<String>> classMethods = new HashMap<>(); // Class -> Methods
    private Map<String, List<String>> classImplements = new HashMap<>(); // Class -> Interfaces
    private Map<String, String> classInheritance = new HashMap<>(); // Child -> Parent
    private Map<String, Map<String, String>> classVariables = new HashMap<>(); // Class -> (Variable, Visibility)
    private String currentClassName; // Track the current class being processed
        private Map<String, Integer> objectInstances = new HashMap<>(); // ✅ Tracks objects created


        @Override
        public void enterInterfaceDeclaration(DelphiParser.InterfaceDeclarationContext ctx) {
            String interfaceName = ctx.IDENTIFIER().getText();
            List<String> methods = new ArrayList<>();
    
            if (ctx.interfaceBody() != null) {
                for (DelphiParser.MethodSignatureContext methodCtx : ctx.interfaceBody().methodSignature()) {
                    methods.add(methodCtx.IDENTIFIER().getText());
                }
            }
    
            interfaceDefinitions.put(interfaceName, methods);
            System.out.println("Interface defined: " + interfaceName + " with methods: " + methods);
        }

    @Override
public void enterClassDeclaration(DelphiParser.ClassDeclarationContext ctx) {
    currentClassName = ctx.IDENTIFIER(0).getText();
    classMethods.put(currentClassName, new ArrayList<>());
    classVariables.put(currentClassName, new HashMap<>());

    System.out.println("Class defined: " + currentClassName);

    // Handle inheritance (extends)
    if (ctx.IDENTIFIER().size() > 1 && ctx.getChild(2).getText().equals("extends")) {
        String parentClass = ctx.IDENTIFIER(1).getText();
        classInheritance.put(currentClassName, parentClass);
        System.out.println("Class " + currentClassName + " extends " + parentClass);
    }

   // Handle interface implementation (implements)
   if (ctx.IDENTIFIER().size() > 1) {
    for (int i = 1; i < ctx.IDENTIFIER().size(); i++) {
        if (ctx.getChild(i * 2).getText().equals("implements")) {
            String interfaceName = ctx.IDENTIFIER(i).getText();
            classImplements.computeIfAbsent(currentClassName, k -> new ArrayList<>()).add(interfaceName);
            System.out.println("Class " + currentClassName + " implements interface: " + interfaceName);
        }
    }
}
}

    @Override
    public void enterVariableDeclaration(DelphiParser.VariableDeclarationContext ctx) {
        String visibility = ctx.visibilitySpecifier().getText();
        String variableName = ctx.IDENTIFIER(0).getText();
        String variableType = ctx.IDENTIFIER(1).getText();

        // Ensure the current class has a variable map before assigning values
        classVariables.computeIfAbsent(currentClassName, k -> new HashMap<>());
        classVariables.get(currentClassName).put(variableName, visibility);

        System.out.println("Variable " + variableName + " of type " + variableType + " is " + visibility + " in " + currentClassName);
    }

    public void validateEncapsulation() {
        for (Map.Entry<String, Map<String, String>> classEntry : classVariables.entrySet()) {
            String className = classEntry.getKey();
            Map<String, String> variables = classEntry.getValue();

            for (Map.Entry<String, String> varEntry : variables.entrySet()) {
                String varName = varEntry.getKey();
                String visibility = varEntry.getValue();

                if (visibility.equals("private")) {
                    System.out.println("Encapsulation Warning: " + varName + " in " + className + " is private.");
                }
            }
        }
    }

    public void validateInheritance() {
        for (Map.Entry<String, String> entry : classInheritance.entrySet()) {
            String childClass = entry.getKey();
            String parentClass = entry.getValue();

            if (classMethods.containsKey(parentClass)) {
                List<String> parentMethods = new ArrayList<>(classMethods.get(parentClass)); // Copy parent methods
                List<String> childMethods = classMethods.getOrDefault(childClass, new ArrayList<>());

                parentMethods.removeAll(childMethods);

                if (!parentMethods.isEmpty()) {
                    System.err.println("Error: Class " + childClass + " does not fully inherit from " + parentClass);
                    System.err.println("Missing inherited methods: " + parentMethods);
                } else {
                    System.out.println("Class " + childClass + " successfully inherits from " + parentClass);
                }
            } else {
                System.err.println("Error: Parent class " + parentClass + " not found for child " + childClass);
            }
        }
    }

    @Override
    public void enterMethodDeclaration(DelphiParser.MethodDeclarationContext ctx) {
        if (currentClassName != null) {
            String methodName = ctx.IDENTIFIER().getText();
            classMethods.get(currentClassName).add(methodName);
            System.out.println("Method '" + methodName + "' declared in class: " + currentClassName);
        }
    }
    public void validateInterfaces() {
        for (Map.Entry<String, List<String>> entry : classImplements.entrySet()) {
            String className = entry.getKey();
            List<String> interfaces = entry.getValue();
    
            for (String interfaceName : interfaces) {
                List<String> requiredMethods = interfaceDefinitions.get(interfaceName);
                List<String> implementedMethods = classMethods.getOrDefault(className, new ArrayList<>());
    
                if (requiredMethods == null) {
                    System.err.println("Error: Interface " + interfaceName + " is not defined");
                    continue;
                }
    
                List<String> missingMethods = new ArrayList<>(requiredMethods);
                missingMethods.removeAll(implementedMethods);
    
                if (!missingMethods.isEmpty()) {
                    System.err.println(" Error: Class " + className + " does not fully implement interface " + interfaceName);
                    System.err.println(" Missing interface methods: " + missingMethods);
                } else {
                    System.out.println("Class " + className + " fully implements interface " + interfaceName);
                }
            }
        }
    }
    @Override
    public void enterConstructorDeclaration(DelphiParser.ConstructorDeclarationContext ctx) {
        System.out.println("Constructor found '" + ctx.IDENTIFIER().getText() + "' of class:"+currentClassName);
    }

    @Override
    public void enterDestructorDeclaration(DelphiParser.DestructorDeclarationContext ctx) {
        System.out.println("Destructor found '" + ctx.IDENTIFIER().getText()+ "' of class:"+currentClassName);
    }

    @Override
    public void exitIntegerOutput(DelphiParser.IntegerOutputContext ctx) {
        System.out.println("Output: " + ctx.INT().getText());
    }
}