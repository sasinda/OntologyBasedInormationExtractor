package sigmacr.sentianalyzer.exceptions;

public class SentiAnalyzerException extends Exception{

    public SentiAnalyzerException() {
    }

    public SentiAnalyzerException(String message) {
        super(message);
    }

    public SentiAnalyzerException(String message, Throwable cause) {
        super(message, cause);
    }
}
