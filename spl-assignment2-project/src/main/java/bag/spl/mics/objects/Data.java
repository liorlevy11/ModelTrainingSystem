package bag.spl.mics.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {


    /**
     * Enum representing the Data type.
     */
    public enum Type {Images, Text, Tabular}

    public Type type;
    private int processed;
    private int size;

    public Data(Type type, int processed, int size) {
        this.type = type;
        this.processed = processed;
        this.size = size;
    }

    public Data(Type type, int size) {
        this.type = type;
        this.processed = 0;
        this.size = size;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getProcessed() {
        return processed;
    }

    public int getSize() {
        return size;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
