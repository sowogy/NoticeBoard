package com.example.SpringWeb.repository;

import com.example.SpringWeb.model.Authority;
import com.example.SpringWeb.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByMember(Member member);
}
