package Library.Factory.impl;

import Library.Factory.api.IPatronFactory;
import Library.model.impl.Patron;

public class PatronFactory implements IPatronFactory {
    @Override
    public Patron createPatron(String name, String email, String phoneNumber) {
        return new Patron(name, email, phoneNumber);
    }
}
