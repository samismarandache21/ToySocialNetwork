package services;

import ObserverClasses.Observable;
import ObserverClasses.Observer;
import ObserverClasses.ObserverAction;
import ObserverClasses.ActionType;
import domain.Mesaj;
import repository.database.MesajeDatabaseRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceMesaje implements Observable {
    private final MesajeDatabaseRepository repo; // Repository for handling messages
    private final List<Observer> observers;      // List of observers for notifications

    public ServiceMesaje(MesajeDatabaseRepository mesajeRepository) {
        this.repo = mesajeRepository;
        this.observers = new ArrayList<>(); // Initialize observers list
    }

    /**
     * Retrieves all messages exchanged between two users.
     *
     * @param userId1 ID of the first user.
     * @param userId2 ID of the second user.
     * @return List of messages between the two users.
     */
    public List<Mesaj> getMesajeUseri(Long userId1, Long userId2) {
        return repo.getEntities().values().stream()
                .filter(mesaj ->
                        (mesaj.getFrom().equals(userId1) && mesaj.getTo().contains(userId2)) ||
                                (mesaj.getFrom().equals(userId2) && mesaj.getTo().contains(userId1)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a specific message by its ID.
     *
     * @param id ID of the message.
     * @return The message with the specified ID.
     */
    public Mesaj getMesajById(Long id) {
        return repo.findOne(id).orElseThrow(() ->
                new RuntimeException("Mesajul cu ID-ul " + id + " nu a fost găsit!"));
    }

    /**
     * Adds a new message to the repository.
     *
     * @param mesaj   Content of the message.
     * @param from    ID of the sender.
     * @param to      ID of the recipient.
     * @param replyTo Optional ID of the message being replied to.
     */
    public void addMesaj(String mesaj, Long from, Long to, Optional<Long> replyTo) {
        // Prepare the message recipients (assuming single recipient for now)
        List<Long> recipients = new ArrayList<>();
        recipients.add(to);

        // Handle the reply message ID if provided
        Long replyToId = replyTo.orElse(null);

        // Create a new Mesaj object
        Mesaj newMesaj = new Mesaj(mesaj, from, recipients, LocalDateTime.now(), replyToId);

        // Save the message to the repository
        repo.save(newMesaj);

        // Notify observers of the new message
        notifyObservers(new ObserverAction(ActionType.NEWMESSAGE, from, to));
    }

    /**
     * Adds an observer to the service.
     *
     * @param observer The observer to add.
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the service.
     *
     * @param observer The observer to remove.
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers about an action.
     *
     * @param action The action to notify observers about.
     */
    @Override
    public void notifyObservers(ObserverAction action) {
        observers.forEach(observer -> observer.update(action));
    }

    public void saveMessage(Mesaj mesaj) {
        Optional<Mesaj> result = repo.save(mesaj);
        if (result.isEmpty()) {
            System.out.println("Message saved successfully!");
        } else {
            System.out.println("Message save failed!");
        }

        notifyObservers(new ObserverAction(ActionType.NEWMESSAGE, mesaj.getFrom(), mesaj.getTo().getFirst())); // Assuming single recipient
    }

    public List<Mesaj> getMessagesBetweenUsers(Long userId1, Long userId2) {
        // Get messages exchanged between two users
        if(repo.findConversation(userId1, userId2).isEmpty()) {
            createConversation(userId1, userId2);
        }
        return repo.getMessagesByConversation(repo.findConversation(userId1, userId2).get());
    }

    public Long createConversation(Long userId1, Long userId2) {
        return repo.createConversation(userId1, userId2); // This method must be implemented in your repository
    }

    public Optional<Long> findConversationBetweenUsers(Long userId1, Long userId2) {
        return repo.findConversation(userId1, userId2); // This method must be implemented in your repository
    }


}
