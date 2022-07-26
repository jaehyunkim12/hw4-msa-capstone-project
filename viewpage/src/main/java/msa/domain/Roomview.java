package msa.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Roomview_table")
@Data
public class Roomview {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long roomId;

    private String desc;
    private Integer reviewCnt;
    private Boolean roomStatus;
    private Long rsvId;
    private Integer rsvStatus;
    private Long payId;
    private Boolean payStatus;
}
