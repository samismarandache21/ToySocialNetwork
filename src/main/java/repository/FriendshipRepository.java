package repository;

import com.example.reteasociala.paging.Page;
import com.example.reteasociala.paging.Pageable;
import domain.Friendship;
import domain.Tuple;

public interface FriendshipRepository extends PagedRepository<Tuple<Long, Long>, Friendship>{

    public Page<Friendship> findAllUserFriendshipsOnPage(Pageable pageable, Long userId);
}
