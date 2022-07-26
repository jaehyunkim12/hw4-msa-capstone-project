package msa.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import msa.PaymentApplication;
import msa.domain.PaymentApproved;
import msa.domain.PaymentCancelled;

@Entity
@Table(name = "Payment_table")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long payId;

    private Long rsvId;

    private Long roomId;

    private Boolean status;

    @PostPersist
    public void onPostPersist() {
        PaymentApproved paymentApproved = new PaymentApproved(this);
        paymentApproved.publishAfterCommit();

        PaymentCancelled paymentCancelled = new PaymentCancelled(this);
        paymentCancelled.publishAfterCommit();
    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
            PaymentRepository.class
        );
        return paymentRepository;
    }

    public static void cancelPayment(
        ReservationCancelRequested reservationCancelRequested
    ) {
        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        */

        /** Example 2:  finding and process
        
        repository().findById(reservationCancelRequested.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);


         });
        */

    }
}
