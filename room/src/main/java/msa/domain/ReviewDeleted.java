package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ReviewDeleted extends AbstractEvent {

    private Long reviewId;
    private Long roomId;
    private String content;

    public ReviewDeleted(Review aggregate) {
        super(aggregate);
    }

    public ReviewDeleted() {
        super();
    }
    // keep

}
