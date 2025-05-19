package Library.Factory.api;

import Library.model.api.IPatron;

public interface IPatronFactory {
    IPatron createPatron(String name, String email, String phoneNumber);
}
