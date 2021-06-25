package com.hashedin.mockview.repository;

import com.hashedin.mockview.model.Slot;
import com.hashedin.mockview.model.SlotStatus;
import com.hashedin.mockview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@Transactional
public interface SlotRepository extends JpaRepository<Slot, Integer> {

    List<Slot> findByInterviewDateAndSlotStatus(Date date, SlotStatus vacant);

    List<Slot> findByInterviewDateAndSlotStatusAndInterviewStartTimeGreaterThanEqualAndInterviewStartTimeLessThan(Date date, SlotStatus vacant, LocalTime startTime, LocalTime endTime);


    List<Slot> findByInterviewerAndInterviewDateGreaterThanAndSlotStatus(User loggedInUser, Date currentDate, SlotStatus vacant);

    @Modifying
    void deleteByIdIn(List<Integer> slotsIdToBeDeleted);
}
