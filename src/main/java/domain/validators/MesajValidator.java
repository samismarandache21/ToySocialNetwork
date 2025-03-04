package domain.validators;

import domain.Mesaj;

public class MesajValidator implements Validator<Mesaj>{

    @Override
    public void validate(Mesaj entity) throws ValidationException {
        if (entity == null){
            throw new ValidationException("Null message");
        }
        if(entity.getId() == null){
            throw new ValidationException("Id is null");
        }
    }
}
