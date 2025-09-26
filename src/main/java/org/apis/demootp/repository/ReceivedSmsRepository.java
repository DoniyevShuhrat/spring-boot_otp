package org.apis.demootp.repository;

import org.apis.demootp.domain.ReceivedSms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReceivedSmsRepository extends JpaRepository<ReceivedSms, Long> {

    // HOZIRCHA ICHIGA HECH NARSA YOZISH SHART EMAS!
    // Spring Data JPA biz uchun quyidagi metodlarni avtomatik yaratib beradi:
    // .save(receivedSms) -> Saqlash yoki yangilash
    // .findById(id) -> ID bo'yicha topish
    // .findAll() -> Hamma yozuvlarni topish
    // .deleteById(id) -> ID bo'yicha o'chirish
    // ... va yana ko'plab boshqa metodlar.

    // Kelajakda, agar sizga maxsus qidiruv kerak bo'lsa, shu yerga qo'shasiz. Masalan:
    // Optional<ReceivedSms> findByCorrelationId(UUID correlationId);
}
