package com.gym.service;

import com.gym.domain.Booking;
import com.gym.domain.ClassSchedule;
import com.gym.repository.BookingRepository;
import com.gym.repository.ClassRepository;

import java.util.List;

public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ClassRepository classRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, ClassRepository classRepository) {
        this.bookingRepository = bookingRepository;
        this.classRepository = classRepository;
    }
    @Override
    public boolean bookClass(int userId, int scheduleId) {
        // 1. Validate schedule exists
        ClassSchedule schedule = classRepository.findScheduleById(scheduleId);
        if (schedule == null) {
            System.err.println("Schedule not found");
            return false;
        }

        if (schedule.getAvailableSpots() <= 0) {
            System.err.println("Class is full");
            return false;
        }

        if (hasUserBooked(userId, scheduleId)) {
            System.err.println("You have already booked this class");
            return false;
        }

        Booking booking = new Booking(userId, scheduleId, "CONFIRMED");
        boolean bookingSaved = bookingRepository.save(booking);

        if (!bookingSaved) {
            System.err.println("Failed to create booking");
            return false;
        }
        schedule.decrementSpots();
        boolean scheduleUpdated = classRepository.updateSchedule(schedule);
        if (!scheduleUpdated) {
            System.err.println("Warning: Booking created but spots not decremented");
        }
        System.out.println("Class booked successfully!");
        return true;
    }
    @Override
    public boolean cancelBooking(int bookingId, int userId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            System.err.println("Booking not found");
            return false;
        }
        if (booking.getUserId() != userId) {
            System.err.println("You can only cancel your own bookings");
            return false;
        }
        if (booking.isCancelled()) {
            System.err.println("Booking is already cancelled");
            return false;
        }
        booking.cancel();
        boolean bookingUpdated = bookingRepository.update(booking);
        if (!bookingUpdated) {
            System.err.println("Failed to cancel booking");
            return false;
        }

        ClassSchedule schedule = classRepository.findScheduleById(booking.getScheduleId());
        if (schedule != null) {
            schedule.incrementSpots();
            classRepository.updateSchedule(schedule);
        }
        System.out.println("Booking cancelled successfully");
        return true;
    }

    @Override
    public Booking getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId);
    }
    @Override
    public List<Booking> getUserBookings(int userId) {
        return bookingRepository.findByUserId(userId);
    }
    @Override
    public List<Booking> getScheduleBookings(int scheduleId) {
        return bookingRepository.findByScheduleId(scheduleId);
    }
    @Override
    public boolean isScheduleAvailable(int scheduleId) {
        ClassSchedule schedule = classRepository.findScheduleById(scheduleId);
        return schedule != null && schedule.hasAvailableSpots();
    }
    @Override
    public boolean hasUserBooked(int userId, int scheduleId) {
        List<Booking> userBookings = bookingRepository.findByUserId(userId);
        return userBookings.stream()
                .anyMatch(b -> b.getScheduleId() == scheduleId && b.isConfirmed());
    }
}