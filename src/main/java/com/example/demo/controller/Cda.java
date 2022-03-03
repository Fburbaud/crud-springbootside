package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import javax.management.AttributeNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Friends;
import com.example.demo.repository.FriendRepository;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class Cda {
		
	@Autowired
	private FriendRepository friendRep;
	
	@GetMapping("/friends")
	public List<Friends> getAllFriends(){
		return friendRep.findAll();
	}
	
	@PostMapping("/friendsAdd")
	public void AddFriend(@RequestBody Friends friend) {
		friendRep.save(friend);
	}
	
	@PostMapping("/friendsUpdate/{id}")
	public void FriendsUpdate(@PathVariable(value="id") Long friendid,
	@RequestBody Friends newFriend, Model model) throws AttributeNotFoundException {
		Friends friend = friendRep.findById(friendid).orElseThrow(
			()-> new AttributeNotFoundException("Pas trouvé" + friendid));
		friend.setNom(newFriend.getNom());
		friend.setPrenom(newFriend.getPrenom());
		friendRep.save(friend);
	}
	
	@GetMapping("/friendsDelete/{id}")
	public void DeleteFriends(@PathVariable(value="id") Long friendid) throws AttributeNotFoundException {
		Friends friend = friendRep.findById(friendid).orElseThrow(
			()-> new AttributeNotFoundException("Pas trouvé" + friendid));
		friendRep.delete(friend);
	}
	
	@GetMapping("/") // va cherche la page /home car on return "home"
	public String Home(Model model) {
		model.addAttribute("listeFriends", friendRep.findAll());
		return "home";
	}	
	
	@GetMapping("/about")
	public String About() {
		return "about";
	}
	
	@GetMapping("/contact")
	public String Contact() {
		return "contact";
	}
	
	@GetMapping("/contact/{id}")
	public String Contact(@PathVariable(value="id") Long friendid, Model model) {
		Optional<Friends> optionalFriend = friendRep.findById(friendid);
		if(optionalFriend.isPresent()) {
			model.addAttribute("friend",optionalFriend.get());
			return "contact";
		}else {
			return "home";
		}
	}
	
	@PostMapping("/contact")
	public String Contact(@Validated Friends friend, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "contact";
		}
		
		friendRep.save(friend);
		return "redirect:/";
	}
	
	@PostMapping("/contactEdit/{id}")
	public String ContactEdit(@PathVariable(value="id") Long friendid,
	@Validated Friends newFriend, BindingResult bindingResult, Model model) throws AttributeNotFoundException {
		if(bindingResult.hasErrors()) {
			return "contact";
		}
		Friends friend = friendRep.findById(friendid).orElseThrow(
			()-> new AttributeNotFoundException("Pas trouvé" + friendid));
		friend.setNom(newFriend.getNom());
		friend.setPrenom(newFriend.getPrenom());
		friendRep.save(friend);
		return "redirect:/";
	}
	
	@GetMapping("/delete{id}")
	public String Delete(@PathVariable(value="id") Long friendid) throws AttributeNotFoundException {
		Friends friend = friendRep.findById(friendid).orElseThrow(
			()-> new AttributeNotFoundException("Pas trouvé" + friendid));
		friendRep.delete(friend);
		return "redirect:/";
	}
}
