import java.util.*;
import java.io.*;

// This class will run the scanning, correctly pack the parsed values into Person objects, and print/write them
public class NameScanner {
    public static void main(String[] args) throws FileNotFoundException {
        // Handle the error if there is no input file specified
        try {
            File f = new File(args[0]); // Take first commandline argument as input text file
            // Using a TreeMap with last name as key and the corresponding person as value will
            // help us store the attributes of a person while also sorting them alphabetically
            TreeMap<String, Person> people = new TreeMap<>();
            scanNames(f, people);
            outputNames(people);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR: No input file specified!\nThe correct command is: java NameScanner Names.txt");
            return;
        }
    }

    // This method will scan the file into Person objects correctly
    public static void scanNames(File f, TreeMap<String, Person> people) throws FileNotFoundException{
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            // In java, strings are immutable so we will use a StringBuilder object
            StringBuilder tempLine = new StringBuilder(s.nextLine());
            int commaIndex = 0; // Keep track of where the comma is so we can separate the names
            int numberBeginIndex = 0; // Keep track of where the numbers begin so we can grab the phone number
            boolean foundFirstdigit = false; // This will help us not continually overwrite numberBeginIndex
            String firstName = "";
            String lastName = "";
            for (int i = 0; i < tempLine.length(); i++) {
                // If the line is just an empty line, skip it
                if (tempLine.length() == 0)
                    break;

                // Parse character by character
                switch (tempLine.toString().charAt(i)) {
                    // If it runs into a comment, just delete the rest of the line so we don't keep parsing it
                    case '#':
                        tempLine.delete(i, tempLine.length());
                        break;

                    // Skip over (
                    case '(':
                        break;

                    // Replace a ) with a "-" so we get the number in the right format
                    case ')':
                        tempLine.replace(i, i + 1, "-");
                        break;

                    // Save where the comma is and set lastName to the correct value
                    case ',':
                        commaIndex = i;
                        lastName = tempLine.substring(0, commaIndex);
                        break;

                    // Skip over spaces
                    case ' ':
                        break;

                    // Skip over tabs
                    case '\t':
                        break;

                    // Skip over new line characters
                    case '\n':
                        continue;

                    // This case handles the phone number digits
                    default:
                        // If we haven't found the first digit yet and the current character is a digit,
                        // set boolean flag to true, set firstName, and save numberBeginIndex
                        if (!foundFirstdigit && Character.isDigit(tempLine.charAt(i))) {
                            firstName = tempLine.substring(commaIndex + 1, i - 1);
                            foundFirstdigit = true;
                            numberBeginIndex = i;
                        }
                        break;
                }
            }
            // Construct a new person object with what we have parsed
            Person newPerson = new Person(lastName, removeWhiteSpaceFromName(firstName), tempLine.substring(numberBeginIndex, tempLine.length()));
            people.put(lastName, newPerson); // Add the new person to the map to be sorted
        }
    }

    // This method will output the names to the console as well as the SortedNames.txt file
    public static void outputNames(TreeMap<String, Person> people) {
        // Use a StringBuilder to get a large string to write to the output file
        StringBuilder outputString = new StringBuilder();
        // Iterate through the map
        Iterator<Map.Entry<String, Person>> currentPerson = people.entrySet().iterator();
        currentPerson.next();

        while (currentPerson.hasNext()) {
            // Getting the value of the map will return a person object
            Person tempPerson = currentPerson.next().getValue();
            // Write the info followed by a new line
            outputString.append(tempPerson.toString() + "\n");
            System.out.println(tempPerson.toString()); // Remove this line if you don't want to print to console
        }

        try { // Put this in a try block to handle IOException
            FileWriter outputFile = new FileWriter("SortedNames.txt");
            // Write the outputString to SortedNames.txt
            outputFile.write(outputString.toString());
            outputFile.close(); // Close the file so it saves the changes
        }

        // If we through an exception, output and error message
        catch (IOException e) {
            System.out.println("ERROR: Output file not found!");
        }
    }

    // This method removes white space from the firstName and adds a space to firstNames with a middle initial
    public static String removeWhiteSpaceFromName(String name) {
        // Again, use StringBuiler so we can continually update it
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            // If the character isn't a space, append it
            if (name.charAt(i) != ' ') {
                temp.append(name.charAt(i));
            }
        }

        // Add space for middle name (i.e. change DanielW. to Daniel W.)
        for (int i = 0; i < temp.length() - 1; i++) {
            // If the current char is lowercase the the next over is uppercase
            if (Character.isLowerCase(temp.charAt(i)) && Character.isUpperCase(temp.charAt(i + 1))) {
                // Append a space followed by the middle initial and a period
                temp.replace(i + 1, i + 3, " " + temp.charAt(i + 1) + ".");
                break; // Break out of loop because we are done
            }
        }
        return temp.toString();
    }
}