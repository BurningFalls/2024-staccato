package com.staccato.memory.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.staccato.memory.domain.MemoryMember;

public interface MemoryMemberRepository extends JpaRepository<MemoryMember, Long> {
    List<MemoryMember> findAllByMemberIdOrderByMemoryCreatedAtDesc(long memberId);

    @Query("""
            SELECT mm FROM MemoryMember mm WHERE mm.member.id = :memberId
            AND ((mm.memory.term.startAt is null AND mm.memory.term.endAt is null)
            or (:date BETWEEN mm.memory.term.startAt AND mm.memory.term.endAt))
            ORDER BY mm.memory.createdAt DESC
            """)
    List<MemoryMember> findAllByMemberIdAndIncludingDateOrderByCreatedAtDesc(@Param("memberId") long memberId, @Param("date") LocalDate date);
}
