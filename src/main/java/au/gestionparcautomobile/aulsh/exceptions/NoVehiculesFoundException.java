package au.gestionparcautomobile.aulsh.exceptions;

public class NoVehiculesFoundException extends RuntimeException {
    public NoVehiculesFoundException(String message) {
        super(message);
    }
}
