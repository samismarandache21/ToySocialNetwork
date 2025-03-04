package domain.validators;

import domain.Friendship;
import domain.Utilizator;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity == null || entity.getId() == null || (entity.getStatus() != 0 && entity.getStatus() != 1)){
            throw new ValidationException("Friendship not valid");
        }
    }
}