package sigmacr.prclassifier;

/**
 * User: pulasthi
 * Date: 7/1/13
 * Time: 2:42 PM
 */
public class ClassifierException extends Exception{
    public ClassifierException() {
    }

    public ClassifierException(String message) {
        super(message);
    }

    public ClassifierException(String message, Throwable cause) {
        super(message, cause);
    }
}
