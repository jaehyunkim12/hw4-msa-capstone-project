package msa.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import msa.config.kafka.KafkaProcessor;
import msa.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PolicyHandler {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReviewCreated'"
    )
    public void wheneverReviewCreated_UpdateReview(
        @Payload ReviewCreated reviewCreated
    ) {
        ReviewCreated event = reviewCreated;
        System.out.println(
            "\n\n##### listener UpdateReview : " + reviewCreated + "\n\n"
        );

        // Sample Logic //
        Room.updateReview(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReviewDeleted'"
    )
    public void wheneverReviewDeleted_UpdateReview(
        @Payload ReviewDeleted reviewDeleted
    ) {
        ReviewDeleted event = reviewDeleted;
        System.out.println(
            "\n\n##### listener UpdateReview : " + reviewDeleted + "\n\n"
        );

        // Sample Logic //
        Room.updateReview(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationConfirmed'"
    )
    public void wheneverReservationConfirmed_ConfirmReserve(
        @Payload ReservationConfirmed reservationConfirmed
    ) {
        ReservationConfirmed event = reservationConfirmed;
        System.out.println(
            "\n\n##### listener ConfirmReserve : " +
            reservationConfirmed +
            "\n\n"
        );

        // Sample Logic //
        Room.confirmReserve(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='ReservationCancelled'"
    )
    public void wheneverReservationCancelled_Cancel(
        @Payload ReservationCancelled reservationCancelled
    ) {
        ReservationCancelled event = reservationCancelled;
        System.out.println(
            "\n\n##### listener Cancel : " + reservationCancelled + "\n\n"
        );

        // Sample Logic //
        Room.cancel(event);
    }
    // keep

}
