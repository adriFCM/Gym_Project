package com.gym.repository;

import com.gym.domain.GymClass;
import com.gym.domain.ClassSchedule;
import java.util.List;

public interface ClassRepository {
    boolean saveClass(GymClass gymClass);
    GymClass findClassById(int classId);
    List<GymClass> findAllClasses();
    boolean updateClass(GymClass gymClass);
    boolean deleteClass(int classId);

    boolean saveSchedule(ClassSchedule schedule);
    ClassSchedule findScheduleById(int scheduleId);
    List<ClassSchedule> findSchedulesByClassId(int classId);
    List<ClassSchedule> findAllSchedules();
    boolean updateSchedule(ClassSchedule schedule);
    boolean deleteSchedule(int scheduleId);
}