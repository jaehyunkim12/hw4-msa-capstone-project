package msa.domain;

import java.util.*;
import lombok.*;
import msa.domain.*;
import msa.infra.AbstractEvent;

@Data
@ToString
public class ReviewModified extends AbstractEvent {

    private Long reviewId;
    private Long roomId;
    private String content;

    public ReviewModified(Review aggregate) {
        super(aggregate);
    }

    public ReviewModified() {
        super();
    }
    // keep

}
