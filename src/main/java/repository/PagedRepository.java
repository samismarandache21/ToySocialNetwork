package repository;

import com.example.reteasociala.paging.Page;
import com.example.reteasociala.paging.Pageable;
import domain.Entity;

public interface PagedRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    public Page<E> findAllOnPage(Pageable pageable);
}
