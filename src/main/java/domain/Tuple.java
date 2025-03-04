package domain;

public class Tuple <E1,E2>{
    private E1 first;
    private E2 second;

    public Tuple(E1 first, E2 second) {
        this.first = first;
        this.second = second;
    }

    public E1 getFirst() {
        return first;
    }

    public E2 getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return first.toString() + ";" + second.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        Tuple other = (Tuple) obj;
        return first.equals(other.first) && second.equals(other.second) || first.equals(other.second) && second.equals(other.first);
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }
}
