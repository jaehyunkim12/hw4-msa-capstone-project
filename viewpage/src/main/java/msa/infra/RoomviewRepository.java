package msa.infra;

import java.util.List;
import msa.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "roomviews", path = "roomviews")
public interface RoomviewRepository
    extends PagingAndSortingRepository<Roomview, Long> {
    // keep

}
