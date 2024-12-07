package KMA.BeBookingApp.domain.booking.service.impl;

import KMA.BeBookingApp.domain.booking.dto.request.BookingRequest;
import KMA.BeBookingApp.domain.booking.dto.request.CheckAvailabilityRequest;
import KMA.BeBookingApp.domain.booking.dto.response.BookingResponse;
import KMA.BeBookingApp.domain.booking.dto.response.HoldAvailabilityResponse;
import KMA.BeBookingApp.domain.booking.entity.Booking;
import KMA.BeBookingApp.domain.booking.repository.BookingRepository;
import KMA.BeBookingApp.domain.booking.repository.HomestayAvailabilityRepository;
import KMA.BeBookingApp.domain.booking.service.abtract.BookingService;
import KMA.BeBookingApp.domain.booking.service.abtract.HomestayAvailabilityService;
import KMA.BeBookingApp.domain.common.dto.request.InitPaymentVnPayRequest;
import KMA.BeBookingApp.domain.common.enumType.booking.BookingStatus;
import KMA.BeBookingApp.domain.common.enumType.payment.PaymentMethod;
import KMA.BeBookingApp.domain.common.exception.AppErrorCode;
import KMA.BeBookingApp.domain.common.exception.AppException;
import KMA.BeBookingApp.domain.homestay.entity.Homestay;
import KMA.BeBookingApp.domain.homestay.repository.HomestayRepository;
import KMA.BeBookingApp.domain.common.dto.response.InitDepositResponse;
import KMA.BeBookingApp.domain.payment.service.abtract.PaymentService;
import KMA.BeBookingApp.domain.user.entity.User;
import KMA.BeBookingApp.domain.user.service.abtract.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
public class BookingServiceImpl implements BookingService {

    HomestayRepository homestayRepository;
    BookingRepository bookingRepository;
    HomestayAvailabilityService homestayAvailabilityService;
    PaymentService paymentService;
    AuthService authService;

    int MAX_BOOKING_PER_DAY = 3;
    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
//
        LocalDate checkinDate = request.getCheckinDate();
        LocalDate checkoutDate = request.getCheckoutDate();

        User user = authService.validateAndGetUserByAuthenticationName();
        PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod());

        Long homestayId = request.getHomestayId();
        Homestay homestay = homestayRepository.findByIdToBook(homestayId).orElseThrow(
                ()->new AppException(AppErrorCode.RESOURCE_NOT_FOUND)
        );

        if(homestay.getMaxGuests() < request.getGuests()){
            throw new AppException(AppErrorCode.BOOKING_INVALID);
        }

        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime startOfDay = currentDate.toLocalDate().atStartOfDay();

        LocalDateTime endOfDay = currentDate.plusDays(1).toLocalDate().atStartOfDay();

        boolean isMaxBookingPerDay = bookingRepository.isMaxBookingPerDay(user.getId(), MAX_BOOKING_PER_DAY, startOfDay, endOfDay);

        if (isMaxBookingPerDay) {
            throw new AppException(AppErrorCode.TOO_MANY_REQUEST);
        }


        //booking
        Booking booking  = bookingRepository.save(Booking.builder()
                .userId(user.getId())
                .homestayId(homestayId)
                .checkinDate(checkinDate)
                .checkoutDate(checkoutDate)
                .guests(request.getGuests())
                .note(request.getNote())
                .bookingStatus(BookingStatus.PENDING)
                .build());



        //availability
        HoldAvailabilityResponse holdAvailabilityResponse = homestayAvailabilityService.checkAvailabilityForBooking(
                CheckAvailabilityRequest.builder()
                        .bookingId(booking.getId())
                        .checkInDate(checkinDate)
                        .checkoutDate(checkoutDate)
                        .homestayId(homestayId)
                        .build()
        );



        //payment
        InitDepositResponse initDepositResponse =  InitDepositResponse.builder().build();

        if(paymentMethod.equals(PaymentMethod.VNPAY)){
             initDepositResponse =  paymentService.initPayment(InitPaymentVnPayRequest.builder()
                    .ipAddress(request.getIpAddress())
                    .bookingId(booking.getId())
                    .amountVND(holdAvailabilityResponse.getTotalPriceVnd())
                    .build()
            );
        }


        booking.setNights(holdAvailabilityResponse.getNights());
        booking.setAmountUSD(holdAvailabilityResponse.getTotalPriceUsd());
        booking.setPriceUSDPerNight(holdAvailabilityResponse.getPriceUsdPerNight());
        booking.setAmountVND(holdAvailabilityResponse.getTotalPriceVnd());
        booking.setPriceVNDPerNight(holdAvailabilityResponse.getPriceVndPerNight());

        Booking bookingSaved = bookingRepository.save(booking);
        return buildBookingResponse(homestay, holdAvailabilityResponse , initDepositResponse , bookingSaved);

    }

    private BookingResponse buildBookingResponse(Homestay homestay, HoldAvailabilityResponse priceResponse, InitDepositResponse initDepositResponse, Booking booking) {
        return BookingResponse.builder()

                .bookingId(booking.getId())
                .homestayId(homestay.getId())
                .expiredTransaction(initDepositResponse.getExpiredTransaction())
                .urlPayment(initDepositResponse.getUrl())
                .build();
    }

    @Transactional
    @Override
    public void markBooked(Long bookingId) {
        int effectRow = bookingRepository.markBooked(bookingId , BookingStatus.BOOKED);
        if (effectRow != 1) {
            log.error("[BookingService] Booking not found or status is not 'PENDING' for bookingId: {}", bookingId);
            throw new AppException(AppErrorCode.BOOKING_NOT_FOUND);
        }
        homestayAvailabilityService.markBooked(bookingId);
    }

    @Override
    public void cancelCronJobBooking(Long bookingId) {
            int updatedRows = bookingRepository.cancelCronJobBooking(bookingId);
            homestayAvailabilityService. cancelPendingAvailabilityDay(bookingId);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        User user = authService.validateAndGetUserByAuthenticationName();
        int updatedRows = bookingRepository.cancelBooking(bookingId, user.getId());

        if (updatedRows != 1) {
            throw new AppException(AppErrorCode.UNAUTHORIZED);
        }
        paymentService.cancelPayment(bookingId);
        homestayAvailabilityService.cancelPendingAvailabilityDay(bookingId);
        log.info("Booking ID {} successfully cancelled by user ID {}", bookingId, user.getId());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateCompletedBookings() {
        LocalDate today = LocalDate.now();
        int updatedRows = bookingRepository.markBookingsCompleted(today, BookingStatus.COMPLETED, BookingStatus.BOOKED);
        System.out.println("Updated " + updatedRows + " bookings to COMPLETED.");
    }


}
