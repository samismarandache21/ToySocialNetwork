package services;

import domain.Utilizator;
import javafx.scene.image.Image;
import jdk.jshell.execution.Util;
import repository.database.UtilizatoriDatabaseRepository;

import java.util.Optional;

public class ServiceUtilizatori {
    private UtilizatoriDatabaseRepository repo;


    public ServiceUtilizatori(UtilizatoriDatabaseRepository repo) {
        this.repo = repo;
    }

    public Utilizator getUtilizator(Long userId){
        Optional<Utilizator> u = repo.findOne(userId);
        return u.isPresent() ? u.get() : null;
    }

    public void addUtilizator(String username, String password) {
        Optional<Utilizator> rez = repo.existaUsername(username);
        if(rez.isPresent()){
            throw new RuntimeException("Username already exists");
        }
        Utilizator u = new Utilizator(username, password);
        Optional<Utilizator> opt = repo.save(u);
        if (opt.isPresent()) {
            throw new RuntimeException("Error saving the user");
        }
    }

    public Optional<Utilizator> existaUtilizator(String username) {
        return repo.existaUtilizator(username);
    }

    public Optional<Utilizator> existaUsername(String username) {
        return repo.existaUsername(username);
    }

    public Image getImage(long user_id){
        return repo.getProfilePic(user_id);
    }

}
