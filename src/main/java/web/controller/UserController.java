package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.service.UserService;
import web.entity.User;

import java.util.List;

@Controller
public class UserController {

    @Value(value = "${welcome.message}")
    private String message;

    @Autowired
    private UserService service;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", message);
        return "index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String allUsers(Model model) {
        List<User> getAllUsers = service.getAll();
        model.addAttribute("users", getAllUsers);
        return "all-users";
    }

    @RequestMapping(value = "/add-user", method = RequestMethod.POST)
    public String saveUser(@ModelAttribute("user") User user) {
        service.save(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/update-user", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute("user") User user) {
        service.save(user);
        return "redirect:/users";
    }

    @RequestMapping(value = "/update-user/{id}", method = RequestMethod.GET)
    public String showUpdateUserForm(@PathVariable("id") int userId, Model model) {
        User user = service.getById(userId);
        model.addAttribute("user", user);
        return "update-user";
    }

    @RequestMapping(value = "/add-user", method = RequestMethod.GET)
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "add-user";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String deleteUser(@RequestParam("id") int userId) {
        service.remove(userId);
        return "redirect:/users";
    }
}