package com.gym.service;

import com.gym.domain.GymClass;
import com.gym.domain.ClassSchedule;
import java.time.LocalDate;
import java.util.List;

public interface ClassService {
    boolean createClass(GymClass gymClass);
    GymClass getClassById(int classId);
    List<GymClass> getAllClasses();
    List<GymClass> searchClasses(String searchTerm);
    boolean updateClass(GymClass gymClass);
    boolean deleteClass(int classId);

    boolean createSchedule(ClassSchedule schedule);
    ClassSchedule getScheduleById(int scheduleId);
    List<ClassSchedule> getSchedulesByClassId(int classId);
    List<ClassSchedule> getAvailableSchedules(); // Only schedules with spots
    List<ClassSchedule> getSchedulesByDate(LocalDate date);
    boolean updateSchedule(ClassSchedule schedule);
    boolean deleteSchedule(int scheduleId);
}