package com.example.gym.dto;

import java.time.OffsetDateTime;

public class ClassSessionCreateRequest {

    private Long classTypeId;   // вид занятия (тяжёлая, кроссфит, ОФП)
    private Long roomId;        // зал
    private Long coachId;       // тренер
    private OffsetDateTime startAt;
    private Integer capacity;

    // геттеры / сеттеры
    public Long getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Long classTypeId) {
        this.classTypeId = classTypeId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getCoachId() {
        return coachId;
    }

    public void setCoachId(Long coachId) {
        this.coachId = coachId;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(OffsetDateTime startAt) {
        this.startAt = startAt;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
