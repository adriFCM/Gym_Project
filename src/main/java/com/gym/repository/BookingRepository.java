package com.gym.repository;

import com.gym.domain.Booking;
import java.util.List;

public interface BookingRepository {
    boolean save(Booking booking);
    Booking findById(int bookingId);
    List<Booking> findByUserId(int userId);
    List<Booking> findByScheduleId(int scheduleId);
    List<Booking> findAll();
    boolean update(Booking booking);
    boolean delete(int bookingId);
}