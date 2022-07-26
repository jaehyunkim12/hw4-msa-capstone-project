package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ReviewCreated extends AbstractEvent {

    private Long reviewId;
    private String roomId;
    private String content;

    public ReviewCreated(Review aggregate) {
        super(aggregate);
    }

    public ReviewCreated() {
        super();
    }
    // keep

}
