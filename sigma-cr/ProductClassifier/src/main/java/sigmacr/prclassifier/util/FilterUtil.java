package sigmacr.prclassifier.util;

import sigmacr.prclassifier.ClassifierException;
import weka.filters.Filter;

import java.io.*;

public class FilterUtil {

    public static void saveFilter(Filter filter, String path) throws ClassifierException {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
            outputStream.writeObject(filter);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new ClassifierException("File saving failed",e);
        }
    }

    public static Filter loadFilter(String path) throws ClassifierException {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            return (Filter) inputStream.readObject();
        } catch (IOException e) {
            throw new ClassifierException("File reading failed",e);
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new ClassifierException("Object loading failed",e);
        }
    }
}
