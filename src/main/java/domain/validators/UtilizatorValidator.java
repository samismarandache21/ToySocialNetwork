package domain.validators;


import domain.Utilizator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        if (entity == null) {
            throw new ValidationException("Utilizator object is null");
        }
        if(entity.getUsername().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid");
        if(entity.getParola().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid");
    }
}
