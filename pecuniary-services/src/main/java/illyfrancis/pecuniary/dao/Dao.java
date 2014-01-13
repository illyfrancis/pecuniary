package illyfrancis.pecuniary.dao;

import java.util.List;

public interface Dao<T> {
    public List<T> getAll();
    public T getById(String id);
    public T save(T entity);
    public void deleteById(String id);
}
