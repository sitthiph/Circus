package Circus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

public class Circus {
    public static void main(String[] args) throws FileNotFoundException {
        CDLLPerson data = startData();
        Scanner console = new Scanner(System.in);
        int input;
        System.out.println(
                "(1)\tPrint list alphabetically\n" +
                "(12)\tPrint list by IdNum\n" +
                "(3)\tInsert new Employee\n" +
                "(4)\tDelete an Employee\n" +
                "(5)\tPrint only a particular category list of employees alphabetically\n" +
                "(6)\tPrint entire list of all employees by category alphabetically\n" +
                "(7)\tAdd a category\n" +
                "(8)\tDelete a category (and ALL corresponding employees)\n" +
                "(9)\tQuit");

        while(true) {
            while (true) {
                while (!console.hasNextInt()) {
                    console.nextLine();
                    System.out.print("Not an integer");
                }
                input = console.nextInt();
                if (input < 1 || input > 9)
                    System.out.println("Not an number between 1 - 9, re-input");
                else
                    break;
            }
            TreeList tree;
            String info;
            switch (input) {
                case 1:
                    tree = new TreeList(new TreeNode(data.getHead()), new AlphaComparator());
                    runTree(tree, data);
                    System.out.println(tree.toString());
                    break;
                case 2:
                    tree = new TreeList(new TreeNode(data.getHead()), new SSNComparator());
                    runTree(tree, data);
                    System.out.println(tree.toString());
                    break;
                case 3:
                    info = EmployeePrompt(console);
                    data.add(new CDLLPersonNode(new PersonNode(new Scanner(info))));
                    break;
                case 4:
                    System.out.println("SSN?");
                    info = console.next();
                    tree = new TreeList(new TreeNode(data.getHead()), new SSNComparator());
                    data.remove(tree.getNode(info));
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    System.exit(0);
            }
        }
    }
    private static CDLLPerson startData() throws java.io.FileNotFoundException {
        Scanner con = new Scanner(new
                File("C:\\Users\\tienp\\IdeaProjects\\Advance Java Programming\\Projects\\Circus\\CircusEmployees.txt"));
        CDLLPerson data = new CDLLPerson(new CDLLPersonNode(new PersonNode(con)));
        while (con.hasNext()) {
            data.add(new CDLLPersonNode(new PersonNode(con)));
        }
        return data;
    }
    private static void runTree(TreeList tree, CDLLPerson data) {
        CDLLPersonNode current = data.getHead().getNext();
        while(current != data.getTail()) {
            tree.add(current.getPerson());
            current = current.getNext();
        }
        if(current != data.getHead())
            tree.add(current.getPerson());
    }
    private static String EmployeePrompt(Scanner console){
        System.out.println("Last Name, First Name, Middle Initial, SSN, Category, Tittle");
        String info = "";
        info = repeatCon(console, info);
        info = repeatCon(console, info);
        return info;
    }
    private static String repeatCon(Scanner console, String info) {
        if(console.hasNext())
            info += console.next() + " ";
        if(console.hasNext())
            info += console.next() + " ";
        if(console.hasNext())
            info += console.next() + " ";
        return info;
    }
}

class PersonNode {
    private String firstName;
    private String lastName;
    private char middleName;
    private String SSN;
    private String category;
    private String tittle;

    PersonNode(Scanner con) {
        String firstName, lastName, SSN, category, tittle;
        char middleName;
        lastName = con.next();
        firstName = con.next();
        middleName = con.next().charAt(0);
        SSN = con.next();
        category = con.next();
        tittle = con.next();

        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.SSN = SSN;
        this.category = category;
        this.tittle = tittle;
    }

    String getFirstName() { return firstName; }
    String getLastName() { return lastName; }
    char getMiddleName() { return middleName; }
    String getSSN() { return SSN; }
    String getCategory() { return category; }
    String getTittle() { return tittle; }

    @Override
    public String toString() {
        return String.format("%-30s%-30s%-30s%-30s", (this.lastName + " " + this.firstName + " " +
                this.middleName), this.SSN, this.category, this.tittle);
    }
}

class CDLLPerson {
    private CDLLPersonNode head;
    private CDLLPersonNode tail;

    CDLLPerson(CDLLPersonNode head) {
        this.head = head;
        this.tail = head;
        this.head.setNext(this.tail);
        this.tail.setNext(this.head);
        this.head.setPrevious(this.tail);
        this.tail.setPrevious(this.head);
    }

    public CDLLPersonNode getHead() {
        return head;
    }
    public CDLLPersonNode getTail() {
        return tail;
    }

    void add(CDLLPersonNode node) {
        CDLLPersonNode holder;
        holder = this.tail;
        this.tail.setNext(node);
        this.tail = tail.getNext();
        this.tail.setNext(head);
        this.tail.setPrevious(holder);
        this.head.setPrevious(this.tail);
    }
    void remove(CDLLPersonNode del) {
        if (del == null) {
            System.out.println("No such Person exist");
            return;
        }
        CDLLPersonNode previous = del.getPrevious();
        del = del.getNext();
        del.setPrevious(previous);
        previous.setNext(del);
    }

    @Override
    public String toString(){
        String out = "";
        CDLLPersonNode current = this.head;
        out += current.getPerson().toString() + "\n";
        while(current != this.tail) {
            current = current.getNext();
            out += current.getPerson().toString() + "\n";
        }
        return out;
    }
}
class CDLLPersonNode {
    private CDLLPersonNode previous;
    private CDLLPersonNode next;
    private PersonNode person;

    CDLLPersonNode(PersonNode person){
        this.person = person;
    }

    CDLLPersonNode getNext() {
        return this.next;
    }
    CDLLPersonNode getPrevious() {
        return this.previous;
    }
    PersonNode getPerson() {
        return this.person;
    }

    void setNext(CDLLPersonNode next) {
        this.next = next;
    }
    void setPrevious(CDLLPersonNode previous) {
        this.previous = previous;
    }

}

@SuppressWarnings("Duplicates")
class TreeList {
    private TreeNode root;
    private Comparator<PersonNode> comparator;
    TreeList(TreeNode root, Comparator<PersonNode> compare) { this.root = root; this.comparator = compare; }

    void add(PersonNode personNode) {
        add(this.root, personNode);
    }
    private void add(TreeNode treeNode, PersonNode personNode) {
        if(this.comparator.compare(treeNode.getValue().getPerson(), personNode) > 0)
            if(treeNode.getLeft() == null)
                treeNode.setLeft(new TreeNode(new CDLLPersonNode(personNode)));
            else
                add(treeNode.getLeft(), personNode);
        else if(this.comparator.compare(treeNode.getValue().getPerson(), personNode) < 0)
            if(treeNode.getRight() == null)
                treeNode.setRight(new TreeNode(new CDLLPersonNode(personNode)));
            else
                add(treeNode.getRight(), personNode);
    }

    CDLLPersonNode getNode(String SSN) {
        return getNode(this.root, SSN);
    }
    private CDLLPersonNode getNode(TreeNode treeNode, String SSN) {
        if(treeNode.getValue().getPerson().getSSN().equals(SSN))
            return treeNode.getValue();
        else if((treeNode.getValue().getPerson().getSSN().compareTo(SSN)) > 0) {
            if(treeNode.getLeft() == null)
                return null;
            else
                return getNode(treeNode.getLeft(), SSN);
        } else {
            if(treeNode.getRight() == null)
                return null;
            else
                return getNode(treeNode.getRight(), SSN);
        }
    }

    public String toString() {
        return toString(this.root);
    }
    private String toString(TreeNode treeNode) {
        String holder = "";
        if(treeNode.getLeft() != null)
            holder += toString(treeNode.getLeft());
        holder += treeNode.getValue().getPerson().toString() + "\n";
        if(treeNode.getRight() != null)
            holder += toString(treeNode.getRight());
        return holder;
    }
}
@SuppressWarnings("Duplicates")
class TreeNode {
    private TreeNode left, right;
    private CDLLPersonNode value;

    TreeNode (CDLLPersonNode value) { this.value = value; }

    void setRight(TreeNode right) { this.right = right; }

    void setLeft(TreeNode left) { this.left = left; }

    CDLLPersonNode getValue() { return value; }

    TreeNode getLeft() { return left; }

    TreeNode getRight() { return right; }
}

@SuppressWarnings("Duplicates")
class AlphaComparator implements Comparator<PersonNode> {
    public int compare(PersonNode p1, PersonNode p2) {
        if((p1.getLastName().compareTo(p2.getLastName())) == 0)
            if((p1.getFirstName().compareTo(p2.getFirstName())) == 0)
                if(p1.getMiddleName() == (p2.getMiddleName()))
                    return p1.getSSN().compareTo(p2.getSSN());
                else
                    return (p1.getMiddleName() - (p2.getMiddleName()));
            else
                return p1.getFirstName().compareTo(p2.getFirstName());
        else
            return p1.getLastName().compareTo(p2.getLastName());
    }
}

@SuppressWarnings("Duplicates")
class SSNComparator implements Comparator<PersonNode> {
    public int compare(PersonNode p1, PersonNode p2) {
        return p1.getSSN().compareTo(p2.getSSN());
    }
}

class CategoryComparator implements Comparator<PersonNode> {
    public int compare(PersonNode p1, PersonNode p2) {
        return p1.getCategory().compareTo(p2.getCategory());
    }
}