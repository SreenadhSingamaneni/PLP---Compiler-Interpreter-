program EncapsulationTest;

class Person {
    private age: integer;

    public procedure SetAge(a: integer);
    public procedure GetAge();
}

class Student extends Person {
    private id: integer;
}
.