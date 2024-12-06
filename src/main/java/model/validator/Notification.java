package model.validator;

import java.util.*;

//T inseamna ca poate fi utilizat cu orice operatie ex user, book
public class Notification<T>{
    private T result; //rezultatul operatiei
    private final List<String> errors; //mesaj de eroare

    public Notification(){
        this.errors = new ArrayList<>();
    }

    public void addError(String error){
        this.errors.add(error);
    }

    public boolean hasErrors(){
        return !this.errors.isEmpty();
    }

    public void setResult(T result) {
        this.result = result;
    }

    public T getResult() {
        if (hasErrors()){
            throw new ResultFetchException(errors);
        }
        return result;
    }

    public String getFormattedErrors(){
        return String.join("\n", errors);
    }
}