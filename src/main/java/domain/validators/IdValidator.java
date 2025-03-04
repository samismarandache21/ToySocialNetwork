package domain.validators;

public class IdValidator implements Validator<String>{
    @Override
    public void validate(String id){
        if (id == null || id.isEmpty())
            throw new ValidationException("Id is null or empty");
        int idUser;
        try{
            idUser = Integer.parseInt(id);
        }
        catch(NumberFormatException e){
            throw new ValidationException("Id-ul nu este valid");
        }
        if(idUser < 0){
            throw new ValidationException("Id-ul nu este valid");
        }
    }
}
