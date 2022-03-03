package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Friends;

public interface FriendRepository extends JpaRepository<Friends, Long>{
	
}
