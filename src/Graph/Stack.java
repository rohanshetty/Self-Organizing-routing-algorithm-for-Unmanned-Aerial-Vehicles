package Graph;

/**
 * A stack data structure that will store all the geo-cordinates in the route of a drone
 * @author Rohan
 */
public class Stack {

    Link top;
    Link bottom;

    public Stack() {
        top = null;
        bottom = null;
    }

    public boolean isEmpty() {
        return (top == null);
    }

    public void insert(Link newLink) {
        if (isEmpty()) {
            bottom = newLink;
        } else {
            top.previous = newLink;
        }
        newLink.next = top;
        top = newLink;
    }

    public Link remove() {
        if (top.next == null) {
            bottom = null;
        } else {
            top.next.previous = null;
        }
        top = top.next;
        return top;
    }

    public Link seek() {
        return top;
    }

    public void displayListFromTop() {
        Link current = top;
        while (current != null) {
            System.out.println(current);
            current = current.next;
        }
    }

    public void displayListFromBottom() {
        Link current = bottom;
        while (current != null) {
            System.out.println(current);
            current = current.previous;
        }
    }

    public Link getTop() {
        return top;
    }

    public Link getBottom() {
        return bottom;
    }

}
