// We use a person class to store a last name, first name, and phone number.
// We also will write a toString() function that will output the person in the correct format.
public class Person {
    private String lastName;
    private String firstName;
    private String phoneNumber;

    public Person(String lastName, String firstName, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
    }

    // This method allows for easy string formatting
    public String toString() {
        return "Name: " + lastName + ", " + firstName + "\tPhone: " + phoneNumber;
    }
}
